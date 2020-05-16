package com.msqube.confluent;

import org.junit.Test;

import com.msqube.confluent.GitHubSourceConnector;

import java.util.HashMap;
import java.util.Map;

import static com.msqube.confluent.GitHubSourceConnectorConfig.*;
import static org.junit.Assert.*;

public class GitHubSourceConnectorTest {

  private Map<String, String> initialConfig() {
    Map<String, String> baseProps = new HashMap<>();
    baseProps.put(ORGANIZATION_CONFIG, "foo");
    baseProps.put(SINCE_CONFIG, "2017-04-26T01:23:45Z");
    baseProps.put(BATCH_SIZE_CONFIG, "100");
    baseProps.put(TOPIC_CONFIG, "github-issues");
    baseProps.put(OWNER_TOKEN_CONFIG, "token----keys");
    return (baseProps);
  }

  @Test
  public void taskConfigsShouldReturnOneTaskConfig() {
      GitHubSourceConnector gitHubSourceConnector = new GitHubSourceConnector();
      gitHubSourceConnector.start(initialConfig());
      assertEquals(gitHubSourceConnector.taskConfigs(1).size(),1);
      assertEquals(gitHubSourceConnector.taskConfigs(10).size(),1);
  }
}
