package org.wifry.fooddelivery.services.security;

import org.wifry.fooddelivery.model.Role;
import org.wifry.fooddelivery.model.User;
import org.wifry.fooddelivery.services.BaseService;

import java.util.List;

public interface UserService extends BaseService<User> {
    User findByName(String username);

    List<Role> listRoles();

}
