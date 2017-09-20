package model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;


@NamedQueries({
	@NamedQuery(name = User.FIND_BY_UNAME, query = "select u from User u where u.username = :username"),
	@NamedQuery(name = User.FIND_BY_UNAME_PASS, query = "select u from User u where u.username = :username and u.password = :password"), 
	@NamedQuery(name = User.FIND_ALL, query = "select u from User u"), 
})
@SuppressWarnings("serial")
@XmlRootElement
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "email"),name = "Usuario")
@Entity
public class User implements Serializable {
	public static final String FIND_BY_UNAME = "User.findByUserName";
	public static final String FIND_BY_UNAME_PASS = "User.findByUserNamePassword";
    public static final String FIND_ALL = "User.findAll";

    @Id
	@NotNull(message="{username.required}")
	@Size(min = 8, max = 16)
	private String username;
    
    @XmlTransient
    @NotNull(message="{password.required}")
	@Size(min = 8, max = 16)
    private String password;
    
	@NotNull(message="{name.required}")
	@Size(max = 128)
	private String name;
	
	@NotNull(message="{lastname.required}")
	@Size(max = 128)
	private String lastname;
    
    @NotNull(message="{email.required}")
    @Size(max = 128)
    @Pattern(regexp = "[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\."
            + "[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+)*@"
            + "(?:[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?\\.)+[A-Za-z0-9]"
            + "(?:[A-Za-z0-9-]*[A-Za-z0-9])?",
            message = "{invalid.email}")
    private String email;

    @NotNull
	private Date creationdate;
    
    @OneToOne
    @JoinColumn(name="roleid")
	private Role roleid;
    
    /** Getters and Setteres */

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getCreationdate() {
		return creationdate;
	}

	public void setCreationdate(Date creationdate) {
		this.creationdate = creationdate;
	}

	public Role getRoleid() {
		return roleid;
	}

	public void setRoleid(Role roleid) {
		this.roleid = roleid;
	}
}
