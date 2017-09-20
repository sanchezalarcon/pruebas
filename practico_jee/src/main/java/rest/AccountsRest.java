package rest;

import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import model.User;
import service.UserSessionBean;

@Path("/accountsrest")
public class AccountsRest {
	
	@Inject private UserSessionBean userService;
	@Inject private Validator validator;
	@Inject private Logger log;
	
	@Context HttpServletRequest request;


	@GET
	@Path("/hello")
	public String hello() {
	return "hola";
	}
	
	@Path("/login")
	@POST
	public String login(@FormParam("username") String username, @FormParam("password") String password) {
		
		
	
		if (request.getUserPrincipal() == null) {
	        try {       	
	        	request.getSession();
	    	    request.login(username,password);
	            //return Response.status(Response.Status.OK.entity("ok").type(MediaType.TEXT_PLAIN).build();
	            return userService.getByUserNamePassword(username,password).getName().toString();
	        } catch (ServletException ignored) {
	            // username/password desconocidos en request.login().
	        	
	            return "Usuario/contraseña incorrectos";
	        }
	    } 
	    
	    return "no login";
	}
	
	@GET
	@Path("/usuarios")
	@Produces({ "application/xml" })
	public List<User> getUsuarios() {
		return userService.findAll();
	}
	
	@POST
	@Consumes("application/xml")
	@Produces("application/xml")
	public Response altaUsuario(User user) {
	
	    Response.ResponseBuilder builder = null;

        try {
            // Validates member using bean validation
            validateMember(user);

            userService.altaUsuario(user);

            // Create an "ok" response
            builder = Response.ok().entity(user);
        } catch (ConstraintViolationException ce) {
            // Handle bean validation issues
            builder = createViolationResponse(ce.getConstraintViolations());
        } catch (ValidationException e) {
            // Handle the unique constrain violation
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("email", "Email taken");
            builder = Response.status(Response.Status.CONFLICT).entity(responseObj);
        } catch (Exception e) {
            // Handle generic exceptions
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("error", e.getMessage());
            builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
        }

        return builder.build();
	}

	@PUT
	@Path("/{username}")
	@Consumes({ "application/xml" })
	public Response modificarUsuario(@PathParam("username") String username, InputStream is) {
		return null;
	}

	@DELETE
	@Path("/{username}")
	@Consumes({ "application/xml" })
	public void eliminarUsuario(@PathParam("username") String username) {
		
	}
	
	@GET
	@Path("/usuarios/{username}")
	@Produces({ "application/xml" })
	public User getUsuario(@PathParam("username") String username) {
		User u = userService.getByUserName(username);
		if (u==null) 
			throw new WebApplicationException(Response.Status.NOT_FOUND);
			
		return u;	
	}
	
    /**
     * <p>
     * Validates the given Member variable and throws validation exceptions based on the type of error. If the error is standard
     * bean validation errors then it will throw a ConstraintValidationException with the set of the constraints violated.
     * </p>
     * <p>
     * If the error is caused because an existing member with the same email is registered it throws a regular validation
     * exception so that it can be interpreted separately.
     * </p>
     * 
     * @param member Member to be validated
     * @throws ConstraintViolationException If Bean Validation errors exist
     * @throws ValidationException If member with the same email already exists
     */
    private void validateMember(User user) throws ConstraintViolationException, ValidationException {
        // Create a bean validator and check for issues.
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
        }

        // Check the uniqueness of the email address
        if (userNameAlreadyExists(user.getUsername())) {
            throw new ValidationException("UserName único Violation");
        }
    }
    
    /**
     * Creates a JAX-RS "Bad Request" response including a map of all violation fields, and their message. This can then be used
     * by clients to show violations.
     * 
     * @param violations A set of violations that needs to be reported
     * @return JAX-RS response containing all violations
     */
    private Response.ResponseBuilder createViolationResponse(Set<ConstraintViolation<?>> violations) {
        log.fine("Validation completed. violations found: " + violations.size());

        Map<String, String> responseObj = new HashMap<>();

        for (ConstraintViolation<?> violation : violations) {
            responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
        }

        return Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
    }
    
    /**
     * Checks if a member with the same email address is already registered. This is the only way to easily capture the
     * "@UniqueConstraint(columnNames = "email")" constraint from the Member class.
     * 
     * @param email The email to check
     * @return True if the email already exists, and false otherwise
     */
    public boolean userNameAlreadyExists(String username) {
        User user = null;
        try {
            user = userService.getByUserName(username);
        } catch (NoResultException e) {
            // ignore
        }
        return user != null;
    }


}