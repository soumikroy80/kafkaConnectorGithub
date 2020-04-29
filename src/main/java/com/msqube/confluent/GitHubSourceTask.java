package com.msqube.confluent;

import com.mashape.unirest.http.JsonNode;
import com.msqube.confluent.model.Commit;
import com.msqube.confluent.model.Issue;
import com.msqube.confluent.model.PullRequest;
import com.msqube.confluent.model.Repository;
import com.msqube.confluent.model.User;
import com.msqube.confluent.utils.DateUtils;

import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.source.SourceTask;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.msqube.confluent.GitHubSchemas.*;

import java.time.Instant;
import java.util.*;


public class GitHubSourceTask extends SourceTask {
    private static final Logger log = LoggerFactory.getLogger(GitHubSourceTask.class);
    public GitHubSourceConnectorConfig config;

    protected Instant nextQuerySince;
    public static List<Repository> repos=new ArrayList<Repository>();

    GitHubAPIHttpClient gitHubHttpAPIClient;
    BuildRecord buildRecord;

    @Override
    public String version() {
        return VersionUtil.getVersion();
    }

    @Override
    public void start(Map<String, String> map) {
        //Do things here that are required to start your task. This could be open a connection to a database, etc.
        config = new GitHubSourceConnectorConfig(map);
        gitHubHttpAPIClient = new GitHubAPIHttpClient(config);
        initializeLastVariables();
        buildRecord = new BuildRecord(config);
        
    }

    private void initializeLastVariables(){
    	populateRepos();
    	
        Map<String, Object> lastSourceOffset = null;
        lastSourceOffset = context.offsetStorageReader().offset(sourcePartition());
        if( lastSourceOffset == null){
            // we haven't fetched anything yet, so we initialize to 7 days ago
            nextQuerySince = config.getSince();
        } else {
            Object updatedAt = lastSourceOffset.get(UPDATED_AT_FIELD);
            if(updatedAt != null && (updatedAt instanceof String)){
                nextQuerySince = Instant.parse((String) updatedAt);
            }
        }
    }



    @Override
    public List<SourceRecord> poll() throws InterruptedException {

    	gitHubHttpAPIClient.sleepIfNeed();

    	// fetch data
    	// we'll count how many results we get with i
    	int i = 0;
    	final ArrayList<SourceRecord> records = new ArrayList<>();
    	String requestUrl="";
    	JsonNode jsonResponse=null;
    	
    	
    	for(Repository repo: repos) {
    		// "ISSUE":
    		requestUrl= String.format(
    				"https://api.github.com/repos/%s/%s/issues?since=%s&state=all&direction=asc&sort=updated",
    				repo.getRepoOwner(),
    				repo.getRepoName(),
    				nextQuerySince.toString());
    		jsonResponse = gitHubHttpAPIClient.getNextItems(requestUrl,config.getAuthTokenConfig());
    		for (Object obj : jsonResponse.getArray()) {
    			Issue issue = Issue.fromJson((JSONObject) obj);
    			issue.setOwner(repo.getRepoOwner());
    			issue.setRepo(repo.getRepoName());
    			SourceRecord sourceRecord = new SourceRecord(
    					sourcePartition(),
    					sourceOffset(),
    					config.getTopic(),
    					null, // partition will be inferred by the framework
    					SCHEMA_ISSUE,
    					buildRecord.buildRecordValue(issue)
    					);
    			records.add(sourceRecord);
    			i += 1;
    		}
    		// "COMMIT" Changes 1:
    		requestUrl= String.format(
    				"https://api.github.com/repos/%s/%s/commits?since=%s",
    				repo.getRepoOwner(),
    				repo.getRepoName(),
    				nextQuerySince.toString());
    		jsonResponse = gitHubHttpAPIClient.getNextItems(requestUrl,config.getAuthTokenConfig());
    		for (Object obj : jsonResponse.getArray()) {
    			Commit commit = Commit.fromJson((JSONObject) obj);
    			commit.setOwner(repo.getRepoOwner());
    			commit.setRepo(repo.getRepoName());
    			SourceRecord sourceRecord = new SourceRecord(
    					sourcePartition(),
    					sourceOffset(),
    					config.getTopic(),
    					null, // partition will be inferred by the framework
    					SCHEMA_COMMIT,
    					buildRecord.buildRecordValue(commit));
    			records.add(sourceRecord);
    			i += 1;
    		}

    	}

    	if (i > 0) {
    		log.info(String.format("Fetched %s record(s)", i));
    		nextQuerySince = Instant.now().plusSeconds(1);
    		gitHubHttpAPIClient.sleep();
    	}
    	return records;
    
    }

    @Override
    public void stop() {
        // Do whatever is required to stop your task.
    }

    private Map<String, String> sourcePartition() {
        Map<String, String> map = new HashMap<>();
        map.put(TYPE_FIELD, "GITHUB");
        return map;
    }

    private Map<String, String> sourceOffset() {
        Map<String, String> map = new HashMap<>();
        map.put(UPDATED_AT_FIELD, Instant.now().toString());
        return map;
    }
    
    public void populateRepos() {
    	try {
    			String requestUrl= String.format("https://api.github.com/orgs/%s/repos",config.getOwnerConfig());
    			JsonNode jsonResponse;
    			jsonResponse = gitHubHttpAPIClient.getNextItems(requestUrl,config.getAuthTokenConfig());
    			for (Object obj : jsonResponse.getArray()) {
    			Repository repo=Repository.formJson((JSONObject) obj);
    			repo.setRepoOwner(config.getOwnerConfig());
    			repos.add(repo);
    			}

    	} catch (InterruptedException e) {
    		e.printStackTrace();
    		log.error(e.getMessage());
    	}
    	
    }




}