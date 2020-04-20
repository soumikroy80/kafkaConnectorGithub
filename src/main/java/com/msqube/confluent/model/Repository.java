package com.msqube.confluent.model;

import java.time.Instant;
import org.json.JSONObject;

public class Repository {
	private String repoName;
	private String repoOwner;
	private String repoDescription;
	private long createdAt;
	private String node_type = "REPO";
	
	
	

	public String getNode_type() {
		return node_type;
	}

	public void setNode_type(String node_type) {
		this.node_type = node_type;
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

	public String getRepoOwner() {
		return repoOwner;
	}

	public void setRepoOwner(String repoOwner) {
		this.repoOwner = repoOwner;
	}
	
	

	public long getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(long createdAt) {
		this.createdAt = createdAt;
	}

	public static Repository formJson(JSONObject jsonObject) {
		Repository repo = new Repository();
		long crtd =Instant.parse(jsonObject.getString("created_at")).getEpochSecond();
		repo.setCreatedAt(crtd*1000);
		if (!jsonObject.isNull("description"))
			repo.setRepoDescription(jsonObject.getString("description"));
		repo.setRepoName(jsonObject.getString("name"));
		return repo;

	}

}
