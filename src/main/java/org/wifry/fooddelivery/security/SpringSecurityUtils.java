package org.wifry.fooddelivery.security;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.wifry.fooddelivery.model.User;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

public class SpringSecurityUtils {

    public static <T extends org.springframework.security.core.userdetails.User> T getCurrentUser() {
        Authentication authentication = getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof org.springframework.security.core.userdetails.User) {
                return (T) principal;
            }
        }
        return null;
    }


    public static String getCurrentUserName() {
        Authentication authentication = getAuthentication();
        if (authentication != null && authentication.getPrincipal() != null) {
            return authentication.getName();
        }
        return "";
    }

    public static String getCurrentUserIp() {
        Authentication authentication = getAuthentication();
        if (authentication != null) {
            Object details = authentication.getDetails();
            if (details instanceof WebAuthenticationDetails) {
                WebAuthenticationDetails webDetails = (WebAuthenticationDetails) details;
                return webDetails.getRemoteAddress();
            }
        }

        return "";
    }


    public static void saveUserDetailsToContext(UserDetails userDetails, HttpServletRequest request) {
        PreAuthenticatedAuthenticationToken authentication = new PreAuthenticatedAuthenticationToken(userDetails,
                userDetails.getPassword(), userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public static Authentication getAuthentication() {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context != null) {
            return context.getAuthentication();
        }
        return null;
    }

    /**
     *
     * Comprobar si el usuario actual no incluye el permiso
     *
     * {@link SpringSecurityAuthorizeUtils}
     *
     * @param ifNotGranted
     *
     * @return No contiene acceso completo
     */
    public static final boolean ifNotGranted(String ifNotGranted) {
        final Collection<GrantedAuthority> granted = SpringSecurityAuthorizeUtils.getPrincipalAuthorities();
        return SpringSecurityAuthorizeUtils.ifNotGranted(granted, ifNotGranted);
    }

    /**
     * Comprobar si todas contienen los permisos actuales
     * <p>
     * {@link SpringSecurityAuthorizeUtils}
     *
     * @param ifAllGranted
     * @return Si todos los permisos y parámetros partido
     */
    public static final boolean ifAllGranted(String ifAllGranted) {
        final Collection<GrantedAuthority> granted = SpringSecurityAuthorizeUtils.getPrincipalAuthorities();
        return SpringSecurityAuthorizeUtils.ifAllGranted(granted, ifAllGranted);
    }

    /**
     *
     * Comprobar si el usuario actual contiene ningún tipo de permiso
     *
     * {@link SpringSecurityAuthorizeUtils}
     *
     * @param ifAnyGranted Ya sea volver permisos y permisos partido
     */
    public static final boolean ifAnyGranted(String ifAnyGranted) {
        final Collection<GrantedAuthority> granted = SpringSecurityAuthorizeUtils.getPrincipalAuthorities();
        return SpringSecurityAuthorizeUtils.ifAnyGranted(granted, ifAnyGranted);
    }

    public static User getUserDetails() {

        SecurityContext sc = SecurityContextHolder.getContext();
        Authentication auth = sc.getAuthentication();
        User ud = null;
        if ((auth != null) && auth.getPrincipal() instanceof User) {
            ud = (User) auth.getPrincipal();
        }
        return ud;
    }

}