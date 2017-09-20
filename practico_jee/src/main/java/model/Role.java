package model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("serial")
@XmlRootElement
@Table(name = "Role")
@Entity
@NamedQueries({
    @NamedQuery(name = Role.LIST_AVAILABLE, query = "select r from Role r"), 
    @NamedQuery(name = Role.USER_ROLE, query = "select r from Role r where r.name = 'USER'"), 
    @NamedQuery(name = Role.ADMIN_ROLE, query = "select r from Role r where r.name = 'ADMIN'"),
})
public class Role implements Serializable {
    public static final String LIST_AVAILABLE = "Role.listAvailable";
    public static final String USER_ROLE = "Role.userRole";
    public static final String ADMIN_ROLE = "Role.adminRole";

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    @Column(unique = true, nullable=false)
    @Size(max = 256)	
    private String name;

    @NotNull
	@Size(max = 1024)	
	private String description;

    /** Getters and Setteres */
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
