package com.project.dao;

import java.util.List;

import com.project.beans.User;

/**
 * The data access object interface for CRUD operations on Users.
 * This allows us to define a contract which can be implemented in different ways
 * using different methods of data storage and retrieval.
 */

public interface UserDao {
	
	/**
	 * Add a new user
	 * @param user the user to insert
	 * @return the newly added user object
	 */
	public User addUser(User user);
	
	/**
	 * Get a user by id
	 * @param userId the id to search by
	 * @return the user object
	 */
	public User getUser(Integer userId);
	
	/**
	 *  Get a user by username and password
	 * @param username
	 * @param pass
	 * @return the user object
	 */
	public User getUser(String username, String pass);
	
	/**
	 * Get all users in the persistence layer
	 * @return a list of all users
	 */
	public List<User> getAllUsers();
	
	/**
	 * Updates a user
	 * @param user the user to update
	 * @return the newly updated user object
	 */
	public User updateUser(User user);
	
	/**
	 * Deletes a user
	 * @param user the user to remove
	 * @return true if removed; false if not removed
	 */
	public boolean removeUser(User user);
	
}
