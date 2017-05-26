package org.wifry.fooddelivery.web.security;

import org.omnifaces.util.Faces;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.wifry.fooddelivery.util.FacesUtils;
import org.wifry.fooddelivery.model.Role;
import org.wifry.fooddelivery.model.User;
import org.wifry.fooddelivery.security.SpringSecurityUtils;


import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.Serializable;
import java.util.Set;

@Component("loginBean")
@Scope("view")
public class LoginBean implements Serializable {

    private static final long serialVersionUID = 6161848514166778321L;
    private String username;
    private String password;

    public LoginBean() {
    }

    public String doLogin() throws IOException, ServletException {
        RequestDispatcher dispatcher = Faces.getRequest().getRequestDispatcher("/loginIn");
        dispatcher.forward(Faces.getRequest(), Faces.getResponse());
        Faces.responseComplete();
        return null;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User getUsercurrent() {
        User user = SpringSecurityUtils.getUserDetails();
        return user;
    }

    public String getUserCurrentRol() {
        User userDetails = SpringSecurityUtils.getUserDetails();
        if(userDetails == null){
            return "";
        }

        Set<Role> roles = getUsercurrent().getRoles();
        if (roles.isEmpty()) {
            try {
                Faces.invalidateSession();
                FacesUtils.addErrorMessage("El usuario no tiene privilegios");
                Faces.redirect("/");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Role role = roles.stream().findFirst().orElse(null);
        return role.getDescripcion();
    }

}
