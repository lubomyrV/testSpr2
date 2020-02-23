package net.test.dao;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import net.test.entity.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
 
@Repository
@Transactional
public class AppUserDAO {
 
    @Autowired
    private EntityManager entityManager;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
 
    public AppUser findUserAccount(String userName) {
        try {
            String sql = "SELECT e FROM " + AppUser.class.getName() + " e " //
                    + " WHERE e.userName = :userName AND e.enabled = 1";
 
            Query query = entityManager.createQuery(sql, AppUser.class);
            query.setParameter("userName", userName);
 
            return (AppUser) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
 
    public int createUser(String username, String password) {
        String encrytedPassword = this.passwordEncoder.encode(password);
        Query query = entityManager.createNativeQuery("INSERT INTO user (user_name, encryted_password) VALUES (?, ?)");  
        query.setParameter(1, username);
        query.setParameter(2, encrytedPassword);
        int relust = 0;
        try {
        	relust = query.executeUpdate();
		} catch (Exception e) {
			// TODO: handle exception
		}
        return relust;
    }
}