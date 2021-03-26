package com.project.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.beans.Reimbursement;
import com.project.beans.User;
import com.project.beans.User.UserType;
import com.project.dao.ReimbursementDao;
import com.project.dao.ReimbursementDaoDB;
import com.project.dao.UserDao;
import com.project.dao.UserDaoDB;
import com.project.service.ReimbursementService;
import com.project.service.UserService;


public class DataController {
	public static void getData(HttpServletRequest req, HttpServletResponse res) throws JsonProcessingException,
	IOException{
		
		ReimbursementDao rdao = new ReimbursementDaoDB();
		Reimbursement reim = new Reimbursement();
		User user = new User();
		UserDao udao = new UserDaoDB();
		ReimbursementService rsrv = new ReimbursementService(rdao);
		UserService usrv = new UserService(udao);
		
		
		System.out.println("List" + rdao.getReimbursements());
		System.out.println("In DataController");
		
		String username=(String) req.getSession().getAttribute("loggedUsername");
		String password=(String) req.getSession().getAttribute("loggedPassword");

		
		user = udao.getUser(username, password);
		res.setContentType("text/html");
		
		if(user != null) {
			if(user.getUserType() == UserType.EMPLOYEE) {
				List<Reimbursement> al1 = new ArrayList<Reimbursement>();
				al1 = rsrv.getMyCurrentTickets();
				List<Reimbursement> al2 = new ArrayList<Reimbursement>();
				al2 = rsrv.getMyPastTickets();
				al1.addAll(al2);
				res.getWriter().write(new ObjectMapper().writeValueAsString(al1));
			} else if(user.getUserType() == UserType.MANAGER) {
				List<Reimbursement> al1 = new ArrayList<Reimbursement>();
				al1 = rsrv.getAllCurrentTickets();
				List<Reimbursement> al2 = new ArrayList<Reimbursement>();
				al2 = rsrv.getAllPastTickets();
				al1.addAll(al2);
				res.getWriter().write(new ObjectMapper().writeValueAsString(al1));
			}
		}
		
	}
}
