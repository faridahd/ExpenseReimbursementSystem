package com.project.controllers;

import javax.servlet.http.HttpServletRequest;

import com.project.beans.Reimbursement;
import com.project.beans.User;
import com.project.beans.User.UserType;
import com.project.dao.ReimbursementDao;
import com.project.dao.ReimbursementDaoDB;
import com.project.dao.UserDao;
import com.project.dao.UserDaoDB;
import com.project.service.ReimbursementService;
import com.project.service.UserService;

public class CurrentReimbController {
	public static String currentR(HttpServletRequest req) {
		
if(req.getSession().getAttribute("loggedUsername") != null && req.getSession().getAttribute("loggedPassword") != null) {
			
			
			
			ReimbursementDao rdao = new ReimbursementDaoDB();
			Reimbursement reim = new Reimbursement();
			UserDaoDB udao = new UserDaoDB();
			UserService usrv = new UserService(udao);
			ReimbursementService rsrv = new ReimbursementService(rdao);

			User user = new User();
			User user2 = new User();
			user = udao.getUser((String)req.getSession().getAttribute("loggedUsername"), (String)req.getSession().getAttribute("loggedPassword"));
			
			if(req.getParameter("AP") != null) {
				int id = Integer.parseInt(req.getParameter("AP"));
				System.out.println(req.getParameter("AP"));
				reim = rsrv.getUserTicketsById(id);
				if(reim.getAuthor().getUsername() != user.getFirstName()) {
					rsrv.approveOrDeny(reim, true);
				}
				req.setAttribute("AP", null);
			}
			
			if(req.getParameter("RE") != null) {
				int id = Integer.parseInt(req.getParameter("RE"));
				System.out.println(req.getParameter("RE"));
				reim = rsrv.getUserTicketsById(id);
				rsrv.approveOrDeny(reim, false);
				req.setAttribute("RE", null);
			}
			

			if(user.getUserType() == UserType.MANAGER) {
				return "resources/html/adminCurrent.html";
			} else {
				return "resources/html/currentbills.html";
			}
		} else {
			return "resources/html/index.html";
		}
		
	}
}
