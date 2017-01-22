package se.joeladonai.userapi.resource;

import java.net.URI;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;

import se.joeladonai.userapi.model.WorkItem;
import se.joeladonai.userapi.model.WorkItem.WorkItemStatus;
import se.joeladonai.userapi.service.UserService;
import se.joeladonai.userapi.service.WorkItemService;

@Path("/workitems")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class WorkItemResource {
	@Autowired
	WorkItemService workItemService;
	@Autowired
	UserService userService;

	@POST
	public Response createWorkItem(WorkItem workItem, @Context UriInfo uriInfo) throws Exception {
		WorkItem newWorkItem = workItemService.createWorkItem(workItem);
		String workItemId = String.valueOf(newWorkItem.getId());
		URI location = uriInfo.getAbsolutePathBuilder().path(workItemId).build();
		return Response.created(location).entity(workItem).build();
	}

	@PUT
	@Path("/{workItemId}")
	public WorkItem changeWorkItemStatus(@PathParam("workItemId") Long workItemId,
			@QueryParam("status") WorkItemStatus status) throws Exception {
		return workItemService.changeWorkItemStatus(workItemId, status);
	}

	@DELETE
	@Path("{workItemId}")
	public Response deleteWorkItem(@PathParam("workItemId") Long id) throws Exception {
		workItemService.removeWorkItem(id);
		return Response.status(Status.NO_CONTENT).build();
	}

	@PUT
	@Path("{workItemId}/user/{userId}")
	public void assignWorkItemToUser(@PathParam("workItemId") Long workItemId, @PathParam("userId") Long userId)
			throws Exception {
		userService.addWorkItemToUser(userId, workItemId);
	}

	@GET
	@Path("/status")
	public List<WorkItem> getAllWorkItemsByStatus(@QueryParam("type") WorkItemStatus status) throws Exception {
		return workItemService.getWorkItemsByStatus(status);
	}

	@GET
	@Path("/team/{teamId}")
	public List<WorkItem> getWorkItemsByTeam(@PathParam("teamId") Long teamId) throws Exception {
		return workItemService.getAllWorkItemsByTeam(teamId);
	}

	@GET
	@Path("/user/{userId}")
	public List<WorkItem> getWorkItemsByUser(@PathParam("userId") Long userId) throws Exception {
		return workItemService.getAllWorkItemsByUser(userId);
	}

	@GET
	@Path("/description")
	public List<WorkItem> getWorkItemsByDescription(@QueryParam("word") String word) throws Exception {
		return workItemService.getWorkItemsByDescription(word);
	}
}