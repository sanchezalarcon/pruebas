package service;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import model.Role;
import model.User;

/**
 * capa de servicios donde se exponen los métodos login, logout, ABM de usuarios, y  listado de usuarios filtrado
 * por un rango de fechas según su fecha de creación. Para la baja de usuarios, así como a los listados, 
 * debe tener el rol  ADMIN
 *   
 * @author gs
 *
 */
@Stateless
public class UserSessionBean {
	
    @Inject private EntityManager em;

    /**
     * Alta de usuario 
     */
    @PermitAll
    public User altaUsuario(User user)  {
        // verificar que no exista el nombre de usuario
        TypedQuery<Long> q = em.createNamedQuery(User.FIND_BY_UNAME, Long.class).setParameter("username", user.getUsername());
        if ( q.getSingleResult().longValue() > 0L ) {
            
            return null;
        }
        
        user.setPassword( user.getPassword() );
        // add role USER
        Role user_role = em.createNamedQuery(Role.USER_ROLE, Role.class).setParameter("nombre", "USER").getSingleResult();
        user.setRoleid(user_role);
        // Persist user.
        em.persist(user);
        em.flush();
        return user;
    }
    
    /**
     * Get usuario por username
     */
    @RolesAllowed({"USER","ADMIN"})
    public User getByUserName(String uname) {
        return em.find(User.class, uname);
    }
    
    /**
     * Get usuario si existe username y password, si no return null
     */
    @RolesAllowed({"USER","ADMIN"})
    public User getByUserNamePassword(String uname,String pass) {
        return em.createNamedQuery(User.FIND_BY_UNAME_PASS, User.class)
        		.setParameter("username", uname).setParameter("password", pass).getSingleResult();
    }
    
    /**
     * Baja de usuario por username
     */
    @RolesAllowed({"ADMIN"})
    public void delete(String uname) {
        em.remove( em.find(User.class, uname) );
    }

    /**
     * Modificar password 
     */
    @RolesAllowed({"USER","ADMIN"})
    public User updatePassword(User user) {
    	 user.setPassword( user.getPassword() );
        return em.merge(user);
    }

    /**
     * Modificar datos de usuario
     */
    @RolesAllowed({"USER","ADMIN"})
    public User merge(User user) {
        return em.merge(user);
    }
        
    /**
     * Listar todos los usuarios
     */
    //@RolesAllowed({"ADMIN"})
    @PermitAll
    public List<User> findAll() {
        return em.createNamedQuery(User.FIND_ALL, User.class).getResultList();
    }
    
}
