package net.test.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
 
@Entity
@Table(name = "role", //
        uniqueConstraints = { //
                @UniqueConstraint(name = "uc_role", columnNames = "name") })
public class Role {
     
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private int id;
 
    @Column(name = "name", length = 30, nullable = false)
    private String name;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Role [id=" + id + ", name=" + name + "]";
	}
     
}