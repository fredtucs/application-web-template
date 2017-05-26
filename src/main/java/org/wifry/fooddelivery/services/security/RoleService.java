package org.wifry.fooddelivery.services.security;

import org.wifry.fooddelivery.model.Role;
import org.wifry.fooddelivery.services.BaseService;

import java.util.List;

public interface RoleService extends BaseService<Role> {

    List<Role> listParents();

}