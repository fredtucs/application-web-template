package org.wifry.fooddelivery.services.security.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.wifry.fooddelivery.model.Role;
import org.wifry.fooddelivery.model.User;
import org.wifry.fooddelivery.repository.security.RolRepository;
import org.wifry.fooddelivery.repository.security.UserRepository;
import org.wifry.fooddelivery.services.security.UserService;
import org.wifry.fooddelivery.util.Md5Utils;

import java.util.List;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RolRepository rolRepository;

    @Override
    public User getByID(Long id) {
        User user = userRepository.findOne(id);
        if (user != null)
            user.getAuthorities().size();
        return user;
    }

    @Override
    public List<User> listAll() {
        return userRepository.findAll(new Sort(Sort.Direction.ASC, "apellidos"));
    }

    @Override
    public List<User> list() {
        return userRepository.listUsers();
    }

    @Override
    public List<User> find(String valor) {
        return userRepository.findUsers(valor);
    }

    @Override
    public User findByName(String username) {
        User user = userRepository.findByUsername(username);
        if (user != null)
            user.getAuthorities().size();
        return user;
    }

    @Override
    public void save(User user1) {
        if (user1.getPassword() != null && user1.getPassword().length() != 32)
            user1.setPassword(Md5Utils.hash(user1.getPassword()));
        userRepository.save(user1);
    }

    @Override
    public List<Role> listRoles() {
        return rolRepository.findAll();
    }

    @Override
    public void delete(User user) {
        userRepository.delete(user);
    }

    @Override
    public void updateState(User entity) {
        userRepository.updateState(entity);
    }


}
