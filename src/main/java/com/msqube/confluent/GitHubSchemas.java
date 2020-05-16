package com.msqube.confluent;

import java.time.Instant;

import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaBuilder;
import org.apache.kafka.connect.data.Timestamp;

public class GitHubSchemas {

    public static final String NEXT_PAGE_FIELD = "next_page";
    public static final String ID_FIELD = "id";

    // Issue fields
    public static final String OWNER_FIELD = "owner";
    public static final String REPOSITORY_FIELD = "repository";
    public static final String CREATED_AT_FIELD = "created_at";
    public static final String UPDATED_AT_FIELD = "updated_at";
    public static final String NUMBER_FIELD = "number";
    public static final String URL_FIELD = "url";
    public static final String HTML_URL_FIELD = "html_url";
    public static final String TITLE_FIELD = "title";
    public static final String STATE_FIELD = "state";
    public static final String STATUS_FIELD = "status";

    public static final String BODY_FIELD = "body";
    public static final String ISSUE_ID_FIELD = "issue_id";
    public static final String TYPE_FIELD = "node_type";

    // SCM User fields
    public static final String USER_FIELD = "user";
    public static final String USER_URL_FIELD = "url";
    public static final String USER_HTML_URL_FIELD = "html_url";
    public static final String USER_ID_FIELD = "user_id";
    public static final String USER_LOGIN_FIELD = "login";
    public static final String EMAIL_FIELD = "email";
    public static final String USER_EMAIL_FIELD = "user_email";
    public static final String USER_COMPANY_FIELD = "company";
    public static final String USER_LOCATION_FIELD = "location";
    
    //Commit fields
    public static final String COMMIT_FIELD = "commit";
    public static final String AUTHOR_FIELD = "author";
    public static final String COMMIT_MESSAGE_FIELD = "message";
    public static final String DATE_FIELD = "date";
    public static final String NAME_FIELD = "name";
    public static final String COMMIT_TIME_FIELD = "commitTime";
    public static final String DESC_FIELD = "description";
    public static final String COMMIT_ID_FIELD = "commit_id";
    
    

    // PR fields
    public static final String CLOSED_AT_FIELD = "closed_at";
    public static final String MERGED_AT_FIELD = "merged_at";
    public static final String PULL_ID_FIELD = "pullRequest_id";
    public static final String ASSIGNEE_ID_FIELD = "assignee_id";
    public static final String ASSIGNEE_FIELD = "assignee";
    
    public static final String OWNER_ID_FIELD = "owner_id";
    public static final String REPO_ID_FIELD = "repo_id";
    
    
    // Schema names
    public static final String SCHEMA_KEY = "com.msqube.confluent.IssueKey";
    public static final String SCHEMA_VALUE_ISSUE = "com.msqube.confluent.IssueValue";
    public static final String SCHEMA_VALUE_USER = "com.msqube.confluent.UserValue";
    public static final String SCHEMA_VALUE_COMMIT = "com.msqube.confluent.CommitValue";
    public static final String SCHEMA_VALUE_PR = "com.msqube.confluent.PrValue";

    
    //Issue Schema for pulse
    public static final Schema SCHEMA_ISSUE = SchemaBuilder.struct().name(SCHEMA_VALUE_ISSUE)
            .version(1)
            .field(TYPE_FIELD, Schema.STRING_SCHEMA)
            .field(ISSUE_ID_FIELD, Schema.INT32_SCHEMA)
            .field(REPO_ID_FIELD, Schema.INT32_SCHEMA)
            .field(ASSIGNEE_ID_FIELD, Schema.OPTIONAL_INT32_SCHEMA) // mandatory
            .field(TITLE_FIELD, Schema.STRING_SCHEMA)
            .field(STATUS_FIELD, Schema.STRING_SCHEMA)
            .field(DESC_FIELD, Schema.STRING_SCHEMA)
            .field(CREATED_AT_FIELD, Timestamp.SCHEMA)
            .field(UPDATED_AT_FIELD, Timestamp.SCHEMA)
            .build();
    
 // Commit Schema
    public static final Schema SCHEMA_COMMIT = SchemaBuilder.struct().name(SCHEMA_VALUE_COMMIT)
            .version(1)
            .field(TYPE_FIELD, Schema.STRING_SCHEMA)
//            .field(COMMIT_ID_FIELD, Schema.INT32_SCHEMA)
            .field(REPO_ID_FIELD, Schema.INT32_SCHEMA) // mandatory
            .field(USER_ID_FIELD, Schema.INT32_SCHEMA)
            .field(USER_EMAIL_FIELD, Schema.OPTIONAL_STRING_SCHEMA)
            .field(COMMIT_MESSAGE_FIELD, Schema.OPTIONAL_STRING_SCHEMA)
            .field(COMMIT_TIME_FIELD, Timestamp.SCHEMA)
            .build();
    
    //Pull Request Schema
//    final static Schema OptionalTimeSchema = Timestamp.builder().optional().build();
    public static final Schema SCHEMA_PULLREQUEST = SchemaBuilder.struct().name(SCHEMA_VALUE_PR)
            .version(1)
            .field(TYPE_FIELD, Schema.STRING_SCHEMA)
            .field(PULL_ID_FIELD, Schema.INT32_SCHEMA)
            .field(USER_ID_FIELD, Schema.INT32_SCHEMA) // mandatory
            .field(REPO_ID_FIELD, Schema.INT32_SCHEMA) // mandatory
            .field(TITLE_FIELD, Schema.OPTIONAL_STRING_SCHEMA)
            .field(BODY_FIELD, Schema.OPTIONAL_STRING_SCHEMA)
            .field(STATE_FIELD, Schema.OPTIONAL_STRING_SCHEMA)
            .field(UPDATED_AT_FIELD, Timestamp.SCHEMA)
            .field(CREATED_AT_FIELD, Timestamp.SCHEMA)
            .field(CLOSED_AT_FIELD, Timestamp.SCHEMA)
            .field(MERGED_AT_FIELD, Timestamp.SCHEMA)
            .field(OWNER_FIELD, Schema.STRING_SCHEMA)
            .build();
    
    
}
