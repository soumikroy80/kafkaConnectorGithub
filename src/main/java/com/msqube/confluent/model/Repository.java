package com.msqube.confluent.model;

import static com.msqube.confluent.GitHubSchemas.*;

import java.time.Instant;

import org.json.JSONObject;

public class Repository {
	
	private Integer repoId;
	private String repoName;
	private String repoOwner;
	private String repoDescription;
	private Instant createdAt;

	
	
	public Integer getRepoId() {
		return repoId;
	}

	public void setRepoId(Integer repoId) {
		this.repoId = repoId;
	}

	public String getRepoName() {
		return repoName;
	}

	public void setRepoName(String repoName) {
		this.repoName = repoName;
	}

	public String getRepoDescription() {
		return repoDescription;
	}

	public void setRepoDescription(String repoDescription) {
		this.repoDescription = repoDescription;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createddAt) {
		this.createdAt = createddAt;
	}

	public String getRepoOwner() {
		return repoOwner;
	}

	public void setRepoOwner(String repoOwner) {
		this.repoOwner = repoOwner;
	}

	public static Repository formJson(JSONObject jsonObject) {
		Repository repo = new Repository();
		repo.setRepoId(jsonObject.getInt(ID_FIELD));
		repo.setRepoName(jsonObject.getString(NAME_FIELD));
		repo.setCreatedAt(Instant.parse(jsonObject.getString(CREATED_AT_FIELD)));
		
		if (!jsonObject.isNull(DESC_FIELD))
			repo.setRepoDescription(jsonObject.getString(DESC_FIELD));
		
		return repo;

	}

}
