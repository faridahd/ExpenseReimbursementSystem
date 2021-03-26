package com.project.controllers;

import javax.servlet.http.HttpServletRequest;

public class LogoutController {
	public static String logout(HttpServletRequest req) {
		req.getSession().invalidate();
		req.getSession().invalidate();
		req.getSession().invalidate();
		return "resources/html/index.html";
	}
}
