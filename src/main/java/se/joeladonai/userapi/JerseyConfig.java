package se.joeladonai.userapi;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import se.joeladonai.userapi.resource.AuthenticationEndpoint;
import se.joeladonai.userapi.resource.AuthenticationFilter;
import se.joeladonai.userapi.resource.IssueResource;
import se.joeladonai.userapi.resource.TeamResource;
import se.joeladonai.userapi.resource.UserResource;
import se.joeladonai.userapi.resource.WorkItemResource;

@Component
public class JerseyConfig extends ResourceConfig {

	public JerseyConfig() {
		register(UserResource.class);
		register(TeamResource.class);
		register(WorkItemResource.class);
		register(IssueResource.class);
		register(AuthenticationEndpoint.class);
		register(AuthenticationFilter.class);
	}
}
