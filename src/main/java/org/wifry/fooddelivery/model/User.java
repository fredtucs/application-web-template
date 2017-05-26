package org.wifry.fooddelivery.model;

import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "T_M_USER")
public class User extends BaseEntity implements UserDetails, CredentialsContainer {

    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_USER")
    @SequenceGenerator(name = "SQ_USER", sequenceName = "SQ_USER_ID", allocationSize = 1, initialValue = 1)
    private Long idUser;

    @Column(length = 100, nullable = false, unique = true)
    private String username;

    @Column(length = 100, nullable = false)
    private String password;

    @Column(nullable = false)
    private boolean forceChangePasswd = true;


    @Column(nullable = false)
    private boolean accountNonExpired = true;

    @Column(nullable = false)
    private boolean accountNonLocked = true;

    @Column(nullable = false)
    private boolean credentialsNonExpired = true;

    @Column(nullable = false)
    private Boolean admin;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "T_M_ROLES_USERS",
            joinColumns = @JoinColumn(name = "idUser", referencedColumnName = "idUser", foreignKey = @ForeignKey(name = "FK_USER_ROLES")),
            inverseJoinColumns = @JoinColumn(name = "idRole", referencedColumnName = "idRole", foreignKey = @ForeignKey(name = "FK_ROLES_USERS")))
    private Set<Role> roles;

    public User() {

    }

    /**
     * Construct the <code>User</code> with the details required by
     * {@link org.springframework.security.authentication.dao.DaoAuthenticationProvider}
     * .
     *
     * @param username              the username presented to the
     *                              <code>DaoAuthenticationProvider</code>
     * @param password              the password that should be presented to the
     *                              <code>DaoAuthenticationProvider</code>
     * @param accountNonExpired     set to <code>true</code> if the account has not expired
     * @param credentialsNonExpired set to <code>true</code> if the credentials have not expired
     * @param accountNonLocked      set to <code>true</code> if the account is not locked
     * @param roles                 the authorities that should be granted to the caller if they
     *                              presented the correct username and password and the user is
     *                              enabled. Not null.
     * @throws IllegalArgumentException if a <code>null</code> value was passed either as a parameter
     *                                  or as an element in the <code>GrantedAuthority</code>
     *                                  collection
     */
    public User(String username, String password, boolean forceChangePasswd, boolean accountNonExpired,
                boolean credentialsNonExpired, boolean accountNonLocked, Boolean admin, Set<Role> roles, Estado estado) {

        if (((username == null) || "".equals(username)) || (password == null)) {
            throw new IllegalArgumentException("Cannot pass null or empty values to constructor");
        }
        this.admin = admin;
        this.forceChangePasswd = forceChangePasswd;
        this.username = username;
        this.password = password;
        this.accountNonExpired = accountNonExpired;
        this.credentialsNonExpired = credentialsNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.roles = roles;
        setEstado(estado);
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

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

    public boolean isForceChangePasswd() {
        return forceChangePasswd;
    }

    public void setForceChangePasswd(boolean forceChangePasswd) {
        this.forceChangePasswd = forceChangePasswd;
    }

    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public boolean isEnabled() {
        return getEstado().isActivo();
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
        for (Role rol : roles) {
            authorities.add(rol);
            authorities.addAll(rol.getPermissions());
        }
        return authorities;
    }

    @Override
    public void eraseCredentials() {
        password = null;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((username == null) ? 0 : username.hashCode());
        return result;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        User other = (User) obj;
        if (username == null) {
            if (other.username != null)
                return false;
        } else if (!username.equals(other.username))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "User [username=" + username + "]";
    }

}