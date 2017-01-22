package se.joeladonai.userapi.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import se.joeladonai.userapi.model.User;
import se.joeladonai.userapi.secure.Credentials;
import se.joeladonai.userapi.secure.Token;
import se.joeladonai.userapi.service.UserService;

@Path("/authentication")
public class AuthenticationEndpoint {
	
	@Autowired
	private UserService userService;
	
	@GET
	@Path("/hej")
	public Response hello() {
		return Response.ok("hej").build();
	}
	
    @POST
    @Produces("application/json")
    @Consumes("application/json")
    public Response authenticateUser(Credentials credentials) throws Exception {
        String username = credentials.getUsername();
        String password = credentials.getPassword();
        // Authenticate the user, issue a token and return a response
        return authenticate(username, password);
    }

    @POST
    @Produces("application/json")
    @Consumes("application/x-www-form-urlencoded")
    public Response authenticateUser(@FormParam("username") String username, 
                                     @FormParam("password") String password) throws Exception {
    	return authenticate(username, password);
    }

    private Response authenticate(String username, String password) throws Exception {
    	try {
    		
    		User user = userService.getUserByName(username);
    		
        	String userPasswordInDbAndThePasswordIsHashed = user.getPassword();
        	//String saltInDb = user.getSalt();
        	
        	//Authentcate user
        	//String hashedInputPassword = HashPassword(password, saltInDb);
        	//if(hashedInputPassword.equals(userPasswordInDbAndThePasswordIsHashed)){
        	System.out.println(username + " : " + password);
        	System.out.println(user.getUsername() + " : " + user.getPassword());
        	if(password.equals(userPasswordInDbAndThePasswordIsHashed)){
        		//It was correct password
                // Return the token on the response
        		Token token  = new Token();
    			user.setToken(token.getAccess_token());
    			user.setExpirationTime(token.getExpiration_time());
    			userService.updateUser(user.getId(), user);
    			return Response.ok(token).build();
        	}else {
        		//NOT CORRECT PASSWORD
                // Return the token on the response
                return Response.status(Status.UNAUTHORIZED).entity("Wrong password").build();

        	}


        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }   
    }

 
}