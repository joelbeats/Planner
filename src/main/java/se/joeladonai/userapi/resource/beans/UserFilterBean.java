package se.joeladonai.userapi.resource.beans;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;

public class UserFilterBean {
	private @QueryParam("firstName") @DefaultValue("") String firstname;
	private @QueryParam("lastName") @DefaultValue("") String lastname;
	private @QueryParam("userName") @DefaultValue("") String username;
	
	
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	};
}
