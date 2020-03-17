package com.example.demo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
 
@Entity
@Table(name = "user", //
        uniqueConstraints = { //
                @UniqueConstraint(name = "uc_user", columnNames = "username") })
public class AppUser {
 
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private int id;
 
    @Column(name = "username", length = 64, nullable = false)
    private String username;
    
    @Column(name = "email", length = 64)
    private String email;
 
    @Column(name = "encryted_password", length = 128, nullable = false)
    private String encrytedPassword;
 
    @Column(name = "enabled", length = 1, nullable = false)
    private boolean enabled;
    
 
    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
    public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEncrytedPassword() {
        return encrytedPassword;
    }
 
    public void setEncrytedPassword(String encrytedPassword) {
        this.encrytedPassword = encrytedPassword;
    }
 
    public boolean isEnabled() {
        return enabled;
    }
 
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

	@Override
	public String toString() {
		return "AppUser [id=" + id + ", username=" + username + ", email=" + email + ", enabled=" + enabled + "]";
	}
    
}
