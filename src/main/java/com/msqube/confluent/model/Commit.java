package com.msqube.confluent.model;

import static com.msqube.confluent.GitHubSchemas.*;

import java.time.Instant;

import org.json.JSONObject;

public class Commit {
	private Integer commitId;
	private String committerEmail;
	private String commitMessage;
	private Instant committedAt;
	private Integer userId = 11111111;
	private Integer repoId;

	public Integer getCommitId() {
		return commitId;
	}

	public void setCommitId(Integer commitId) {
		this.commitId = commitId;
	}

	public String getCommitterEmail() {
		return committerEmail;
	}

	public void setCommitterEmail(String committerEmail) {
		this.committerEmail = committerEmail;
	}

	public String getCommitMessage() {
		return commitMessage;
	}

	public void setCommitMessage(String commitMessage) {
		this.commitMessage = commitMessage;
	}

	public Instant getCommittedAt() {
		return committedAt;
	}

	public void setCommittedAt(Instant committedAt) {
		this.committedAt = committedAt;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getRepoId() {
		return repoId;
	}

	public void setRepoId(Integer repoId) {
		this.repoId = repoId;
	}

	public static Commit fromJson(JSONObject commitBody) {
		Commit commit = new Commit();
		if(!commitBody.isNull(AUTHOR_FIELD)) {
			commit.setUserId(commitBody.getJSONObject(AUTHOR_FIELD).getInt(ID_FIELD));
		}
		JSONObject jsonObject = commitBody.getJSONObject(COMMIT_FIELD);
		commit.setCommitMessage(jsonObject.getString(COMMIT_MESSAGE_FIELD));

		jsonObject = jsonObject.getJSONObject(AUTHOR_FIELD);
		commit.setCommittedAt(Instant.parse(jsonObject.getString(DATE_FIELD)));
		commit.setCommitterEmail(jsonObject.getString(EMAIL_FIELD));

		return commit;

	}

}
