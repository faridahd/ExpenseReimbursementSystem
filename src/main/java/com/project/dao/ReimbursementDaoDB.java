package com.project.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;
import com.project.beans.Reimbursement;
import com.project.beans.User;
import com.project.beans.Reimbursement.ReimbursementStatus;
import com.project.beans.Reimbursement.ReimbursementType;
import com.project.beans.User.UserType;
import com.project.utils.ConnectionUtil;


public class ReimbursementDaoDB implements ReimbursementDao {

	static Connection conn = ConnectionUtil.getConnectionUtil().getConnection();
	UserDaoDB udao = new UserDaoDB();
	User user = new User();
	Reimbursement reimb = new Reimbursement();
	List<Reimbursement> reimbList = new ArrayList<Reimbursement>();
	
	@Override
	public Reimbursement addReimbursement(Reimbursement reimb) {
		// TODO Auto-generated method stub
		//select from user role table
		String reimbTypeQuery = "SELECT * FROM ers_reimbursement_type;";
		//select from user role table
		String reimbStatusQuery = "SELECT * FROM ers_reimbursement_status;";
		//Select from reimbursement table
		String sql = "INSERT INTO ers_reimbursement(reimb_amount, reimb_submitted, "+
		"reimb_description, reimb_reciept, reimb_author, reimb_status_id, reimb_type_id)" +
		"VALUES(?,?,?,?,?,?,?)";
		
		Timestamp tSubmitted = null;
		if(!(reimb.getDateSubmitted()==null)) {
			tSubmitted = reimb.getDateSubmitted();
		}
				
				
		//Timestamp tResolved = Timestamp.valueOf(reimb.getDateResolved());

		int reimbStatusId = 0;
		int reimbTypeId = 0;
		int count = 0;
		try {
			//handle reimbursement type query
			Statement reimS = conn.createStatement();
			ResultSet reimRS = reimS.executeQuery(reimbTypeQuery);
			count = 0;
			while(reimRS.next()) {
				count++;
				if(ReimbursementType.valueOf(reimRS.getString(2)).equals(reimb.getType())){
					reimbTypeId = reimRS.getInt(1);
				}
				if(count == 4 && reimbTypeId == 0) {
					reimbTypeId = reimRS.getInt(1);
				}
			}
			
			//handle reimbursement status query
			reimRS = reimS.executeQuery(reimbStatusQuery);
			count = 0;
			while(reimRS.next()) {
				count++;
				if(ReimbursementStatus.valueOf(reimRS.getString(2)).equals(reimb.getStatus())){
					reimbStatusId = reimRS.getInt(1);
				}
				if(count == 3 && reimbStatusId == 0) {
					reimbStatusId = reimRS.getInt(1);
				}
			}
			
			//insert reimbursement request into reimbursement table
			//working here.....
			PreparedStatement ps = conn.prepareStatement(sql);
			if(reimb.getAmount() == null) {
				ps.setDouble(1, 0);
			} else {
				ps.setDouble(1, reimb.getAmount());
			}
			
			ps.setTimestamp(2, tSubmitted);
			ps.setString(3, reimb.getDescription());
			ps.setString(4, reimb.getReceipt());
			ps.setInt(5, reimb.getAuthor().getId());
			ps.setInt(6, reimbStatusId);
			ps.setInt(7, reimbTypeId);
			ps.execute();
			return reimb;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Reimbursement getReimbursement(Integer reimbId) {
		// TODO Auto-generated method stub
		
		try {
			//select reimbursement related records from different tables
			String sql = "select reim.reimb_id, reim.reimb_amount, reim.reimb_submitted, reim.reimb_resolved, reim.reimb_description, reim.reimb_reciept, \r\n"
					+ "st.reimb_status,\r\n"
					+ "ty.reimb_type,\r\n"
					+ "aut.ers_user_id, aut_rol.user_role, aut.ers_username, aut.ers_password, aut.ers_firstname, aut.ers_lastname, aut.ers_email,\r\n"
					+ "res.ers_user_id, res_rol.user_role, res.ers_username, res.ers_password, res.ers_firstname, res.ers_lastname, res.ers_email\r\n"
					+ "from ers_reimbursement reim \r\n"
					+ "left join ers_reimbursement_status st on reim.reimb_status_id = st.reimb_status_id\r\n"
					+ "left join ers_reimbursement_type ty on reim.reimb_type_id = ty.reimb_type_id\r\n"
					+ "left join ers_users aut on reim.reimb_author = aut.ers_user_id\r\n"
					+ "left join ers_user_roles aut_rol on aut_rol.user_role_id = aut.user_role_id \r\n"
					+ "left join ers_users res on reim.reimb_resolver = res.ers_user_id\r\n"
					+ "left join ers_user_roles res_rol on res_rol.user_role_id = res.user_role_id \r\n"
					+ "WHERE reim.reimb_id = '" + reimbId + "';";
			
			Statement st = conn.createStatement();
			
			ResultSet rs = st.executeQuery(sql);
			while(rs.next()) {
				reimb = new Reimbursement();
				reimb.setReimb_id(rs.getInt(1));
				reimb.setAmount(rs.getDouble(2));
				if(rs.getTimestamp(3)==null) {
					reimb.setDateSubmitted(null);
				} else {
					reimb.setDateSubmitted(rs.getTimestamp(3));
				}

				if(rs.getTimestamp(4)==null) {
					reimb.setDateResolved(null);
				} else {
					reimb.setDateResolved(rs.getTimestamp(4));
				}

				reimb.setDescription(rs.getString(5));
				
				reimb.setReceipt(rs.getString(6));
				
				reimb.setStatus(ReimbursementStatus.valueOf(rs.getString(7)));	

				reimb.setType(ReimbursementType.valueOf(rs.getString(8)));

				
				//Get Author
				User author = new User();
				author.setId(rs.getInt(9));
				author.setUserType(UserType.valueOf(rs.getString(10)));
				author.setUsername(rs.getString(11));
				author.setPassword(rs.getString(12));
				author.setFirstName(rs.getString(13));
				author.setLastName(rs.getString(14));
				author.setEmail(rs.getString(15));
				reimb.setAuthor(author);
				//Get Resolver
				User resolver = new User();
				if(rs.getInt(16) !=0) {
					resolver.setId(rs.getInt(16));
					resolver.setUserType(UserType.valueOf(rs.getString(17)));
					resolver.setUsername(rs.getString(18));
					resolver.setPassword(rs.getString(19));
					resolver.setFirstName(rs.getString(20));
					resolver.setLastName(rs.getString(21));
					resolver.setEmail(rs.getString(22));
					reimb.setResolver(resolver);
				}

			}
			return reimb;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Reimbursement> getReimbursements() {
		// TODO Auto-generated method stub
		reimbList = new ArrayList<Reimbursement>();
		try {
			//select reimbursement related records from different tables
			String sql = "select reim.reimb_id, reim.reimb_amount, reim.reimb_submitted, reim.reimb_resolved, reim.reimb_description, reim.reimb_reciept, "
					+ "st.reimb_status, "
					+ "ty.reimb_type, "
					+ "aut.ers_user_id, aut_rol.user_role, aut.ers_username, aut.ers_password, aut.ers_firstname, aut.ers_lastname, aut.ers_email, "
					+ "res.ers_user_id, res_rol.user_role, res.ers_username, res.ers_password, res.ers_firstname, res.ers_lastname, res.ers_email "
					+ "from ers_reimbursement reim "
					+ "left join ers_reimbursement_status st on reim.reimb_status_id = st.reimb_status_id "
					+ "left join ers_reimbursement_type ty on reim.reimb_type_id = ty.reimb_type_id "
					+ "left join ers_users aut on reim.reimb_author = aut.ers_user_id "
					+ "left join ers_user_roles aut_rol on aut_rol.user_role_id = aut.user_role_id "
					+ "left join ers_users res on reim.reimb_resolver = res.ers_user_id "
					+ "left join ers_user_roles res_rol on res_rol.user_role_id = res.user_role_id";
			
			Statement st = conn.createStatement();
			
			ResultSet rs = st.executeQuery(sql);
			while(rs.next()) {
				reimb = new Reimbursement();
				reimb.setReimb_id(rs.getInt(1));
				reimb.setAmount(rs.getDouble(2));
				if(rs.getTimestamp(3)==null) {
					reimb.setDateSubmitted(null);
				} else {
					reimb.setDateSubmitted(rs.getTimestamp(3));
				}

				if(rs.getTimestamp(4)==null) {
					reimb.setDateResolved(null);
				} else {
					reimb.setDateResolved(rs.getTimestamp(4));
				}

				reimb.setDescription(rs.getString(5));
				
				reimb.setReceipt(rs.getString(6));
				
				reimb.setStatus(ReimbursementStatus.valueOf(rs.getString(7)));	

				reimb.setType(ReimbursementType.valueOf(rs.getString(8)));
				
				//Get Author
				User author = new User();
				author.setId(rs.getInt(9));
				author.setUserType(UserType.valueOf(rs.getString(10)));
				author.setUsername(rs.getString(11));
				author.setPassword(rs.getString(12));
				author.setFirstName(rs.getString(13));
				author.setLastName(rs.getString(14));
				author.setEmail(rs.getString(15));
				reimb.setAuthor(author);
				//Get Resolver
				User resolver = new User();
				if(rs.getInt(16) !=0) {
					resolver.setId(rs.getInt(16));
					resolver.setUserType(UserType.valueOf(rs.getString(17)));
					resolver.setUsername(rs.getString(18));
					resolver.setPassword(rs.getString(19));
					resolver.setFirstName(rs.getString(20));
					resolver.setLastName(rs.getString(21));
					resolver.setEmail(rs.getString(22));
					reimb.setResolver(resolver);
				}
				reimbList.add(reimb);
			}
			return reimbList;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Reimbursement> getReimbursementsByUser(User user) {
		int userId = user.getId();
		reimbList = new ArrayList<Reimbursement>();
		try {
			//select reimbursement related records from different tables
			String sql = "select reim.reimb_id, reim.reimb_amount, reim.reimb_submitted, reim.reimb_resolved, reim.reimb_description, reim.reimb_reciept, "
					+ "st.reimb_status, "
					+ "ty.reimb_type, "
					+ "aut.ers_user_id, aut_rol.user_role, aut.ers_username, aut.ers_password, aut.ers_firstname, aut.ers_lastname, aut.ers_email, "
					+ "res.ers_user_id, res_rol.user_role, res.ers_username, res.ers_password, res.ers_firstname, res.ers_lastname, res.ers_email "
					+ "from ers_reimbursement reim "
					+ "left join ers_reimbursement_status st on reim.reimb_status_id = st.reimb_status_id "
					+ "left join ers_reimbursement_type ty on reim.reimb_type_id = ty.reimb_type_id "
					+ "left join ers_users aut on reim.reimb_author = aut.ers_user_id "
					+ "left join ers_user_roles aut_rol on aut_rol.user_role_id = aut.user_role_id "
					+ "left join ers_users res on reim.reimb_resolver = res.ers_user_id "
					+ "left join ers_user_roles res_rol on res_rol.user_role_id = res.user_role_id "
					+ "WHERE aut.ers_user_id = '" + userId + "';";
			
			Statement st = conn.createStatement();
			
			ResultSet rs = st.executeQuery(sql);
			while(rs.next()) {
				reimb = new Reimbursement();
				reimb.setReimb_id(rs.getInt(1));
				reimb.setAmount(rs.getDouble(2));
				if(rs.getTimestamp(3)==null) {
					reimb.setDateSubmitted(null);
				} else {
					reimb.setDateSubmitted(rs.getTimestamp(3));
				}

				if(rs.getTimestamp(4)==null) {
					reimb.setDateResolved(null);
				} else {
					reimb.setDateResolved(rs.getTimestamp(4));
				}

				reimb.setDescription(rs.getString(5));
				
				reimb.setReceipt(rs.getString(6));
				
				reimb.setStatus(ReimbursementStatus.valueOf(rs.getString(7)));	

				reimb.setType(ReimbursementType.valueOf(rs.getString(8)));
				
				//Get Author
				User author = new User();
				author.setId(rs.getInt(9));
				author.setUserType(UserType.valueOf(rs.getString(10)));
				author.setUsername(rs.getString(11));
				author.setPassword(rs.getString(12));
				author.setFirstName(rs.getString(13));
				author.setLastName(rs.getString(14));
				author.setEmail(rs.getString(15));
				reimb.setAuthor(author);
				//Get Resolver
				User resolver = new User();
				if(rs.getInt(16) !=0) {
					resolver.setId(rs.getInt(16));
					resolver.setUserType(UserType.valueOf(rs.getString(17)));
					resolver.setUsername(rs.getString(18));
					resolver.setPassword(rs.getString(19));
					resolver.setFirstName(rs.getString(20));
					resolver.setLastName(rs.getString(21));
					resolver.setEmail(rs.getString(22));
					reimb.setResolver(resolver);
				}
				reimbList.add(reimb);										
			}
			return reimbList;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Reimbursement updateReimbursement(Reimbursement reimb) {
		//select from user role table
		String reimbTypeQuery = "SELECT * FROM ers_reimbursement_type;";
		//select from user role table
		String reimbStatusQuery = "SELECT * FROM ers_reimbursement_status;";
		//Select from reimbursement table
		String sql = "UPDATE ers_reimbursement SET reimb_amount=?, reimb_submitted=?, reimb_resolved=?, "+
		"reimb_description=?, reimb_reciept=?, reimb_author=?, reimb_resolver=?, reimb_status_id=?, reimb_type_id=? "
		+ "WHERE reimb_id=?;";
		
		Timestamp tSubmitted = null;
		if(!(reimb.getDateSubmitted()==null)) {
			tSubmitted = reimb.getDateSubmitted();
		}
					
		Timestamp tResolved = null;
		if(!(reimb.getDateResolved()==null)) {
			tResolved = reimb.getDateResolved();
		}

		int reimbStatusId = 0;
		int reimbTypeId = 0;
		int count = 0;
		try {
			//handle reimbursement type query
			Statement reimS = conn.createStatement();
			ResultSet reimRS = reimS.executeQuery(reimbTypeQuery);
			count = 0;
			while(reimRS.next()) {
				count++;
				if(ReimbursementType.valueOf(reimRS.getString(2)).equals(reimb.getType())){
					reimbTypeId = reimRS.getInt(1);
				}
				if(count == 4 && reimbTypeId == 0) {
					reimbTypeId = reimRS.getInt(1);
				}
			}
			
			//handle reimbursement status query
			reimRS = reimS.executeQuery(reimbStatusQuery);
			count = 0;
			while(reimRS.next()) {
				count++;
				if(ReimbursementStatus.valueOf(reimRS.getString(2)).equals(reimb.getStatus())){
					reimbStatusId = reimRS.getInt(1);
				}
				if(count == 3 && reimbStatusId == 0) {
					reimbStatusId = reimRS.getInt(1);
				}
			}
			
			//insert reimbursement request into reimbursement table
			//working here.....
			PreparedStatement ps = conn.prepareStatement(sql);
			if(reimb.getAmount() == null) {
				ps.setDouble(1, 0);
			} else {
				ps.setDouble(1, reimb.getAmount());
			}
			
			ps.setTimestamp(2, tSubmitted);
			ps.setTimestamp(3, tResolved);
			ps.setString(4, reimb.getDescription());
			ps.setString(5, reimb.getReceipt());
			ps.setInt(6, reimb.getAuthor().getId());
			ps.setInt(7, reimb.getResolver().getId());
			ps.setInt(8, reimbStatusId);
			ps.setInt(9, reimbTypeId);
			ps.setInt(10, reimb.getReimb_id());
			
			ps.execute();
			
			return reimb;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean removeReimbursement(Reimbursement reimb) {
		// TODO Auto-generated method stub
		String sql = "DELETE FROM ers_reimbursement WHERE reimb_id=?;";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, reimb.getReimb_id());
			ps.execute();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

}
