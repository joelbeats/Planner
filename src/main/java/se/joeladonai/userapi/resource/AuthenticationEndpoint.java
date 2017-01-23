package se.joeladonai.userapi.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import se.joeladonai.userapi.model.User;
import se.joeladonai.userapi.secure.Credentials;
import se.joeladonai.userapi.secure.EncryptHelper;
import se.joeladonai.userapi.secure.Token;
import se.joeladonai.userapi.service.UserService;

@Path("/authentication")
@Component
public class AuthenticationEndpoint {
	
	@Autowired
	private UserService userService;
	
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response authenticateUser(Credentials credentials) throws Exception {
        String username = credentials.getUsername();
        String password = credentials.getPassword();
        System.out.println(username + " : " + password);
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
   
    		User user = userService.getUserByName(username);
    		
        	String userPasswordInDb = user.getPassword();
        	String saltInDb = user.getSalt();
        	
        	
        	String hashedInputPassword = EncryptHelper.hashPassword(password.toCharArray(), Base64.decodeBase64(saltInDb));
        	
        	System.out.println("");
        	System.out.println("");
        	System.out.println("");
        	System.out.println("----------------------------------");
        	System.out.println("userPasswordInDb: " + userPasswordInDb);
        	System.out.println("saltInDb: " +saltInDb);
        	System.out.println(hashedInputPassword + " ====> hashedInputPassword");
        	System.out.println(userPasswordInDb + " ====> userPasswordInDb");
        	System.out.println("----------------------------------");
        	System.out.println("");
        	System.out.println("");
        	System.out.println("");
        	
        	if(hashedInputPassword.equals(userPasswordInDb)){
        		Token token  = new Token();
        		System.out.println("access-token: " + token.getAccess_token());
        		System.out.println("expirationTime: " + token.getExpiration_time());
        		
    			user.setToken(token.getAccess_token());
    			user.setExpirationTime(token.getExpiration_time());
    			
    			System.out.println("access-token in user: " + user.getToken());
        		System.out.println("expirationTime in user: " + user.getExpirationTime());
    			
        		user.setFirstname("adonai");
    			userService.updateUser(user.getId(), user);
    			return Response.ok(token).build();
        	}else {
                return Response.status(Status.UNAUTHORIZED).entity("Your username or password is not valid").build();
        	
        }   
    }

 
}