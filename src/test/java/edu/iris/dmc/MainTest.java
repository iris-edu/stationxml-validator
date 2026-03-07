package edu.iris.dmc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URL;

import org.junit.jupiter.api.Test;

import edu.iris.dmc.testutil.ProcessTest;

public class MainTest {

    @Test
    public void mainTest() throws Exception {
        URL url = MainTest.class.getClassLoader().getResource("pass.xml");
        String[] args = new String[] {
                "--verbose",
                "--ignore-warnings",
                "--input",
                url.getPath()
        };
        ProcessTest.ExecResult r = ProcessTest.runJavaMain(edu.iris.dmc.Application.class, args);
        boolean content2 = r.getStdout().contains("PASSED");
        assertTrue(content2);
    }

    @Test
    public void help() throws Exception {
        String[] args = new String[] {"--help"};
        ProcessTest.ExecResult r = ProcessTest.runJavaMain(edu.iris.dmc.Application.class, args);
        assertEquals(0, r.getExitCode());
    }

    @Test
    public void rules() throws Exception {
        String[] args = new String[] {"--rules"};
        ProcessTest.ExecResult r = ProcessTest.runJavaMain(edu.iris.dmc.Application.class, args);
        assertEquals(0, r.getExitCode());
    }

    @Test
    public void units() throws Exception {
        String[] args = new String[] {"--units"};
        ProcessTest.ExecResult r = ProcessTest.runJavaMain(edu.iris.dmc.Application.class, args);
        assertEquals(0, r.getExitCode());
    }
}
