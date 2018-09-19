package com.capgemini.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.capgemini.model.Destination;
import com.capgemini.model.DestinationJsonResponse;
import com.capgemini.model.Source;
import com.capgemini.model.SourceJsonResponse;

@Controller
public class WebController {

	@RequestMapping("/home")
	public String sourceForm() {
		return "connection";
	}

	@RequestMapping(value = "/loginSource")
	public @ResponseBody SourceJsonResponse loginSource(
			@ModelAttribute Source source, BindingResult result)
			throws ClassNotFoundException, SQLException {

		SourceJsonResponse response = new SourceJsonResponse();

		response.setValidated(true);

		response.setSource(source);

		Class.forName(source.getDriverName());
		Connection con = DriverManager.getConnection(
				source.getConnectionString(), source.getUserName(),
				source.getPassword());
		Statement stmt = con.createStatement();
		String sql = "select * from dual";
		stmt.execute(sql);
		con.close();
		System.out.println("Source Connection Success");
		return response;
	}

	@RequestMapping(value = "/loginDestination")
	public @ResponseBody DestinationJsonResponse loginDestination(
			@ModelAttribute Destination destination, BindingResult result)
			throws ClassNotFoundException, SQLException {
		DestinationJsonResponse response = new DestinationJsonResponse();
		response.setValidated(true);
		response.setDestination(destination);
		Class.forName(destination.getDdriverName());
		Connection con = DriverManager.getConnection(
				destination.getDconnectionString(), destination.getDuserName(),
				destination.getDpassword());
		Statement stmt = con.createStatement();
		String sql = "select * from dual";
		stmt.execute(sql);
		con.close();
		System.out.println("Destination Connection Success");
		return response;
	}
	@RequestMapping("/table")
	public String table()
	{
		return "table";
		
	}

}
