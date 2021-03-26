package com.project.driver;

import com.project.beans.Reimbursement;
import com.project.beans.User;
import com.project.beans.Reimbursement.ReimbursementStatus;
import com.project.beans.Reimbursement.ReimbursementType;
import com.project.beans.User.UserType;
import com.project.dao.ReimbursementDaoDB;
import com.project.dao.UserDaoDB;
import com.project.service.ReimbursementService;
import com.project.service.UserService;
import com.project.utils.SessionCache;
import java.sql.Timestamp;
import java.time.LocalDateTime;
public class ReimbursementAppDriver {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Hello From Project 1");
		
		ReimbursementDaoDB rdao = new ReimbursementDaoDB();
		Reimbursement reim = new Reimbursement();
		
		User author = new User();
		User author2 = new User();
		User resolver = new User();
		UserDaoDB udao = new UserDaoDB();
		
		LocalDateTime currentDate = LocalDateTime.now();
		
		//author
		author.setUsername("farid");
		author.setPassword("password01");
		author.setUserType(UserType.EMPLOYEE);
		author.setEmail("farid_a@yahoo.com");
		//
		author.setId(1);
		//udao.updateUser(author);
		//udao.addUser(author);
		//udao.removeUser(udao.getUser(5));
		//author
		author2.setUsername("jamal");
		author2.setPassword("password03");
		author2.setUserType(UserType.EMPLOYEE);
		author2.setEmail("jamal@yahoo.com");
		//udao.addUser(author2);
		author2.setId(10);
		//
				
		//resolver
		resolver.setUsername("ahmad");
		resolver.setPassword("password02");
		resolver.setUserType(UserType.MANAGER);
		resolver.setEmail("ahmad@yahoo.com");
		//udao.addUser(resolver);
		resolver.setId(9);
		//reimbursement request
		
		Timestamp submit = Timestamp.valueOf(currentDate);
		//reim.setAmount(115.0);
		//reim.setDateSubmitted(submit);
		//reim.setDescription("This is a lodging");
		//reim.setReceipt("lodging receipt");
		//reim.setAuthor(author2);
		//reim.setStatus(ReimbursementStatus.PENDING);
		//reim.setType(ReimbursementType.LODGING);
		//rdao.addReimbursement(reim);
		
		//rdao.removeReimbursement(rdao.getReimbursement(2));
		//reimbursement resolve
		User admin = udao.getUser(9);
		Reimbursement re = new Reimbursement();
		re = rdao.getReimbursement(2);
		Timestamp resolve = Timestamp.valueOf(currentDate);
		re.setDateResolved(resolve);
		re.setResolver(admin);
		re.setStatus(ReimbursementStatus.DENIED);
		//rdao.updateReimbursement(re);
		
		//udao.removeUser(author2);
		//User Service check
		UserService usrv = new UserService(udao);
		ReimbursementService rsrv = new ReimbursementService(rdao);
		//usrv.register(author);
		//usrv.register(resolver);
		System.out.println("User Service:---------------");
		//Login
		usrv.login("ahmad", "password02");
		System.out.println("Current User Loged: " + SessionCache.getCurrentUser().get().getUsername());
		if(SessionCache.getCurrentUser().get().getUserType() == UserType.EMPLOYEE) {
			System.out.println("Eployee Section");
		} else if(SessionCache.getCurrentUser().get().getUserType() == UserType.MANAGER) {
			System.out.println("Finance Manager Section");
		} else {
			System.out.println("No one is loged in");
		}
		System.out.println("ReimbursementService...");
			//rsrv.createReimbursement(reim);
		System.out.println(".............................................");
		System.out.println("------------------------------");
		//Reimbursements by id:
		System.out.println("Reimbursements by Admin");
		System.out.println(rsrv.getAllPastTickets());
		System.out.println("\n\n*******************************************************");
		//Reimbursements by user:
		System.out.println("Reimbursements by Employee");
//		for(Reimbursement r : rdao.getReimbursementsByUser(author)) {
//			System.out.println(r);
//		}
		
		System.out.println("\n\n*******************************************************");
		System.out.println("Reimbursement:");
		for(Reimbursement r : rdao.getReimbursements()) {
			System.out.println(r.toString());
		}
		
		
		
		System.out.println("\n\n*******************************************************");
		
		System.out.println("Users:");
		for(User u : udao.getAllUsers()) {
			System.out.println(u.toString());
		}
		System.out.println("*******************************************************");
	}

}
