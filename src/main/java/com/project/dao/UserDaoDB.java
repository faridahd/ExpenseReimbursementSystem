package com.project.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.project.beans.User;
import com.project.beans.User.UserType;
import com.project.utils.ConnectionUtil;
/**
 * Implementation of UserDAO that reads/writes to a relational database
 */
public class UserDaoDB implements UserDao{

	static Connection conn = ConnectionUtil.getConnectionUtil().getConnection();
	
	@Override
	public User addUser(User user) {
		// TODO Auto-generated method stub
		//select from user role table
		String role_query = "SELECT * FROM ers_user_roles;";
		//insert into user's table
		String sql = "INSERT INTO ers_users(user_role_id, ers_username, ers_password, ers_firstname, ers_lastname, ers_email) VALUES(?,?,?,?,?,?)";
		try {
			//role table query handle
			Statement s = conn.createStatement();
			ResultSet rs = s.executeQuery(role_query);
			int role_id=0;
			while(rs.next()) {
				if(UserType.valueOf(rs.getString(2)) == user.getUserType()) {
					role_id = rs.getInt(1);
				}
			}
			
			
			//user table query handle
			PreparedStatement ps = conn.prepareStatement(sql);
			
			ps.setInt(1, role_id);
			
			if(user.getUsername() != null) {
				ps.setString(2, user.getUsername());
			} else {
				ps.setString(2, "");
			}
			
			if(user.getPassword() != null) {
				ps.setString(3, user.getPassword());
			} else {
				ps.setString(3, "");
			}
			
			if(user.getFirstName() != null) {
				ps.setString(4, user.getFirstName());
			} else {
				ps.setString(4, "");
			}
			
			if(user.getLastName() != null) {
				ps.setString(5, user.getLastName());
			} else {
				ps.setString(5, "");
			}

			if(user.getEmail() != null) {
				ps.setString(6, user.getEmail());
			} else {
				ps.setString(6, "");
			}
			
			ps.execute();
			return user;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public User getUser(Integer userId) {
		// TODO Auto-generated method stub
		User user = null;
		try {
			String sql = "select u.ers_user_id, u.ers_username, u.ers_password, u.ers_firstname, u.ers_lastname, u.ers_email, r.user_role \r\n" 
						+ "from ers_users u, ers_user_roles r\r\n"
						+ "where u.user_role_id = r.user_role_id AND u.ers_user_id='" + userId + "';";
			Statement s = conn.createStatement();
			ResultSet rs = s.executeQuery(sql);
			while(rs.next()) {
				user = new User();
				user.setId(rs.getInt(1));
				user.setUsername(rs.getString(2));
				user.setPassword(rs.getString(3));
				user.setFirstName(rs.getString(4));
				user.setLastName(rs.getString(5));
				user.setEmail(rs.getString(6));
				user.setUserType(UserType.valueOf(rs.getString(7)));
			}
			return user;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public User getUser(String username, String pass) {
		// TODO Auto-generated method stub
		User user = null;
		try {
			String sql = "select u.ers_user_id, u.ers_username, u.ers_password, u.ers_firstname, u.ers_lastname, u.ers_email, r.user_role \r\n" 
						+ "from ers_users u, ers_user_roles r\r\n"
						+ "where u.user_role_id = r.user_role_id AND u.ers_username='" + username + "' AND u.ers_password='"+ pass +"';";
			Statement s = conn.createStatement();
			ResultSet rs = s.executeQuery(sql);
			while(rs.next()) {
				user = new User();
				user.setId(rs.getInt(1));
				user.setUsername(rs.getString(2));
				user.setPassword(rs.getString(3));
				user.setFirstName(rs.getString(4));
				user.setLastName(rs.getString(5));
				user.setEmail(rs.getString(6));
				user.setUserType(UserType.valueOf(rs.getString(7)));
			}
			return user;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<User> getAllUsers() {
		// TODO Auto-generated method stub
		List<User> userList = new ArrayList<User>();
		User user = null;
		try {
			String sql = "select u.ers_user_id, u.ers_username, u.ers_password, u.ers_firstname, u.ers_lastname, u.ers_email, r.user_role \r\n" 
						+ "from ers_users u, ers_user_roles r\r\n"
						+ "where u.user_role_id = r.user_role_id;";
			Statement s = conn.createStatement();
			ResultSet rs = s.executeQuery(sql);
			while(rs.next()) {
				user = new User();
				user.setId(rs.getInt(1));
				user.setUsername(rs.getString(2));
				user.setPassword(rs.getString(3));
				user.setFirstName(rs.getString(4));
				user.setLastName(rs.getString(5));
				user.setEmail(rs.getString(6));
				user.setUserType(UserType.valueOf(rs.getString(7)));
				userList.add(user);
			}
			return userList;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public User updateUser(User user) {
		// TODO Auto-generated method stub
		String sql = "update ers_users set user_role_id=?, ers_username=?, ers_password=?, ers_firstname=?, ers_lastname=?, ers_email=? where ers_user_id=?";
		
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			
			if(user.getUserType()==UserType.MANAGER) {
				ps.setInt(1, 2);
			}else {
				ps.setInt(1, 1);
			}
			
			if(user.getUsername() != null) {
				ps.setString(2, user.getUsername());
			} else {
				ps.setString(2, "");
			}
			
			if(user.getPassword() != null) {
				ps.setString(3, user.getPassword());
			} else {
				ps.setString(3, "");
			}
			
			if(user.getFirstName() != null) {
				ps.setString(4, user.getFirstName());
			} else {
				ps.setString(4, "");
			}
			
			if(user.getLastName() != null) {
				ps.setString(5, user.getLastName());
			} else {
				ps.setString(5, "");
			}

			if(user.getEmail() != null) {
				ps.setString(6, user.getEmail());
			} else {
				ps.setString(6, "");
			}
			
			ps.setInt(7, user.getId());
			
			ps.execute();
			return user;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean removeUser(User user) {
		// TODO Auto-generated method stub
		String sql = "delete from ers_users where ers_user_id=?";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, user.getId());
			ps.execute();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
}
