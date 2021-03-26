package com.project.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.project.beans.Reimbursement;
import com.project.beans.Reimbursement.ReimbursementStatus;
import com.project.beans.User.UserType;
import com.project.beans.User;
import com.project.dao.ReimbursementDao;
import com.project.utils.SessionCache;

import sun.jvm.hotspot.gc.parallel.PSYoungGen;

import com.project.exceptions.UnauthorizedException;

public class ReimbursementService {
/**
* This class should contain the business logic for performing operations on reimbursements
*/
	public final static Logger logger = Logger.getLogger(UserService.class);
	ReimbursementDao rdao;
	
	public ReimbursementService(ReimbursementDao rdao) {
		this.rdao = rdao;
		logger.setLevel(Level.ALL);
	}
	
	//Employee accessed method
	/**
	 * create a reimbursement request.
	 * @param reim
	 * @throws UnauthorizedException if user not logged in
	 * @return true if reimbursement is created, or false if user not loged in
	 */
	public boolean createReimbursement(Reimbursement reim) {
		LocalDateTime currentDate = LocalDateTime.now();
		Timestamp now = Timestamp.valueOf(currentDate);
		if(SessionCache.getCurrentUser() != null) {
			if(reim.getType() == null) {
				System.out.println("Reimbursement Type cannot be null");
				return false;
			}
			reim.setStatus(ReimbursementStatus.PENDING);
			reim.setAuthor(SessionCache.getCurrentUser().get());
			reim.setDateSubmitted(now);
			rdao.addReimbursement(reim);
			logger.info("Reimbursement request submitted by : " + SessionCache.getCurrentUser().get().getUsername());
			return true;
		} else {
			try {
				throw new UnauthorizedException();
			} catch (UnauthorizedException e) {
				System.out.println("You don't have sufficient privileges!");
				logger.warn("Unauthorized attemp to submit a reimbursement request.");
				return false;
			}
		}
	}
	/**
	 * retrieve all past reimbursement tickets related to logged user, (Not pending tickets).
	 * @param
	 * @throws UnauthorizedException if user is not logged
	 * @return List<Reimbursement> a list resolved tickets if user is logged in and have reimbursement requests
	 */
	public List<Reimbursement> getMyPastTickets() {
		List<Reimbursement> reimList = new ArrayList<Reimbursement>();
		if(SessionCache.getCurrentUser() != null) {
			for(Reimbursement r: rdao.getReimbursementsByUser(SessionCache.getCurrentUser().get())) {
				if(!(r.getStatus() == ReimbursementStatus.PENDING)) {
					reimList.add(r);
				}
			}
			return reimList;
		} else {
			try {
				throw new UnauthorizedException();
			} catch (UnauthorizedException e) {
				System.out.println("You don't have sufficient privileges!");
				logger.warn("Unauthorized attemp to access reimbursement.");
				return null;
			}
		}
	}
	
	/**
	 * retrieve all pending reimbursement tickets related to logged user.
	 * @param
	 * @throws UnauthorizedException if user is not logged
	 * @return List<Reimbursement> a list pending tickets if user is logged in and have reimbursement requests
	 */
	public List<Reimbursement> getMyCurrentTickets() {
		List<Reimbursement> reimList = new ArrayList<Reimbursement>();
		if(SessionCache.getCurrentUser() != null) {
			for(Reimbursement r: rdao.getReimbursementsByUser(SessionCache.getCurrentUser().get())) {
				if(r.getStatus() == ReimbursementStatus.PENDING) {
					reimList.add(r);
				}
			}
			return reimList;
		} else {
			try {
				throw new UnauthorizedException();
			} catch (UnauthorizedException e) {
				System.out.println("You don't have sufficient privileges!");
				logger.warn("Unauthorized attemp to access reimbursement.");
				return null;
			}
		}
	}
	
	//MANAGER ACCESSED METHODS
	/**
	 * retrieve all past reimbursement tickets related to a specific user.
	 * @param
	 * @throws UnauthorizedException if user is not logged in and not have Manager privilege
	 * @return List<Reimbursement> a list of resolved tickets for a specific user
	 */
	public List<Reimbursement> getUserPastTickets(User user) {
		List<Reimbursement> reimList = new ArrayList<Reimbursement>();
		if(SessionCache.getCurrentUser() != null && SessionCache.getCurrentUser().get().getUserType() == UserType.MANAGER) {
			for(Reimbursement r: rdao.getReimbursementsByUser(user)) {
				if(!(r.getStatus() == ReimbursementStatus.PENDING)) {
					reimList.add(r);
				}
			}
			return reimList;
		} else {
			try {
				throw new UnauthorizedException();
			} catch (UnauthorizedException e) {
				System.out.println("You don't have sufficient privileges!");
				logger.warn("Unauthorized attemp to access reimbursement.");
				return null;
			}
		}
	}
	
