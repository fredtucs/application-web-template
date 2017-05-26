package org.wifry.fooddelivery.repository.security;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.wifry.fooddelivery.model.User;
import org.wifry.fooddelivery.base.BaseRepository;

import java.util.List;

@Repository
public interface UserRepository extends BaseRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.estado = 1 order by u.username")
    List<User> listUsers();


    @Query("SELECT f FROM User f WHERE UPPER(f.username) LIKE UPPER(CONCAT('%', ?1, '%') ) ORDER BY f.username")
    List<User> findUsers(String valor);

    User findByUsername(String username);
}