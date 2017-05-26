package org.wifry.fooddelivery.repository.security;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.wifry.fooddelivery.model.Role;
import org.wifry.fooddelivery.base.BaseRepository;

import java.util.List;

@Repository
public interface RolRepository extends BaseRepository<Role, Long> {

    @Query("SELECT r FROM Role r LEFT JOIN FETCH r.parentRole LEFT JOIN FETCH r.permissions WHERE r.idRole = ?1")
    Role getByID(Long id);

    @Query("SELECT r FROM Role r WHERE r.estado = 1 order by r.descripcion")
    List<Role> listRole();

    @Query("SELECT r FROM Role r order by r.descripcion")
    List<Role> listRoleAll();

}