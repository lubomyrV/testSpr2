package net.test.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import net.test.dao.RoleDao;

@Service
public class RoleService {

	@Autowired
    private RoleDao roleDao;
	
	public List<String> getAllRoleNames() {
		return roleDao.getAllRoleNames();
	}
	
}
