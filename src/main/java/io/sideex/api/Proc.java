package io.sideex.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.nio.charset.StandardCharsets;

public class Proc {
    private String executable;

    public Proc(String executable) {
        this.executable = executable;
    }

    public ProcResult run(String input) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(this.executable, "--call-by-api");
        Process proc = pb.start();
        OutputStream stdin = proc.getOutputStream();
        InputStream stdout = proc.getInputStream();
        InputStream stderr = proc.getErrorStream();
        stdin.write(input.getBytes(StandardCharsets.UTF_8));
        stdin.flush();
        stdin.close();
        int status = proc.waitFor();
        return new ProcResult(stdout, stderr, status);
    }
}

class ProcResult {
    private InputStream stdout;
    private InputStream stderr;
    private int status;

    ProcResult(InputStream stdout, InputStream stderr, int status) {
        this.status = status;
        this.stdout = stdout;
        this.stderr = stderr;
    }

    public int getStatus() {
        return this.status;
    }

    public InputStream getStdout() {
        return this.stdout;
    }

    public InputStream getStderr() {
        return this.stderr;
    }
}
