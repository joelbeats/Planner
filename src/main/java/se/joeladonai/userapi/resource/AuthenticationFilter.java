package se.joeladonai.userapi.resource;

import java.io.IOException;

import javax.annotation.Priority;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import se.joeladonai.userapi.model.User;
import se.joeladonai.userapi.secure.ExpirationTimeHelper;
import se.joeladonai.userapi.service.UserService;

@Component
@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {
	
	@Autowired
	private UserService userService;
	

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
     
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer")) {
            throw new NotAuthorizedException("Authorization header must be provided");
        }
        
        try {
        	authorizationHeader = authorizationHeader.substring("Bearer".length()).trim();	
        	
    		User user = userService.findUserByToken(authorizationHeader);
    		String expirationTime = user.getExpirationTime();
    		
    		if (Long.parseLong(expirationTime) > System.currentTimeMillis()){
    			user.setExpirationTime(ExpirationTimeHelper.exTimeGenerate());
    			userService.updateUser(user.getId(), user);
    		} else {
    			requestContext.abortWith(Response.status(Status.UNAUTHORIZED).entity("Your login session has expired, please login again").build());
    			return;
    		}

        } catch (Exception e) {
            requestContext.abortWith(
                Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }
}