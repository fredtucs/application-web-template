package org.wifry.fooddelivery.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;
import org.wifry.fooddelivery.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * Created by wtuco on 24/09/2015.
 */
@Component
public class AuthenticationSuccessCustomHandler implements org.springframework.security.web.authentication.AuthenticationSuccessHandler {

   /* @Autowired
    private IAuditoriaService auditoriaService;*/

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        User user = (User) authentication.getPrincipal();
        request.getSession().setAttribute("changepasswd", user.isForceChangePasswd());

        /*if (authentication != null) {
            auditoriaService.logSesion(SesionAccionEnum.LOGIN, authentication);
        }*/
//
//        if (user.isForceChangePasswd()) {
//            response.sendRedirect(request.getContextPath() + "/pages/config/changePassword.html");
//            return;
//        } else if (roles.contains("ROLE_ADMIN")) {
//            response.sendRedirect(request.getContextPath() + "/pages/admin/unidadEjecutora.html");
//            return;
//        } else if (roles.contains("ROLE_SECTORISTA")) {
//            response.sendRedirect(request.getContextPath() + "/pages/admin/usuario.html");
//            return;
//        } else {
        response.sendRedirect(request.getContextPath() + "/pages/home.html");
//        }
    }
}
