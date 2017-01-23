package se.joeladonai.userapi.resource;

import java.net.URI;
import java.util.List;

import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import se.joeladonai.userapi.model.Team;
import se.joeladonai.userapi.model.User;
import se.joeladonai.userapi.resource.beans.UserFilterBean;
import se.joeladonai.userapi.service.TeamService;
import se.joeladonai.userapi.service.UserService;

@Secured
@Component
@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

	@Autowired
	UserService userService;
	@Autowired
	TeamService teamService;

	@POST
	public Response createUser(User user, @Context UriInfo uriInfo) throws Exception {
		User newUser = userService.createUser(user);
		String id = String.valueOf(newUser.getId());
		URI location = uriInfo.getAbsolutePathBuilder().path(id).build();
		return Response.created(location).entity(user).build();
	}

	@PUT
	@Path("/{userId}")
	public User updateUser(@PathParam("userId") Long userId, User user) throws Exception {
		return userService.updateUser(userId, user);
	}

	@PUT
	@Path("/inactivate/{userId}")
	public User inactivateUser(@PathParam("userId") Long userId) throws Exception {
		return userService.inActivateUser(userId);
	}

	@GET
	@Path("/idnumber")
	public User getUserByIdNumber(@QueryParam("id") Long id) throws Exception {
		return userService.getUserByIdNumber(id);
	}

	@GET
	@Path("/find")
	public List<User> findUserByFirstNameOrLastNameOrUserName(
			@BeanParam UserFilterBean filterBean) throws Exception {
		return userService.getUserByUsernameAndFirstnameAndLastname(filterBean.getUsername(), filterBean.getFirstname(), filterBean.getLastname());

	}
	
	@GET
	@Path("/team/{teamId}")
	public List<User> getAllUsersFromTeam(@PathParam("teamId") Long teamId) throws Exception {
		return userService.getAllUserFromTeam(teamId);
	}

	@PUT
	@Path("/{userId}/team/{teamId}")
	public User addUserToTeam(@PathParam("userId") Long userId, @PathParam("teamId") Long teamId) throws Exception {
		Team team = teamService.getTeamById(teamId);
		User user = userService.getUserById(userId);

		user.setTeam(team);
		userService.updateUser(userId, user);

		return userService.getUserById(userId);
	}
}