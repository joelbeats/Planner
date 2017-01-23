package se.joeladonai.userapi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class User extends AbstractEntity {

	@Column(unique = true)
	private Long idNumber;

	@Column(unique = true)
	private String username;
	
	private String password;
	
	private String salt;

	private String firstname;

	private String lastname;
	
	private String token;
	
	private String expirationTime;
	

	@ManyToOne()
	@JoinColumn(name = "team_id")
	private Team team;

	public User(Long idNumber, String username, String firstname, String lastname, Team team) {
		this.idNumber = idNumber;
		this.username = username;
		this.firstname = firstname;
		this.lastname = lastname;
		this.team = team;
		this.token = "";
		this.expirationTime = "";
	}
	
	public User(Long idNumber, String username, String password, String firstname, String lastname, Team team) {
		this.idNumber = idNumber;
		this.username = username;
		this.salt = "";
		this.password = password;
		this.firstname = firstname;
		this.lastname = lastname;
		this.team = team;
		this.token = "";
		this.expirationTime = "";
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	protected User() {

	}

	public Long getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(Long idNumber) {
		this.idNumber = idNumber;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

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

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getExpirationTime() {
		return expirationTime;
	}

	public void setExpirationTime(String expirationTime) {
		this.expirationTime = expirationTime;
	}
}
