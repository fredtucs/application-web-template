package org.wifry.fooddelivery.model;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "T_M_PERMISSIONS")
public class Permission extends BaseEntity implements GrantedAuthority {

    private static final long serialVersionUID = -5404269148967698143L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_PERMISSION")
    @SequenceGenerator(name = "SQ_PERMISSION", sequenceName = "SQ_PERMISSION_ID", allocationSize = 1, initialValue = 1)
    private Long idPermission;

    @Column(nullable = false)
    private String descripcion;

    @Column(length = 50, unique = true)
    private String permissionName;

    @Column(length = 200)
    private String url;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "permissions")
    private Set<Role> roles;

    public Permission() {
    }

    public Permission(String descripcion, String permissionName, String url, Estado estado) {
        this.descripcion = descripcion;
        this.permissionName = permissionName;
        this.url = url;
        setEstado(estado);
    }

    public Long getIdPermission() {
        return idPermission;
    }

    public void setIdPermission(Long idPermission) {
        this.idPermission = idPermission;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionname) {
        this.permissionName = permissionname;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String getAuthority() {
        return permissionName;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Permission that = (Permission) o;
        return Objects.equals(idPermission, that.idPermission) &&
                Objects.equals(descripcion, that.descripcion) &&
                Objects.equals(permissionName, that.permissionName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPermission, descripcion, permissionName);
    }
}
