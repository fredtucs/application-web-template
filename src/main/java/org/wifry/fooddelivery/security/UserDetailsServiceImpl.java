package org.wifry.fooddelivery.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.wifry.fooddelivery.model.User;
import org.wifry.fooddelivery.services.security.UserService;


@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
        User user = userService.findByName(username);
        if (user == null)
            throw new UsernameNotFoundException("user not found");
        if (user != null && user.getAuthorities().isEmpty()) {
            throw new AuthenticationCredentialsNotFoundException("no tiene privilegio");
        }
        return user;
    }
}