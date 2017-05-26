package org.wifry.fooddelivery.repository.security;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.wifry.fooddelivery.base.BaseRepository;
import org.wifry.fooddelivery.model.Permission;

import java.util.List;

@Repository
public interface PermissionRepository extends BaseRepository<Permission, Long> {

    @Query("SELECT p FROM Permission p WHERE p.idPermission = ?1")
    Permission getByID(Long id);

    @Query("SELECT p FROM Permission p WHERE p.estado = 1 order by p.descripcion")
    List<Permission> list();

    @Query("SELECT p FROM Permission p order by p.descripcion")
    List<Permission> listAll();

    @Query("select distinct p from Permission p left join fetch p.roles order by p.descripcion")
    List<Permission> listPermissionForRole();

}