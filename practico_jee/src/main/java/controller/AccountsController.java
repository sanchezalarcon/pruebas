package controller;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import model.User;
import service.UserSessionBean;
import util.Resources;

/**
 * <p>lógica de cuentas de usuario y seguridad</p> 
 * <p>login, logout y ABM de usuarios</p>
 *  
 * @author gs
 *
 */
@ManagedBean
public class AccountsController {
	
    @Inject private FacesContext facesContext;
    @Inject private UserSessionBean userService;
    
    private static final String NAV_ACCOUNTS = "/views/accounts/accounts.xhtml";
    private static final String NAV_ACCOUNTS_REDIRECT = "/views/accounts/accounts.xhtml?faces-redirect=true";

    private String email;
    private String password;
    private String username;
    private String name;
    private String lastname;


    private User newUser;
    private User currentUser;

    private String passwordConfirmation;
    
    @PostConstruct
    public void postConstruct() {
    	
        ExternalContext externalContext = facesContext.getExternalContext();
        currentUser = (User)externalContext.getSessionMap().get("user");
        // en caso de ir a una URL específica
        if ( currentUser == null ) {
            HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
            java.security.Principal principal = request.getUserPrincipal();
            if ( principal != null ) {
                try {
                	currentUser = userService.getByUserName(principal.getName());
                } catch (Exception ignored) {
                    // logout usuario y set a null.
                    try {
                        ((HttpServletRequest) externalContext.getRequest()).logout();
                    } catch (ServletException alsoIgnored) {}
                    externalContext.invalidateSession();
                    currentUser = null;
                }
            }
        }
        newUser = new User();
    }
    
	/**
     * Login usuario con username y password 
     * @return String navigation to /views/accounts/accounts.xhtml
     */
    public String login() {
        ExternalContext externalContext = facesContext.getExternalContext();
        HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
        if (request.getUserPrincipal() == null) {
            try {       	
                request.login(username, password);
                currentUser = userService.getByUserNamePassword(username,password);
                externalContext.getSessionMap().put("user", currentUser);                
            } catch (ServletException ignored) {
                // username/password desconocidos en request.login().
                facesContext.addMessage(null, new FacesMessage("Usuario/contraseña incorrectos", ""));
                return null;
            }
        } 
        return NAV_ACCOUNTS_REDIRECT;
    }

	/**
     * Logout current user 
     * @return Naviation to /views/accounts/accounts.xhtml
     */
    public String logout() {
        ExternalContext externalContext = facesContext.getExternalContext();
        HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
        try {
            request.logout();
            externalContext.invalidateSession();
        } catch (ServletException e) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error en Logout", Resources.getRootErrorMessage(e) ));
            return null;
        }
        // navigate
        return NAV_ACCOUNTS_REDIRECT;
    }

    /**
     * Alta de nuevo usuario
     * @return String navigation to /views/accounts/accounts.xhtml
     */
    public String register() {
        try {
            newUser.setCreationdate(new Date());
            currentUser = userService.altaUsuario(newUser);
            if ( currentUser == null ) {
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error en el registro", "El usuario ya existe" ));
                return null;
            } else {
                // login usuario            
                ExternalContext externalContext = facesContext.getExternalContext();
                HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
                request.login(newUser.getUsername(), newUser.getPassword());
                externalContext.getSessionMap().put("user", currentUser);
            }
        } catch (Exception e) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error en el registro", Resources.getRootErrorMessage(e) ));
            return null;
        }
        // navigate
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Registro exitoso", "" ));
        return NAV_ACCOUNTS;
    }

    /**
     * Modificar password
     * @return String navigation to /views/accounts/accounts.xhtml
     */
    public String updatePassword() {
        try {
            // check password confirmation.
            if ( !currentUser.getPassword().equals(passwordConfirmation) ) {
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Los Passwords deberían ser iguales", ""));
                return null;
            }
            // update user
            userService.merge(userService.updatePassword(currentUser));
        } catch (Exception e) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al actualizar datos", Resources.getRootErrorMessage(e)));
            return null;
        }
        // message and navigation
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Password actualizado", ""));
        return NAV_ACCOUNTS;
    }

    /**
     * Modificar datos de usuario
     * @return Navigation to /views/accounts/accounts.xhtml
     */
    public String update() {
        try {
            // update user
            userService.merge(currentUser);
        } catch (Exception e) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al actualizar datos", Resources.getRootErrorMessage(e)));
            return null;
        }
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Información actualizada", ""));
        return NAV_ACCOUNTS;
    }

    /**
     * Listar todos los usuarios
     */
    public List<User> getUsers() {
        return userService.findAll();
    }

    /**
     * Borrar usuario por username
     */
    public void removeUser(String uname) {
        if ( currentUser.getUsername().equals(uname) ) throw new RuntimeException("No es posible eliminar al usuario");
        userService.delete(uname);
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuario borrado", "") );
    }
	
    /**
	 * True si el usuario esta logueado.
	 */
	public boolean isLoggedIn() {
	    return currentUser != null;
	}
	
	/**
	 * True si usuario tiene role ADMIN.
	 */
	public boolean isAdmin() {
	    if ( currentUser == null ) return false;
	    return currentUser.getRoleid().getName().equals("ADMIN");
	}
	
	/**
	 * Usuario logueado o null.
	 */
	public User getCurrentUser() {
	    return currentUser;
	}
	
	// getters and setters //
	
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
     
    public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	
    public User getNewUser() {
        return newUser;
    }

    public void setNewUser(User newUser) {
        this.newUser = newUser;
    }
    
    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public void setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }
	
	/**
	 * Method to determine whether a component has a validation error condition.
	 * 
	 * @author http://stackoverflow.com/questions/24329504/how-to-check-if-jsf-component-is-valid-in-xhtml
	 * @param clientId of component, e.g. id="test"
	 * @return true if no validation problem.
	 */
	public boolean isValid(String clientId) {
        UIComponent comp = FacesContext.getCurrentInstance().getViewRoot().findComponent(clientId);
        if(comp instanceof UIInput) {
            return ((UIInput)comp).isValid();
        }
        throw new IllegalAccessError();
    }
}
