package org.wifry.fooddelivery.services.security.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wifry.fooddelivery.exceptions.ChangeStatusException;
import org.wifry.fooddelivery.exceptions.DeleteEntityException;
import org.wifry.fooddelivery.exceptions.NullPeriodoException;
import org.wifry.fooddelivery.repository.security.PermissionRepository;
import org.wifry.fooddelivery.services.security.PermissionService;
import org.wifry.fooddelivery.exceptions.SaveEntityException;
import org.wifry.fooddelivery.model.Permission;

import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;

    @Override
    public Permission getByID(Long id) {
        return permissionRepository.getByID(id);
    }

    @Override
    public List<Permission> listAll() {
        return permissionRepository.listAll();
    }

    @Override
    public List<Permission> list() {
        return permissionRepository.list();
    }

    @Override
    public List<Permission> listPermissionForRole() {
        return permissionRepository.listPermissionForRole();
    }

    @Override
    public void save(Permission entity) throws SaveEntityException, NullPeriodoException {
        permissionRepository.save(entity);
    }

    @Override
    public void delete(Permission entity) throws DeleteEntityException {
        permissionRepository.delete(entity);
    }

    @Override
    public void updateState(Permission entity) throws ChangeStatusException {
        permissionRepository.updateState(entity);
    }
}
