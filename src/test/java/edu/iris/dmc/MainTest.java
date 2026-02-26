package edu.iris.dmc;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.URL;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ginsberg.junit.exit.ExpectSystemExitWithStatus;

public class MainTest {

    private PrintStream originalOut;
    private PrintStream originalErr;

    @BeforeEach
    void setUp() {
        originalOut = System.out;
        originalErr = System.err;
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    public void mainTest() throws Exception {

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        ByteArrayOutputStream errContent = new ByteArrayOutputStream();

        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));

        URL url = MainTest.class.getClassLoader().getResource("pass.xml");
        String[] args = new String[] {
                "--verbose",
                "--ignore-warnings",
                "--input",
                url.getPath()
        };

        Application.main(args);

        String sysout = outContent.toString().replace("\r", "");
        String syserr = errContent.toString().replace("\r", "");

        boolean content2 = sysout.contains("PASSED");
        boolean content3 = sysout.contains("\n");

        assertTrue(content2);
        assertTrue(content3);
    }

    @Test
    @ExpectSystemExitWithStatus(0)
    public void help() throws Exception {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        String[] args = new String[] {"--help"};
        Application.main(args);
    }

    @Test
    @ExpectSystemExitWithStatus(0)
    public void rules() throws Exception {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        String[] args = new String[] {"--rules"};
        Application.main(args);
    }

    @Test
    @ExpectSystemExitWithStatus(0)
    public void units() throws Exception {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        String[] args = new String[] {"--units"};
        Application.main(args);
    }
}
