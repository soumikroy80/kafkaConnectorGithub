package com.msqube.confluent;

import static com.msqube.confluent.GitHubSchemas.*;

import java.time.Instant;
import java.util.Date;

import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaBuilder;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.data.Timestamp;

import com.msqube.confluent.model.Commit;
import com.msqube.confluent.model.Issue;
import com.msqube.confluent.model.PullRequest;
import com.msqube.confluent.model.User;

public class BuildRecord {
	
	 GitHubSourceConnectorConfig config;

	 
	public BuildRecord(GitHubSourceConnectorConfig config) {
		this.config = config;
	}
	
	 //Build Issue Record Value
    public Struct buildRecordValue(Issue issue){
        Struct valueStruct = new Struct(SCHEMA_ISSUE)
                .put(TYPE_FIELD, "ISSUE")
                .put(ISSUE_ID_FIELD, issue.getId())
                .put(REPO_ID_FIELD, issue.getRepoId())
                .put(TITLE_FIELD, issue.getTitle())
                .put(STATUS_FIELD, issue.getState())
                .put(DESC_FIELD, issue.getBody())
                .put(CREATED_AT_FIELD, Date.from(issue.getCreatedAt()))
                .put(UPDATED_AT_FIELD, Date.from(issue.getUpdatedAt()));
       if(issue.getAssigneeId() != null) { 
    	   valueStruct.put(ASSIGNEE_ID_FIELD, issue.getAssigneeId());
       }
        return valueStruct;
    }
    
    
    //build committer record value
    public Object buildRecordValue(Commit commit) {
    	Struct valueStruct = new Struct(SCHEMA_COMMIT)
    			.put(TYPE_FIELD, "COMMIT")
    			.put(COMMIT_TIME_FIELD, Date.from(commit.getCommittedAt()))
    			.put(USER_ID_FIELD, commit.getUserId())
    	        .put(REPO_ID_FIELD, commit.getRepoId())
    	        .put(USER_EMAIL_FIELD, commit.getCommitterEmail())
    	        .put(COMMIT_MESSAGE_FIELD, commit.getCommitMessage());

    	return valueStruct;
    }

    public Object buildRecordValue(PullRequest pull) {

    	Struct valueStruct = new Struct(SCHEMA_PULLREQUEST)
    			.put(TYPE_FIELD, "PULL")
    			.put(PULL_ID_FIELD, pull.getPullId())
    			.put(REPO_ID_FIELD, pull.getRepoId())
    			.put(USER_ID_FIELD, pull.getSubmitterId())
    			.put(TITLE_FIELD, pull.getTitle())
    			.put(BODY_FIELD, pull.getBody())
    			.put(STATE_FIELD, pull.getState())
    			.put(CREATED_AT_FIELD, Date.from(pull.getCreatedAt()))
    			.put(UPDATED_AT_FIELD, Date.from(pull.getUpdatedAt()));

    	if(pull.getClosedAt() != null)
    		valueStruct.put(CLOSED_AT_FIELD, Date.from(pull.getClosedAt()));
    	if(pull.getMergedAt() != null)
    		valueStruct.put(MERGED_AT_FIELD, Date.from(pull.getMergedAt()) );

    	return valueStruct;
    }


}
