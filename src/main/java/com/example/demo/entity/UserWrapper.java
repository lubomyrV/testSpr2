package com.example.demo.entity;

import java.util.HashMap;

public class UserWrapper {
	private int id;
	private boolean enabled;
	private String email;
	private HashMap<String, Boolean> roleMap;
	
	public UserWrapper() {}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public HashMap<String, Boolean> getRoleMap() {
		return roleMap;
	}
	public void setRoleMap(HashMap<String, Boolean> roleMap) {
		this.roleMap = roleMap;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "UserWrapper [id=" + id + ", enabled=" + enabled + ", email=" + email + ", roleMap=" + roleMap + "]";
	}
	
	
	
}
