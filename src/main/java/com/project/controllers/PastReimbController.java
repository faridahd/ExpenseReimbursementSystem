package com.project.controllers;

import javax.servlet.http.HttpServletRequest;

public class PastReimbController {
	public static String pastR(HttpServletRequest req) {
			
			if(req.getSession().getAttribute("loggedUsername") != null && req.getSession().getAttribute("loggedPassword") != null) {
				return "resources/html/pastbills.html";
			} else {
				return "resources/html/index.html";
			}
		
	}
}
