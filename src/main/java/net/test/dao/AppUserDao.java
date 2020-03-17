package net.test.dao;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import net.test.entity.AppUser;
import net.test.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
 
@Repository
@Transactional
public class AppUserDao {
 
    @Autowired
    private EntityManager entityManager;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
 
    public AppUser findUserAccount(String username) {
        try {
            String sql = "SELECT e FROM " + AppUser.class.getName() + " e " //
                    + " WHERE e.username = :username AND e.enabled = 1";
 
            Query query = entityManager.createQuery(sql, AppUser.class);
            query.setParameter("username", username);
 
            return (AppUser) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    
    public AppUser getUserById(String userId) {
        try {
        	int id = Integer.parseInt(userId);
            String sql = "SELECT e FROM " + AppUser.class.getName() + " e  WHERE e.id = :userId";
            Query query = entityManager.createQuery(sql, AppUser.class);
            query.setParameter("userId", id);
            return (AppUser) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
 
    public int createUser(String username, String password) {
        String encrytedPassword = this.passwordEncoder.encode(password);
        Query query = entityManager.createNativeQuery("INSERT INTO user (username, encryted_password) VALUES (?, ?)");  
        query.setParameter(1, username);
        query.setParameter(2, encrytedPassword);
        int relust = 0;
        try {
        	relust = query.executeUpdate();
		} catch (Exception e) {
			System.out.println("Exception AppUserDao.createUser(): "+e);
		}
        return relust;
    }

	public int getUserId(String username) {
		Query query = entityManager.createNativeQuery("SELECT * FROM user WHERE username = ?", AppUser.class);
		query.setParameter(1, username);
		AppUser user = null;
		try {
			user = (AppUser) query.getSingleResult();
			System.out.println("user="+user);
		} catch (NoResultException nre){
			//logic is ok!
		}

		return (user != null ? 1 : 0);
	}

	public List<AppUser> getAllUsers() {
		Query query = entityManager.createNativeQuery("SELECT * FROM user", AppUser.class);
		@SuppressWarnings("unchecked")
		List<AppUser> resultList = (List<AppUser>) query.getResultList();
		System.out.println(resultList);
		return resultList;
	}
	
	public List<Object> getUserWithRoles(String userId) {
    	int id = Integer.parseInt(userId);

		Query query = entityManager.createNativeQuery(
				"SELECT user.id, user.username, user.email, user.enabled, user_role.role_id, role.name \n" + 
				"FROM user \n" + 
				"    JOIN user_role ON user.id = user_role.user_id \n" + 
				"        JOIN role ON user_role.role_id = role.id\n" + 
				"WHERE user.id = :userId");
		query.setParameter("userId", id);
		@SuppressWarnings("unchecked")
		List<Object[]> results = query.getResultList();
		
		List<Object> responseList = new ArrayList<Object>();
		int i = 0;
		for (Object[] result : results) {
			//System.out.println("id="+result[0] + "; username=" + result[1]+ "; email=" + result[2]+ "; enabled=" + result[3]
					//+ "; role_id=" + result[4]+ "; role_name=" + result[5]);
			if(i == 0) {
				AppUser user = new AppUser();
				user.setId((int) result[0]);
				user.setUsername((String) result[1]);
				user.setEmail((String) result[2]);
				user.setEnabled((boolean) result[3]);
				responseList.add(user);
				
				
				Role role = new Role();
				role.setId((int) result[4]);
				role.setName((String) result[5]);
				responseList.add(role);	
				
			} else {
				Role role = new Role();
				role.setId((int) result[4]);
				role.setName((String) result[5]);
				responseList.add(role);
			}

			i++;
		}
		System.out.println("responseList="+responseList);
		return responseList;
	}

	public List<AppUser> getUsersByUsernameOrEmail(String nameOrEmail) {
		//System.out.println("nameOrEmail: "+nameOrEmail);
		String sql = "SELECT e FROM " + AppUser.class.getName() + " e "
					+ " WHERE e.username LIKE :username OR e.email LIKE :username";
        Query query = entityManager.createQuery(sql, AppUser.class);
        query.setParameter("username", "%" + nameOrEmail + "%");
		
		@SuppressWarnings("unchecked")
		List<AppUser> resultList = (List<AppUser>) query.getResultList();

		//System.out.println("getUsersByUsernameOrEmail: "+resultList);
		return resultList;
	}

	public boolean updateEnabled(int id, boolean enabled) {
		Query query = entityManager.createNativeQuery(
			"UPDATE user " + 
			"SET enabled = ?1 " + 
			"WHERE id = ?2 "
		);  
        query.setParameter(1, enabled);
        query.setParameter(2, id);
        int relust = 0;
        try {
        	relust = query.executeUpdate();
		} catch (Exception e) {
			System.out.println(e);
		}
        //System.out.println("update "+relust);
        return relust > 0;
	}
}