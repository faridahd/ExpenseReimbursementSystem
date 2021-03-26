package com.project.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.project.beans.User;
import com.project.dao.UserDao;
import com.project.dao.UserDaoDB;
import com.project.service.UserService;
import com.project.utils.SessionCache;

public class LoginController {
	public static String login(HttpServletRequest req) {
		if(!req.getMethod().equals("POST")) {
			return "resources/html/index.html";
		}
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		if(username != null & password != null) {
			
			UserDao udao = new UserDaoDB();
			User user = new User();
			UserService usrv = new UserService(udao);
			
			user = usrv.login(username, password);
			if(user != null) {
				req.getSession().setAttribute("loggedUsername", username);
				req.getSession().setAttribute("loggedPassword", password);
				req.getSession().setAttribute("isValid", "True");
				return "resources/html/home.html";
			} else {
				System.out.println("login failed");
				return "resources/html/index.html";
			}
		} else {
			return "resources/html/index.html";
		}
	}
}
