package com.project.controllers;

import javax.servlet.http.HttpServletRequest;

import com.project.beans.Reimbursement;
import com.project.beans.Reimbursement.ReimbursementType;
import com.project.beans.User.UserType;
import com.project.dao.ReimbursementDao;
import com.project.dao.ReimbursementDaoDB;
import com.project.dao.UserDaoDB;
import com.project.service.ReimbursementService;
import com.project.service.UserService;

public class AddReimbController {

	public static String addR(HttpServletRequest req) {
//		if(!req.getMethod().equals("POST")) {
//			return "resources/html/index.html";
//		}
//		if(req.getSession().getAttribute("loggedUsername") != null) {
//			return "resources/html/index.html";
//		} else {
		
			System.out.println("User" + req.getSession().getAttribute("loggedUsername"));
			System.out.println("Pass" + req.getSession().getAttribute("loggedPassword"));
			System.out.println(req.getParameter("amount"));
			System.out.println(req.getParameter("description"));
			System.out.println(req.getParameter("receipt"));
			System.out.println(req.getParameter("type"));
			System.out.println("In addReimb controller");
			
			
			ReimbursementDao rdao = new ReimbursementDaoDB();
			Reimbursement reim = new Reimbursement();
			UserDaoDB udao = new UserDaoDB();
			UserService usrv = new UserService(udao);
			ReimbursementService rsrv = new ReimbursementService(rdao);
			
			reim.setAmount(Double.parseDouble(req.getParameter("amount")));
			reim.setDescription(req.getParameter("description"));
			reim.setReceipt(req.getParameter("receipt"));
			reim.setType(ReimbursementType.valueOf(req.getParameter("type")));
			rsrv.createReimbursement(reim);

			return "resources/html/home.html";
//		}
			
			
		
	}
}
