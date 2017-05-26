

package org.wifry.fooddelivery.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "T_M_ROLES")
public class Role extends BaseEntity implements GrantedAuthority {

    private static final long serialVersionUID = 5995513420233295381L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_AUTHORITY")
    @SequenceGenerator(name = "SQ_AUTHORITY", sequenceName = "SQ_AUTHORITY_ID", allocationSize = 1, initialValue = 1)
    private Long idRole;

    @Column(nullable = false)
    private String descripcion;

    @Column(nullable = false, unique = true)
    private String roleName;

    @ManyToOne
    @JoinColumn(name = "idParentRole")
    private Role parentRole;

    @Column(nullable = false)
    private Boolean admin;

   /* @OneToMany(mappedBy="parentRole")
    private Set<Role> subRoles = new HashSet<>();*/

    /*@Fetch(FetchMode.JOIN)
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "roles")
    private Set<User> users;*/

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "T_M_ROLES_PERMISSIONS",
            joinColumns = @JoinColumn(name = "idRole", referencedColumnName = "idRole", foreignKey = @ForeignKey(name = "FK_ROLES_PERMISS")),
            inverseJoinColumns = @JoinColumn(name = "idPermission", referencedColumnName = "idPermission", foreignKey = @ForeignKey(name = "FK_PERMISS_ROLE"))
    )
    private Set<Permission> permissions;

    public Role() {
    }

    public Role(String descripcion, String roleName, Role parentRole, Boolean admin, Estado estado) {
        this.descripcion = descripcion;
        this.roleName = roleName;
        this.parentRole = parentRole;
        this.admin = admin;
        setEstado(estado);
    }

    public Role(String descripcion) {
        Assert.hasText(descripcion, "A granted authority textual representation is required");
        this.descripcion = descripcion;
    }

    public Long getIdRole() {
        return idRole;
    }

    public void setIdRole(Long idRole) {
        this.idRole = idRole;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String nombre) {
        this.descripcion = nombre;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String role) {
        this.roleName = role;
    }

    public Role getParentRole() {
        return parentRole;
    }

    public void setParentRole(Role parentRole) {
        this.parentRole = parentRole;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    /* public Set<Role> getSubRoles() {
        return subRoles;
    }

    public void setSubRoles(Set<Role> subRoles) {
        this.subRoles = subRoles;
    }*/

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    @Override
    public String getAuthority() {
        return this.roleName;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idRole == null) ? 0 : idRole.hashCode());
        result = prime * result + ((descripcion == null) ? 0 : descripcion.hashCode());
        result = prime * result + ((roleName == null) ? 0 : roleName.hashCode());
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
        Role other = (Role) obj;
        if (idRole == null) {
            if (other.idRole != null)
                return false;
        } else if (!idRole.equals(other.idRole))
            return false;
        if (descripcion == null) {
            if (other.descripcion != null)
                return false;
        } else if (!descripcion.equals(other.descripcion))
            return false;
        if (roleName == null) {
            if (other.roleName != null)
                return false;
        } else if (!roleName.equals(other.roleName))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Role[" +
                "idRole=" + idRole +
                ", roleName='" + roleName + '\'' +
                ']';
    }
}


