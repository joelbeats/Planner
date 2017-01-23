package se.joeladonai.userapi.resource;

import java.net.URI;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import se.joeladonai.userapi.model.Team;
import se.joeladonai.userapi.service.TeamService;

@Secured
@Component
@Path("/teams")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TeamResource {

	@Autowired
	private TeamService teamService;

	@POST
	public Response createTeam(Team team, @Context UriInfo uriInfo) throws Exception {
		Team newTeam = teamService.createTeam(team);
		String teamId = String.valueOf(newTeam.getId());
		URI location = uriInfo.getAbsolutePathBuilder().path(teamId).build();
		return Response.created(location).entity(team).build();
	}

	@PUT
	@Path("/{teamId}")
	public Team updateTeam(@PathParam("teamId") Long teamId, Team team) throws Exception {
		return teamService.updateTeam(teamId, team.getName());
	}

	@PUT
	@Path("/inactivate/{teamId}")
	public Team inactivateTeam(@PathParam("teamId") Long teamId) throws Exception {
		return teamService.inActivateTeam(teamId);
	}

	@GET
	public List<Team> getAllTeams() throws Exception {
		return teamService.getAllTeams();
	}
}