package org.wifry.fooddelivery.services.security.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wifry.fooddelivery.exceptions.ChangeStatusException;
import org.wifry.fooddelivery.model.Role;
import org.wifry.fooddelivery.model.User;
import org.wifry.fooddelivery.repository.security.RolRepository;
import org.wifry.fooddelivery.security.SpringSecurityUtils;
import org.wifry.fooddelivery.services.security.RoleService;

import java.util.ArrayList;
import java.util.List;

@Service("roleService")
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RolRepository roleDao;

    @Override
    public Role getByID(Long id) {
        return roleDao.getByID(id);
    }

    @Override
    public List<Role> find(String valor) {
        return null;
    }

    @Override
    public List<Role> listAll() {
        return roleDao.listRoleAll();
    }

    @Override
    public List<Role> list() {
        return roleDao.listRole();
    }

    @Override
    public List<Role> listParents() {
        User user = SpringSecurityUtils.getUserDetails();
        Role role = user.getRoles().stream().min((r1, r2) -> (int) (r1.getIdRole() - r2.getIdRole())).orElse(null);
        List<Role> result = roleDao.listRole();
        List<Role> oresult = new ArrayList<>();
        obtenerRoles(oresult, result, role);
        return oresult;
    }

    private void obtenerRoles(List<Role> outRoles, List<Role> inRoles, Role parent) {
        for (Role rol : inRoles) {
            if (rol.getParentRole() != null && rol.getParentRole().getIdRole().compareTo(parent.getIdRole()) == 0) {
                outRoles.add(rol);
                obtenerRoles(outRoles, inRoles, rol);
            }
        }
    }

    @Override
    public void save(Role rol) {
        roleDao.save(rol);
    }

    @Override
    public void delete(Role rol) {
        roleDao.delete(rol);
    }

    @Override
    public void updateState(Role entity) throws ChangeStatusException {
        roleDao.updateState(entity);
    }


}
