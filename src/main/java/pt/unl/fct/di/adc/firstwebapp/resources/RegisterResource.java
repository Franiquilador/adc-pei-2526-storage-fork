package pt.unl.fct.di.adc.firstwebapp.resources;

import java.util.logging.Logger;

import com.google.gson.GsonBuilder;
import org.apache.commons.codec.digest.DigestUtils;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response.Status;

import com.google.gson.Gson;
import com.google.cloud.Timestamp;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.Transaction;
import com.google.cloud.datastore.DatastoreOptions;

import pt.unl.fct.di.adc.firstwebapp.util.LoginData;
import pt.unl.fct.di.adc.firstwebapp.util.RegisterData;
import pt.unl.fct.di.adc.firstwebapp.util.ErrorResponse;
import pt.unl.fct.di.adc.firstwebapp.util.UserResponse;

@Path("/createaccount")
public class RegisterResource {

	private static final Logger LOG = Logger.getLogger(RegisterResource.class.getName());
	private static final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

	//private final Gson g = new Gson();
	private final Gson g = new GsonBuilder().setPrettyPrinting().create(); // for the json to be indented correctly


	public RegisterResource() {}	// Default constructor, nothing to do
	
	@POST
	@Path("/v1")
	@Consumes(MediaType.APPLICATION_JSON)
	
	public jakarta.ws.rs.core.Response registerUserV1(LoginData data) {
		LOG.fine("Attempt to register user: " + data.input.username);
	
		Key userKey = datastore.newKeyFactory().setKind("User").newKey(data.input.username);
		Entity user = Entity.newBuilder(userKey)
						.set("user_pwd", DigestUtils.sha512Hex(data.input.password))
						.set("user_creation_time", Timestamp.now())
						.build();
		datastore.put(user);
		LOG.info("User registered " + data.input.username);

		UserResponse response = new UserResponse(data.input.username, data.input.role);

		return jakarta.ws.rs.core.Response.ok().entity(g.toJson(response)).build();
    }

    @POST
	@Path("/v2")
	@Consumes(MediaType.APPLICATION_JSON)
	public jakarta.ws.rs.core.Response registerUserV2(RegisterData data) {
		LOG.fine("Attempt to register user: " + data.input.username);

		if(!data.validRegistration()) {
			// INVALID_INPUT
			ErrorResponse responseErr = new ErrorResponse("9906", "The call is using input data not following the correct specification");

			return jakarta.ws.rs.core.Response.ok().entity(g.toJson(responseErr)).build();
		}
					
		Key userKey = datastore.newKeyFactory().setKind("User").newKey(data.input.username);
		Entity user = datastore.get(userKey);
		
		if(user != null) {
			// USER_ALREADY_EXISTS
			ErrorResponse responseErr = new ErrorResponse("9901", "Error in creating an account because the username already exists");

			return jakarta.ws.rs.core.Response.ok().entity(g.toJson(responseErr)).build();
		}
		
		user = Entity.newBuilder(userKey)
				.set("user_name", data.input.username)
				.set("user_pwd", DigestUtils.sha512Hex(data.input.password))
				.set("user_creation_time", Timestamp.now())
				.build();

		// Concurrency problem...
		// When we reach here, another client might have put() an entity with the same key...
		
		datastore.put(user);
		LOG.info("User registered " + data.input.username);

		UserResponse response = new UserResponse(data.input.username, data.input.role);
		
		return jakarta.ws.rs.core.Response.ok().entity(g.toJson(response)).build();
	}

    @POST
    @Path("/") // previously v3
    @Consumes(MediaType.APPLICATION_JSON)
    public jakarta.ws.rs.core.Response registerUserV3(RegisterData data) {
        LOG.fine("Attempt to register user: " + data.input.username);

		if(!data.validRegistration()) {
			// INVALID_INPUT
			ErrorResponse responseErr = new ErrorResponse("9906", "The call is using input data not following the correct specification");

			return jakarta.ws.rs.core.Response.ok().entity(g.toJson(responseErr)).build();
		}

        try {
            Transaction txn = datastore.newTransaction();
            Key userKey = datastore.newKeyFactory().setKind("User").newKey(data.input.username);
            Entity user = txn.get(userKey);

            if(user != null) {
                txn.rollback();

				// USER_ALREADY_EXISTS
				ErrorResponse responseErr = new ErrorResponse("9901", "Error in creating an account because the username already exists");

                return jakarta.ws.rs.core.Response.ok().entity(g.toJson(responseErr)).build();
            }            
            else {
                user = Entity.newBuilder(userKey)
                        .set("user_name", data.input.username)
                        .set("user_pwd", DigestUtils.sha512Hex(data.input.password))
						.set("user_address", data.input.address)
						.set("user_role", data.input.role)
						.set("user_phone", data.input.phone)
                        .set("user_creation_time", Timestamp.now())
                        .build();
                txn.put(user);
                txn.commit();
                LOG.info("User registered " + data.input.username);

				UserResponse response = new UserResponse(data.input.username, data.input.role);

				return jakarta.ws.rs.core.Response.ok().entity(g.toJson(response)).build();
            }
        } catch (Exception e) {
            LOG.severe("Error registering user: " + e.getMessage());
            return jakarta.ws.rs.core.Response.status(Status.INTERNAL_SERVER_ERROR).entity("Error registering user.").build();
        }
        finally {
            // No need to rollback here, as we only have one transaction and it will be automatically rolled back if not committed.
        }
    }

	// to check if status is an error code

}