package com.example.demo.dao;

import java.util.HashMap;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class UserRoleDao {

	@Autowired
    private EntityManager entityManager;

	public boolean insertRolesForUser(HashMap<Integer, Integer> forInsertMap) {
		//Query query = entityManager.createNativeQuery("INSERT INTO user_role (user_id, role_id) VALUES (?, ?)");  
        //query.setParameter(1, forInsertMap.keySet().);
        //query.setParameter(2, encrytedPassword);
        int relust = 0;
        try {
        	//relust = query.executeUpdate();
		} catch (Exception e) {
			System.out.println("Exception UserRoleDao.insertRolesForUser(): "+e);
		}
        return relust > 0;
	}
	
}
