package io.sideex.api;

import com.fasterxml.jackson.databind.JsonNode;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.sideex.api.config.Browser;
import io.sideex.api.config.Config;
import io.sideex.api.config.WebDriverConfig;

public class Driver {
    private Config config;
    private String runnerPath;

    public Driver(String runnerPath, Config config) {
        this.runnerPath = runnerPath;
        this.config = config;
    }

    public JsonNode run() throws IOException, InterruptedException, Exception {
        Proc proc = new Proc(runnerPath);
        ProcResult result = proc.run(config.toString());
        System.out.println(new String(result.getStderr().readAllBytes(), StandardCharsets.UTF_8));

        if (result.getStatus() != 0) {
            System.out.println(new String(result.getStderr().readAllBytes(), StandardCharsets.UTF_8));
            throw new Exception(new String(result.getStderr().readAllBytes(), StandardCharsets.UTF_8));

        }
        return parseReport(result.getStdout());
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

    public static void main(String[] args) throws IOException, InterruptedException, Exception {

        ArrayList<String> testSuites = new ArrayList<String>();
        testSuites.add("/home/fourcolor/Documents/sideex/sideex-webservice-client/test/test1.json");

        Browser browser = new Browser();
        Map<String, Object> caps = new HashMap();
        caps.put("browserName", "chrome");
        browser.setCapbility(caps);
        ArrayList<Browser> browsers = new ArrayList<Browser>();
        browsers.add(browser);

        WebDriverConfig webDriverConfig = new WebDriverConfig();
        webDriverConfig.setBrowsers(browsers);
        webDriverConfig.setServerUrl("http://172.17.0.1:4444");
        ArrayList<WebDriverConfig> webDriverConfigs = new ArrayList<WebDriverConfig>();
        webDriverConfigs.add(webDriverConfig);

        Config config = new Config();
        config.getInput().setTestSuites(testSuites);
        config.getWebdriver().setConfigs(webDriverConfigs);

        Driver driver = new Driver(
                "/home/fourcolor/Documents/sideex/sideex-2021/modules/main/dist/runner/sideex-runner-linux", config);

        JsonNode report = driver.run();
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(objectMapper.writeValueAsString(report));
    }
}
