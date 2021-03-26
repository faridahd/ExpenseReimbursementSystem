package com.project.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.project.controllers.AddReimbController;
import com.project.controllers.CurrentReimbController;
import com.project.controllers.HomeController;
import com.project.controllers.LoginController;
import com.project.controllers.LogoutController;
import com.project.controllers.PastReimbController;

public class RequestHelper {
	public static String process(HttpServletRequest req) {
		switch(req.getRequestURI()) {
		case "/ReimbursementProject/login":	
			return LoginController.login(req);
		case "/ReimbursementProject/home":
			return HomeController.home(req);
		case "/ReimbursementProject/currentreimbursements":
			return CurrentReimbController.currentR(req);
		case "/ReimbursementProject/pastreimbursements":
			return PastReimbController.pastR(req);
		case "/ReimbursementProject/addreimbursement":
			return AddReimbController.addR(req);
		case "/ReimbursementProject/logout":
			return LogoutController.logout(req);
		default:
			return "resources/html/index.html";
			
		}
		
	 }
}
