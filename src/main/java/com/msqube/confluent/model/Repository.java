package com.msqube.confluent.model;

import java.time.Instant;
import org.json.JSONObject;

public class Repository {
	private String owner;
	private String repository;
	private String message;
	private long created_at;
	private String node_type = "REPO";
	
	
	

	public String getNode_type() {
		return node_type;
	}

	public void setNode_type(String node_type) {
		this.node_type = node_type;
	}


	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getRepository() {
		return repository;
	}

	public void setRepository(String repository) {
		this.repository = repository;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public long getCreated_at() {
		return created_at;
	}

	public void setCreated_at(long created_at) {
		this.created_at = created_at;
	}

	public static Repository formJson(JSONObject jsonObject) {
		Repository repo = new Repository();
		long crtd =Instant.parse(jsonObject.getString("created_at")).getEpochSecond();
		repo.setCreated_at(crtd*1000);
		if (!jsonObject.isNull("description"))
			repo.setMessage(jsonObject.getString("description"));
		repo.setRepository(jsonObject.getString("name"));
		return repo;

	}

}
