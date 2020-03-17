package com.example.demo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.dao.AppUserDao;
import com.example.demo.dao.RoleDao;
import com.example.demo.dao.UserRoleDao;
import com.example.demo.entity.AppUser;
import com.example.demo.entity.Role;
import com.example.demo.entity.UserWrapper;
 
@Service
public class UserService implements UserDetailsService {
 
    @Autowired private AppUserDao userDao;
    @Autowired private RoleDao roleDao;
    @Autowired private UserRoleDao userRoleDao;
 
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = this.userDao.findUserAccount(username);
 
        if (appUser == null) {
            System.out.println("User not found! " + username);
            throw new UsernameNotFoundException("User " + username + " was not found in the database");
        }
 
        System.out.println("Found User: " + appUser);
 
        // [ROLE_USER, ROLE_ADMIN,..]
        List<String> roleNames = this.roleDao.getRoleNames(appUser.getId());
 
        List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
        if (roleNames != null) {
            for (String role : roleNames) {
                // ROLE_USER, ROLE_ADMIN,..
                GrantedAuthority authority = new SimpleGrantedAuthority(role);
                grantList.add(authority);
            }
        }
 
        UserDetails userDetails = (UserDetails) new User(appUser.getUsername(), //
                appUser.getEncrytedPassword(), grantList);
 
        return userDetails;
    }
    
    public boolean createUser(String username, String password) {
    	int result = userDao.createUser(username, password);
    	return (result > 0);
    }

	public boolean alreadyExists(String username) {
		int result = userDao.getUserId(username);
		System.out.println("alreadyExists="+result);
    	return (result > 0);
	}

	public List<AppUser> getAllUsers() {
		return userDao.getAllUsers();
	}

	public List<AppUser> findUsersByUserameOrEmail(String usernameOrEmail) {
		if(usernameOrEmail.isEmpty()) {
			return null;
		}
		return userDao.getUsersByUsernameOrEmail(usernameOrEmail);
	}

	public boolean updateEnabled(AppUser user) {
		return userDao.updateEnabled(user.getId(), user.isEnabled());
	}

	public AppUser getUserById(String userId) {
		return userDao.getUserById(userId);
	}

	public List<Object> getUserWithRoles(String userId) {
		return userDao.getUserWithRoles(userId);
	}

	public boolean updateUser(UserWrapper userWrap) {
		int userId = userWrap.getId();
		boolean isEnabled = userWrap.isEnabled();
		HashMap<String, Boolean> roleMap = userWrap.getRoleMap();
		
		boolean updateEnableResult = userDao.updateEnabled(userId, isEnabled);
		if(updateEnableResult) {
			List<Role> roles = roleDao.getRoles(roleMap.keySet());
			System.out.println("updateEnableResult roles="+roles);
			int roleIdForInsert;
			int roleIdForDelete;
			for (Role role : roles) {
				if(roleMap.containsKey(role.getName())) {
					if(roleMap.get(role.getName())) {
						//insert
						roleIdForInsert = role.getId();
					} else {
						//delete
						roleIdForDelete = role.getId();
					}
				}
			}
			//userRoleDao.insertRolesForUser(forInsertMap);
		}
		return false;
	}
}
