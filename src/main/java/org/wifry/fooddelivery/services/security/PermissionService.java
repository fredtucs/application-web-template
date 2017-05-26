package org.wifry.fooddelivery.services.security;

import org.wifry.fooddelivery.model.Permission;
import org.wifry.fooddelivery.services.BaseService;

import java.util.List;

public interface PermissionService extends BaseService<Permission> {
    List<Permission> listPermissionForRole();
}
