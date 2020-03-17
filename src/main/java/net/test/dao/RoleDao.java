package net.test.dao;

import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import net.test.entity.Role;
import net.test.entity.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
 
@Repository
@Transactional
public class RoleDao {
 
    @Autowired
    private EntityManager entityManager;
 
    @SuppressWarnings("unchecked")
	public List<String> getRoleNames(int userId) {
        String sql = "SELECT ur.role.name FROM " + UserRole.class.getName() + " ur " //
                + " WHERE ur.user.id = :userId ";
 
        Query query = this.entityManager.createQuery(sql, String.class);
        query.setParameter("userId", userId);
        return (List<String>)query.getResultList();
    }
    
    @SuppressWarnings("unchecked")
	public List<String> getAllRoleNames() {
        String sql = "SELECT r.name FROM "+ Role.class.getName() + " r ";
        Query query = this.entityManager.createQuery(sql, String.class);
        return (List<String>)query.getResultList();
    }

	@SuppressWarnings("unchecked")
	public List<Role> getRoles(Set<String> keySet) {
		String sql = "SELECT r FROM "+ Role.class.getName() + " r ";
        Query query = this.entityManager.createQuery(sql, Role.class);
        return (List<Role>)query.getResultList();
	}
 
}
