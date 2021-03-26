package com.project.dao;

import java.util.List;

import com.project.beans.Reimbursement;
import com.project.beans.User;

/**
 * The data access object interface for CRUD operations on Reimbursements.
 * This allows us to define a contract which can be implemented in different ways
 * using different methods of data storage and retrieval.
 */
public interface ReimbursementDao {
		/**
		 * Adds a new reimbursement request
		 * @param a the reimbursement object to add
		 * @return the same reimbursement that was added
		 */
		public Reimbursement addReimbursement(Reimbursement reimb);
		
		/**
		 * Retrieves a reimbursement
		 * @param reimbId the id of the reimbursement to retrieve
		 * @return the reimbursement object
		 */
		public Reimbursement getReimbursement(Integer reimbId);
		
		/**
		 * Retrieves all reimbursements
		 * @return a list of all reimbursements
		 */
		public List<Reimbursement> getReimbursements();
		
		/**
		 * Retrieves reimbursements by a particular user
		 * @param user the user object to search by
		 * @return a list of reimbursements that the user owns
		 */
		public List<Reimbursement> getReimbursementsByUser(User user);
		
		/**
		 * Updates a specific reimbursement
		 * @param reimb the reimbursement to update
		 * @return the updated reimbursement
		 */
		public Reimbursement updateReimbursement(Reimbursement reimb);
		
		/**
		 * Deletes a reimbursement
		 * @param reimb the reimbursement to delete
		 * @return true if the deletion was successful; false if not
		 */
		public boolean removeReimbursement(Reimbursement reimb);

}
