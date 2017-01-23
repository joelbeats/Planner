package se.joeladonai.userapi.resource;

import java.io.IOException;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.NotAuthorizedException;
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
import se.joeladonai.userapi.secure.Token;
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
    	System.out.println("-------------------------");
    	System.out.println("--- AUTHENTICATION FILTER");
    	System.out.println("-------------------------");
    	
        // Get the HTTP Authorization header from the request
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        System.out.println("authHeader: " + authorizationHeader);
        
        
        // Check if the HTTP Authorization header is present and formatted correctly 
//        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer")) {
//            throw new NotAuthorizedException("Authorization header must be provided");
//        }

        // Extract the token from the HTTP Authorization header
        String token = requestContext.getHeaderString("Authorization");
        System.out.println("token: " + token);

        try {
        	
        	token = token.substring("Bearer".length()).trim();	
        	System.out.println("Bearer: " + token);
    		User user = userService.findUserByToken(token);
    		String expirationTime = user.getExpirationTime();
    		System.out.println(expirationTime);
    		System.out.println(System.currentTimeMillis());
    		
    		//OM Det inte passerat 600 sekunder sedan förra anropet, förläng exipriationTime
    		//Om Det har passerat 600 sek => UNAUTORIZE
    		
    		if (Long.parseLong(expirationTime) > System.currentTimeMillis()){
    			Long timeLeftOnSession = Long.parseLong(expirationTime) - System.currentTimeMillis();
    			System.out.println(timeLeftOnSession);
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