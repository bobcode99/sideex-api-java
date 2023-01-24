package io.sideex.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.sideex.api.config.Browser;
import io.sideex.api.config.Config;
import io.sideex.api.config.WebDriverConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Driver {
    private final Config config;
    private final String runnerPath;

    public Driver(String runnerPath, Config config) {
        this.runnerPath = runnerPath;
        this.config = config;
    }

    public static void main(String[] args) throws Exception {

        ArrayList<String> testSuites = new ArrayList<String>();
        testSuites.add("/Users/user/sideex-things/runner-testcase/suiteOpenWebsite.json");

        Browser browser = new Browser();
        Map<String, Object> caps = new HashMap();
        caps.put("browserName", "safari");
        browser.setCapability(caps);
        ArrayList<Browser> browsers = new ArrayList<Browser>();
        browsers.add(browser);

        WebDriverConfig webDriverConfig = new WebDriverConfig();
        webDriverConfig.setBrowsers(browsers);
        webDriverConfig.setServerUrl("http://127.0.0.1:4445");
        ArrayList<WebDriverConfig> webDriverConfigs = new ArrayList<WebDriverConfig>();
        webDriverConfigs.add(webDriverConfig);

        Config config = new Config();
        config.getInput().setTestSuites(testSuites);
        config.getWebdriver().setConfigs(webDriverConfigs);

        Driver driver = new Driver(
                "/Users/user/sideex-things/runner-testcase/runner-executable-file-oneline/sideex-runner-exe", config);

        JsonNode report = driver.run();
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(objectMapper.writeValueAsString(report));
    }

    // private JsonNode parseReport(InputStream input) throws IOException {
    //     String startToken = "INFO Start to send json report to api";
    //     String endToken = "INFO End of sending json report to api";
    //     BufferedReader reader = new BufferedReader(new InputStreamReader(input));
    //     String line = null;
    //     boolean flag = false;
    //     StringBuilder reportStr = new StringBuilder();
    //     while ((line = reader.readLine()) != null) {
    //         if (line.equals(endToken)) {
    //             flag = false;
    //         }
    //         if (flag) {
    //             reportStr.append(line);
    //         }
    //         if (line.equals(startToken)) {
    //             flag = true;
    //         }
    //     }
    //     ObjectMapper objectMapper = new ObjectMapper();
    //     JsonNode jsonNode = objectMapper.readTree(reportStr.toString());
    //     return jsonNode;
    // }

    public JsonNode run() throws Exception {
        Proc proc = new Proc(runnerPath);
        JsonNode result = proc.run(config.toString());
        // System.out.println(new String(result.getStderr().readAllBytes(), StandardCharsets.UTF_8));

        // if (result.getStatus() != 0) {
        //     System.out.println(new String(result.getStderr().readAllBytes(), StandardCharsets.UTF_8));
        //     throw new Exception(new String(result.getStderr().readAllBytes(), StandardCharsets.UTF_8));

        // }
        return result;
    }
}
