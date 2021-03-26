package com.project.controllers;

import javax.servlet.http.HttpServletRequest;

import com.project.beans.User;
import com.project.beans.User.UserType;
import com.project.dao.UserDao;
import com.project.dao.UserDaoDB;

public class HomeController {
	public static String home(HttpServletRequest req) {
		if(req.getSession().getAttribute("loggedUsername") != null && req.getSession().getAttribute("loggedPassword") != null) {
			return "resources/html/home.html";
		} else {
			return "resources/html/index.html";
		}
		
	}
}