	/**
	 * retrieve all past reimbursement tickets from all users.
	 * @param
	 * @throws UnauthorizedException if user is not logged in and not have Manager privilege
	 * @return List<Reimbursement> a list of resolved tickets
	 */
	public List<Reimbursement> getAllPastTickets() {
		List<Reimbursement> reimList = new ArrayList<Reimbursement>();
		if(SessionCache.getCurrentUser() != null && SessionCache.getCurrentUser().get().getUserType() == UserType.MANAGER) {
			for(Reimbursement r: rdao.getReimbursements()) {
				if(!(r.getStatus() == ReimbursementStatus.PENDING)) {
					reimList.add(r);
				}
			}
			return reimList;
		} else {
			try {
				throw new UnauthorizedException();
			} catch (UnauthorizedException e) {
				System.out.println("You don't have sufficient privileges!");
				logger.warn("Unauthorized attemp to access reimbursement.");
				return null;
			}
		}
	}
	
	
	/**
	 * retrieve all current reimbursement tickets from all users.
	 * @param
	 * @throws UnauthorizedException if user is not logged in and not have Manager privilege
	 * @return List<Reimbursement> a list of resolved tickets
	 */
	public List<Reimbursement> getAllCurrentTickets() {
		List<Reimbursement> reimList = new ArrayList<Reimbursement>();
		if(SessionCache.getCurrentUser() != null && SessionCache.getCurrentUser().get().getUserType() == UserType.MANAGER) {
			for(Reimbursement r: rdao.getReimbursements()) {
				if(r.getStatus() == ReimbursementStatus.PENDING) {
					reimList.add(r);
				}
			}
			return reimList;
		} else {
			try {
				throw new UnauthorizedException();
			} catch (UnauthorizedException e) {
				System.out.println("You don't have sufficient privileges!");
				logger.warn("Unauthorized attemp to access reimbursement.");
				return null;
			}
		}
	}
	
	/**
	 * retrieve a reimbursement related to logged user by Id.
	 * @param
	 * @throws UnauthorizedException if user is not logged or not have Manager privilege
	 * @return Reimbursement if user
	 */
	public Reimbursement getUserTicketsById(Integer reimId) {
		Reimbursement reim = new Reimbursement();
		if(SessionCache.getCurrentUser() != null && SessionCache.getCurrentUser().get().getUserType() == UserType.MANAGER) {
			reim = rdao.getReimbursement(reimId);
			return reim;
		} else {
			try {
				throw new UnauthorizedException();
			} catch (UnauthorizedException e) {
				System.out.println("You don't have sufficient privileges!");
				logger.warn("Unauthorized attemp to access reimbursement.");
				return null;
			}
		}
	}
	
	
	/**
	 * Approve or deny a reimbursement request.
	 * @param reim
	 * @param approval
	 * @throws UnauthorizedException if logged in user is not a Manager
	 * @return true if reimbursement is approved, or false if denied
	 */
	public boolean approveOrDeny(Reimbursement reim, boolean approval) {
		LocalDateTime currentDate = LocalDateTime.now();
		Timestamp now = Timestamp.valueOf(currentDate);
		if(SessionCache.getCurrentUser() != null) {
			if(approval) {
				reim.setStatus(ReimbursementStatus.APPROVED);
				reim.setResolver(SessionCache.getCurrentUser().get());
				reim.setDateResolved(now);
				rdao.updateReimbursement(reim);
				logger.info("Reimbursement request approved. Request ID: " + reim.getReimb_id());
				return true;
			} else {
				reim.setStatus(ReimbursementStatus.DENIED);
				reim.setResolver(SessionCache.getCurrentUser().get());
				reim.setDateResolved(now);
				rdao.updateReimbursement(reim);
				logger.info("Reimbursement request denied. Request ID: " + reim.getReimb_id());
				
				return false;
			}
			
		} else {
			try {
				throw new UnauthorizedException();
			} catch (UnauthorizedException e) {
				System.out.println("You don't have sufficient privileges!");
				logger.warn("Unauthorized attemp to approve or deny a reimbursement request");
				return false;
			}
		}
	}
}
