package com.project.service;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.project.beans.User;
import com.project.dao.UserDao;
import com.project.exceptions.InvalidCredentialsException;
import com.project.exceptions.UsernameAlreadyExistsException;
import com.project.utils.SessionCache;

/**
* This class should contain the business logic for performing operations on users
*/

public class UserService {
	public final static Logger logger = Logger.getLogger(UserService.class);
	UserDao userDao;
	
	public UserService(UserDao udao) {
		this.userDao = udao;
		logger.setLevel(Level.ALL);
	}
	
	/**
	 * Validates the username and password, and return the User object for that user
	 * @throws InvalidCredentialsException if either username is not found or password does not match
	 * @return the User who is now logged in
	 */
	public User login(String username, String password) {
		User user = new User();
		if((username != null && password != null) && !(userDao.getUser(username, password) == null)) {
			user = userDao.getUser(username, password);
			SessionCache.setCurrentUser(user);
			logger.info("User loged in as: " + username);
			return user;
		} else {
			try {
				logger.warn("Login failed!");
				throw new InvalidCredentialsException();
			} catch(InvalidCredentialsException e) {
				e.printStackTrace();
				System.out.println("Username or password is incorrect!");
			}
			SessionCache.setCurrentUser(null);
			return null;
		}
	}
	
	/**
	 * Creates the specified User in the persistence layer
	 * @param newUser the User to register
	 * @throws UsernameAlreadyExistsException if the given User's username is taken
	 */
	public void register(User newUser) {
		if((newUser.getUsername() != null && newUser.getPassword() != null) && userDao.getUser(newUser.getUsername(), newUser.getPassword()) == null) {
			userDao.addUser(newUser);
			logger.info("New user registered as: " + newUser.getFirstName());
			System.out.println("User Registered!");
		} else {
			try {
				logger.warn("Registration failed!");
				throw new UsernameAlreadyExistsException();
			} catch(UsernameAlreadyExistsException e) {
				e.printStackTrace();
				System.out.println("Username or password is incorrect!");
			}
		}
	}
	
}
