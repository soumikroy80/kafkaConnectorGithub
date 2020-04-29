package com.msqube.confluent;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.msqube.confluent.GitHubAPIHttpClient;
import com.msqube.confluent.GitHubSourceConnectorConfig;
import com.msqube.confluent.GitHubSourceTask;
import com.msqube.confluent.model.Issue;

import org.json.JSONObject;
import org.junit.Test;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.msqube.confluent.GitHubSourceConnectorConfig.*;
import static org.junit.Assert.*;

public class GitHubSourceTaskTest {

    private GitHubSourceTask gitHubSourceTask = new GitHubSourceTask();
    private Integer batchSize = 10;

    private Map<String, String> initialConfig() {
        Map<String, String> baseProps = new HashMap<>();
        baseProps.put(OWNER_CONFIG, "apache");
        baseProps.put(SINCE_CONFIG, "2017-04-26T01:23:44Z");
        baseProps.put(OWNER_TOKEN_CONFIG, "token----keys");
        baseProps.put(BATCH_SIZE_CONFIG, batchSize.toString());
        baseProps.put(TOPIC_CONFIG, "github-issues");
        return baseProps;
    }


    @Test
    public void test() throws UnirestException {
        gitHubSourceTask.config = new GitHubSourceConnectorConfig(initialConfig());
        gitHubSourceTask.nextQuerySince = Instant.parse("2017-01-01T00:00:00Z");
        gitHubSourceTask.gitHubHttpAPIClient = new GitHubAPIHttpClient(gitHubSourceTask.config);
        String url = "https://api.github.com/repos/soumikroy80/microservicepoc/issues?since=2020-02-29T00:00:00Z&state=all&direction=asc&sort=updated";
        System.out.println(url);
        HttpResponse<JsonNode> httpResponse = gitHubSourceTask.gitHubHttpAPIClient.getNextItemsAPI(url,gitHubSourceTask.config.getAuthTokenConfig());
        if (httpResponse.getStatus() != 403) {
            assertEquals(200, httpResponse.getStatus());
            Set<String> headers = httpResponse.getHeaders().keySet();
//            assertTrue(headers.contains("ETag"));
//            assertTrue(headers.contains("X-RateLimit-Limit"));
//            assertTrue(headers.contains("X-RateLimit-Remaining"));
//            assertTrue(headers.contains("X-RateLimit-Reset"));
            JSONObject jsonObject = (JSONObject) httpResponse.getBody().getArray().get(0);
            Issue issue = Issue.fromJson(jsonObject);
            assertNotNull(issue);
        }
    }
}