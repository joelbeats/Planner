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

import se.joeladonai.userapi.model.Issue;
import se.joeladonai.userapi.model.WorkItem;
import se.joeladonai.userapi.service.IssueService;
import se.joeladonai.userapi.service.WorkItemService;

@Path("/issues")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class IssueResource {

	@Autowired
	IssueService issueService;
	@Autowired
	WorkItemService workItemService;

	@POST
	@Path("/{workItemId}")
	public Response createIssueAndAssignToWorkItem(Issue issue1, @PathParam("workItemId") Long workItemId,
			@Context UriInfo uriInfo) throws Exception {
		Issue newIssue = issueService.createIssueAndAssignToWorkItem(issue1, workItemId);
		String id = String.valueOf(newIssue.getId());
		URI location = uriInfo.getAbsolutePathBuilder().path(id).build();
		return Response.created(location).entity(newIssue).build();
	}

	@PUT
	@Path("/{issueId}")
	public Response updateIssue(@PathParam("issueId") Long issueId, Issue issue) throws Exception {
		Issue issue2 = issueService.updateIssue(issueId, issue);
		return Response.ok(issue2).build();
	}

	@GET
	public Response getAllWorkItemsWithIssues() throws Exception {
		List<WorkItem> issues = workItemService.getWorkItemsWithIssue();
		return Response.ok(issues).build();
	}
}