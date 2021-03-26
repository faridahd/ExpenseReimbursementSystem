package com.project.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.controllers.DataController;

public class DataHelper extends HttpServlet{
	public static void process(HttpServletRequest req, HttpServletResponse res) throws JsonProcessingException,
	IOException{
		System.out.println("in DataHelper");
		DataController.getData(req, res);
	}
}
