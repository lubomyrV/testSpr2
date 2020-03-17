package com.example.demo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
 
@Entity
@Table(name = "user_role", //
        uniqueConstraints = { //
                @UniqueConstraint(name = "uc_user_role", columnNames = { "user_id", "role_id" }) })
public class UserRole {
 
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;
 
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;
 
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;
 
    public Long getId() {
        return id;
    }
 
    public void setId(Long id) {
        this.id = id;
    }

	public AppUser getUser() {
		return user;
	}

	public void setUser(AppUser user) {
		this.user = user;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "UserRole [id=" + id + ", user=" + user + ", role=" + role + "]";
	}
	
}
