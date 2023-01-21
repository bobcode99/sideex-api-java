package io.sideex.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.nio.charset.StandardCharsets;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import com.fasterxml.jackson.databind.ObjectMapper;
public class Proc {
    private String executable;

    public Proc(String executable) {
        this.executable = executable;
    }

    public JsonNode run(String input) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(this.executable, "--call-by-api");
        Process proc = pb.start();
        OutputStream stdin = proc.getOutputStream();
        InputStream stdout = proc.getInputStream();
        InputStream stderr = proc.getErrorStream();
        stdin.write(input.getBytes(StandardCharsets.UTF_8));
        stdin.flush();
        stdin.close();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stderr));
        JsonNode report = parseReport(stdout);
        String stderrStr;
        String line = null;
        while ((line = reader.readLine()) != null)
            System.out.println(line);
        int status = proc.waitFor();
        return report;
    }

    private JsonNode parseReport(InputStream input) throws IOException {
        String startToken = "INFO Start to send json report to api";
        String endToken = "INFO End of sending json report to api";
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        String line = null;
        boolean flag = false;
        StringBuilder reportStr = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            if (line.equals(endToken)) {
                flag = false;
            }
            if (flag) {
                reportStr.append(line);
            }
            if (line.equals(startToken)) {
                flag = true;
            }
        }
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(reportStr.toString());
        return jsonNode;
    }
}

class ProcResult {
    private InputStream stdout;
    // private InputStream stderr;
    private int status;

    ProcResult(InputStream stdout, InputStream stderr, int status) {
        this.status = status;
        this.stdout = stdout;
        // this.stderr = stderr;
    }

    public int getStatus() {
        return this.status;
    }

    public InputStream getStdout() {
        return this.stdout;
    }

    // public InputStream getStderr() {
    //     return this.stderr;
    // }
}
