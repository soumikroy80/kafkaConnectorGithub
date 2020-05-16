package com.msqube.confluent.model;

import static com.msqube.confluent.GitHubSchemas.*;

import java.time.Instant;

import org.json.JSONObject;

import com.msqube.confluent.utils.DateUtils;

public class PullRequest {

	private int pullId;
	private Integer repoId;
	private String title;
	private String state;
	private String body;
	private Integer submitterId;
	private Instant createdAt;
	private Instant updatedAt;
	private Instant closedAt = Instant.parse("1900-01-01T00:00:00Z");
	private Instant mergedAt = Instant.parse("1900-01-01T00:00:00Z");

	public int getPullId() {
		return pullId;
	}

	public void setPullId(int pullId) {
		this.pullId = pullId;
	}

	public Integer getRepoId() {
		return repoId;
	}

	public void setRepoId(Integer repoId) {
		this.repoId = repoId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Integer getSubmitterId() {
		return submitterId;
	}

	public void setSubmitterId(Integer submitterId) {
		this.submitterId = submitterId;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	public Instant getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Instant updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Instant getClosedAt() {
		return closedAt;
	}

	public void setClosedAt(Instant closedAt) {
		this.closedAt = closedAt;
	}

	public Instant getMergedAt() {
		return mergedAt;
	}

	public void setMergedAt(Instant mergedAt) {
		this.mergedAt = mergedAt;
	}

	public static PullRequest fromJson(JSONObject jsonObject, Instant nextQuerySince) {
		PullRequest pull = new PullRequest();
		Instant updatedField = Instant.parse(jsonObject.getString(UPDATED_AT_FIELD));
		if (updatedField.compareTo(nextQuerySince) > 0) {
			pull.setPullId(jsonObject.getInt(ID_FIELD));
			pull.setUpdatedAt(Instant.parse(jsonObject.getString(UPDATED_AT_FIELD)));
			pull.setCreatedAt(Instant.parse(jsonObject.getString(CREATED_AT_FIELD)));
			pull.setState(jsonObject.getString(STATE_FIELD));
			pull.setBody(jsonObject.getString(BODY_FIELD));
			pull.setTitle(jsonObject.getString(TITLE_FIELD));
			pull.setSubmitterId(jsonObject.getJSONObject(USER_FIELD).getInt(ID_FIELD));

			if (!jsonObject.isNull(CLOSED_AT_FIELD)) {
				pull.setClosedAt(Instant.parse(jsonObject.getString(CLOSED_AT_FIELD)));
			}
			if (!jsonObject.isNull(MERGED_AT_FIELD)) {
				pull.setMergedAt(Instant.parse(jsonObject.getString(MERGED_AT_FIELD)));
			}
		}
		return pull;

	}

}
