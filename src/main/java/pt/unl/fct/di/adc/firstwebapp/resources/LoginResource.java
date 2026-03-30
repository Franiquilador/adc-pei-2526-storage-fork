package pt.unl.fct.di.adc.firstwebapp.resources;

import java.util.logging.Logger;

import org.apache.commons.codec.digest.DigestUtils;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import pt.unl.fct.di.adc.firstwebapp.util.AuthToken;
import pt.unl.fct.di.adc.firstwebapp.util.ErrorResponse;
import pt.unl.fct.di.adc.firstwebapp.util.LoginData;

import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.gson.Gson;
import pt.unl.fct.di.adc.firstwebapp.util.LoginResponse;


@Path("/login")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class LoginResource {

	/** 
	 * Logger Object
	 */
	private static final Logger LOG = Logger.getLogger(LoginResource.class.getName());
	private static final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
	private static final KeyFactory userKeyFactory = datastore.newKeyFactory().setKind("User");

	private final Gson g = new Gson();
	
	public LoginResource() {} // Nothing to be done here
	
	@POST
	@Path("/test")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response doLogin(LoginData data) {
		LOG.fine("Attempt to login user: " + data.input.username);

		if(data.input.username.equals("user") && data.input.password.equals("password")) {


			AuthToken at = new AuthToken(data.input.username, "Error, this is wrong");
			return Response.ok(g.toJson(at)).build();
		}

		return Response.status(Response.Status.FORBIDDEN).entity("Incorrect username or password.").build();
		
	}
	
	@GET
	@Path("/{username}")
	public Response checkUsernameAvailable(@PathParam("username") String username) {
		if(username.trim().equals("user")) {
			return Response.ok().entity(g.toJson(false)).build();
		} else {
			return Response.ok().entity(g.toJson(true)).build();
		}
	}

	@POST
	@Path("/")// previously /rest/login/v1
	@Consumes(MediaType.APPLICATION_JSON)
	public Response doLoginV1(LoginData data) {
		LOG.fine("Attempt to login user: " + data.input.username);

		Key userKey = userKeyFactory.newKey(data.input.username);
		Entity user = datastore.get(userKey);

		if(user != null) {
			String hashedPWD = user.getString("user_pwd");
			if(hashedPWD.equals(DigestUtils.sha512Hex(data.input.password))) {
				LOG.info("User '" + data.input.username + "' logged in successfully.");

				String role = user.getString("user_role");
				AuthToken at = new AuthToken(data.input.username, role);

				// save the token to datastore so we can verify it is the correct one when authenticating another command (showusers for example)
				Key tokenKey = datastore.newKeyFactory().setKind("AuthToken").newKey(at.tokenId);
				Entity tokenEntity = Entity.newBuilder(tokenKey)
						.set("username", at.username)
						.set("role", at.role)
						.set("issuedAt", at.issuedAt)
						.set("expiresAt", at.expiresAt)
						.build();
				datastore.put(tokenEntity);

				LoginResponse response = new LoginResponse(data.input.username, at);

				return Response.ok().entity(g.toJson(response)).build();
			}
			else {
				LOG.warning("User '" + data.input.username + "' provided wrong password.");

				// INVALID_CREDENTIALS
				ErrorResponse responseErr = new ErrorResponse("9900", "The username-password pair is not valid");

				return Response.ok().entity(g.toJson(responseErr)).build();
			}
		}
		else {
			LOG.warning("User '" + data.input.username + "' does not exist.");

			// USER_NOT_FOUND
			ErrorResponse responseErr = new ErrorResponse("9902", "The username referred in the operation doesn’t exist in registered accounts");

			return Response.ok().entity(g.toJson(responseErr)).build();
		}
	}

}