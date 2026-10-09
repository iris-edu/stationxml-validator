package edu.iris.dmc.testutil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public final class ProcessTest {

    private ProcessTest() {}

    public static final class ExecResult {
        private final int exitCode;
        private final String stdout;
        private final String stderr;

        public ExecResult(int exitCode, String stdout, String stderr) {
            this.exitCode = exitCode;
            this.stdout = stdout;
            this.stderr = stderr;
        }

        public int getExitCode() {
            return exitCode;
        }

        public String getStdout() {
            return stdout;
        }

        public String getStderr() {
            return stderr;
        }
    }

    public static ExecResult runJavaMain(Class<?> mainClass, String... args) throws Exception {
        return runJavaMain(Duration.ofSeconds(30), StandardCharsets.UTF_8, mainClass, args);
    }

    public static ExecResult runJavaMain(Duration timeout, Class<?> mainClass, String... args) throws Exception {
        return runJavaMain(timeout, StandardCharsets.UTF_8, mainClass, args);
    }

    public static ExecResult runJavaMain(Duration timeout, Charset charset, Class<?> mainClass, String... args)
            throws Exception {

        Objects.requireNonNull(timeout, "timeout");
        Objects.requireNonNull(charset, "charset");
        Objects.requireNonNull(mainClass, "mainClass");

        String java = javaBin();
        String classpath = System.getProperty("java.class.path");

        List<String> cmd = new ArrayList<String>();
        cmd.add(java);
        cmd.add("-cp");
        cmd.add(classpath);
        cmd.add(mainClass.getName());
        if (args != null) {
            for (String arg : args) cmd.add(arg);
        }

        Process p = new ProcessBuilder(cmd).start();

        StreamCollector outCollector = new StreamCollector(p.getInputStream());
        StreamCollector errCollector = new StreamCollector(p.getErrorStream());

        Thread outThread = new Thread(outCollector, "proc-stdout");
        Thread errThread = new Thread(errCollector, "proc-stderr");
        outThread.setDaemon(true);
        errThread.setDaemon(true);
        outThread.start();
        errThread.start();

        boolean finished = p.waitFor(timeout.toMillis(), TimeUnit.MILLISECONDS);
        if (!finished) {
            // Kill it, then try to drain what we can.
            p.destroyForcibly();
            // Give the collectors a moment to see EOF after destroy.
            outThread.join(1000);
            errThread.join(1000);
            throw new AssertionError("Process timed out after " + timeout + ": " + joinCommand(cmd));
        }

        // Process ended, now ensure streams are fully drained.
        outThread.join(5000);
        errThread.join(5000);

        int code = p.exitValue();

        // If collection failed (rare), surface it clearly.
        if (outCollector.failure != null) {
            throw new IOException("Failed reading stdout for: " + joinCommand(cmd), outCollector.failure);
        }
        if (errCollector.failure != null) {
            throw new IOException("Failed reading stderr for: " + joinCommand(cmd), errCollector.failure);
        }

        String stdout = new String(outCollector.bytes.toByteArray(), charset);
        String stderr = new String(errCollector.bytes.toByteArray(), charset);

        return new ExecResult(code, stdout, stderr);
    }

    /**
     * Invokes a static method directly (in the same JVM) and captures stdout/stderr output.
     * Useful for testing methods that print output but don't call System.exit().
     *
     * @param clazz the class containing the method
     * @param methodName the name of the static method to invoke
     * @param args the arguments to pass to the method (can be null or empty)
     * @return an ExecResult with exitCode=0 and captured stdout/stderr
     * @throws Exception if the method cannot be found or invocation fails
     */
    public static ExecResult runMethod(Class<?> clazz, String methodName, Object... args) throws Exception {
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(methodName, "methodName");

        // Capture original stdout and stderr
        PrintStream originalOut = System.out;
        PrintStream originalErr = System.err;

        ByteArrayOutputStream capturedOut = new ByteArrayOutputStream();
        ByteArrayOutputStream capturedErr = new ByteArrayOutputStream();

        try {
            // Redirect stdout and stderr
            System.setOut(new PrintStream(capturedOut));
            System.setErr(new PrintStream(capturedErr));

            // Find and invoke the method
            Method method = findMethod(clazz, methodName, args);
            method.invoke(null, args);

        } finally {
            // Restore original stdout and stderr
            System.setOut(originalOut);
            System.setErr(originalErr);
        }

        String stdout = new String(capturedOut.toByteArray(), StandardCharsets.UTF_8);
        String stderr = new String(capturedErr.toByteArray(), StandardCharsets.UTF_8);

        return new ExecResult(0, stdout, stderr);
    }

    /**
     * Helper method to find a static method by name and argument count.
     * This handles the reflection needed to locate the correct overloaded method.
     */
    private static Method findMethod(Class<?> clazz, String methodName, Object... args) throws NoSuchMethodException {
        Method[] methods = clazz.getDeclaredMethods();

        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class<?>[] paramTypes = method.getParameterTypes();

                // Match by argument count
                if (paramTypes.length == (args != null ? args.length : 0)) {
                    method.setAccessible(true);
                    return method;
                }
            }
        }

        throw new NoSuchMethodException("Method " + methodName + " with " +
                (args != null ? args.length : 0) + " arguments not found in " + clazz.getName());
    }

    private static final class StreamCollector implements Runnable {
        private final InputStream in;
        private final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        private volatile IOException failure;

        private StreamCollector(InputStream in) {
            this.in = in;
        }

        public void run() {
            byte[] buf = new byte[8192];
            int n;
            try {
                while ((n = in.read(buf)) != -1) {
                    bytes.write(buf, 0, n);
                }
            } catch (IOException e) {
                failure = e;
            } finally {
                try {
                    in.close();
                } catch (IOException ignore) {
                    // ignore
                }
            }
        }
    }

    private static String javaBin() {
        return System.getProperty("java.home")
                + File.separator + "bin" + File.separator + (isWindows() ? "java.exe" : "java");
    }

    private static boolean isWindows() {
        String os = System.getProperty("os.name", "");
        return os.toLowerCase().contains("win");
    }

    private static String joinCommand(List<String> cmd) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cmd.size(); i++) {
            if (i > 0) sb.append(' ');
            String part = cmd.get(i);
            // basic quoting for readability
            if (part.indexOf(' ') >= 0 || part.indexOf('\t') >= 0) {
                sb.append('"').append(part.replace("\"", "\\\"")).append('"');
            } else {
                sb.append(part);
            }
        }
        return sb.toString();
    }
}