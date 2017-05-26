package org.wifry.fooddelivery.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

import java.util.*;

public final class SpringSecurityAuthorizeUtils {

    private static final Logger log = LoggerFactory.getLogger(SpringSecurityAuthorizeUtils.class);

    public static final Set<GrantedAuthority> parseAuthoritiesString(String authorizationsString) {
        final Set<GrantedAuthority> requiredAuthorities = new HashSet<GrantedAuthority>();
        final String[] authorities = StringUtils.commaDelimitedListToStringArray(authorizationsString);

        for (int i = 0; i < authorities.length; i++) {
            String authority = authorities[i];

            // Remove the role's whitespace characters without depending on JDK
            // 1.4+
            // Includes space, tab, new line, carriage return and form feed.
            String role = authority.trim(); // trim, don't use spaces, as per
            // SEC-378
            role = StringUtils.deleteAny(role, "\t\n\r\f");
            requiredAuthorities.add(new SimpleGrantedAuthority(role));
        }

        return requiredAuthorities;
    }

    public static final Set<String> authoritiesToRoles(Collection<GrantedAuthority> c) {
        Set<String> target = new HashSet<String>();

        for (GrantedAuthority authority : c) {
            if (null == authority.getAuthority()) {
                throw new IllegalArgumentException(
                        "Cannot process GrantedAuthority objects which return null from getAuthority() - attempting to process "
                                + authority.toString());
            }
            target.add(authority.getAuthority());
        }
        return target;
    }

    public static final Set<GrantedAuthority> retainAll(final Collection<GrantedAuthority> granted,
                                                        final Set<GrantedAuthority> required) {
        Set<String> grantedRoles = authoritiesToRoles(granted);
        Set<String> requiredRoles = authoritiesToRoles(required);
        grantedRoles.retainAll(requiredRoles);
        return rolesToAuthorities(grantedRoles, granted);
    }

    public static final Set<GrantedAuthority> removeAll(final Collection<GrantedAuthority> granted,
                                                        final Set<GrantedAuthority> required) {
        Set<String> grantedRoles = authoritiesToRoles(granted);
        Set<String> requiredRoles = authoritiesToRoles(required);
        grantedRoles.removeAll(requiredRoles);
        return rolesToAuthorities(grantedRoles, granted);
    }

    public static final Set<GrantedAuthority> rolesToAuthorities(Set<String> grantedRoles,
                                                                 Collection<GrantedAuthority> granted) {
        Set<GrantedAuthority> target = new HashSet<GrantedAuthority>();
        for (String role : grantedRoles) {
            for (GrantedAuthority authority : granted) {
                if (authority.getAuthority().equals(role)) {
                    target.add(authority);
                    break;
                }
            }
        }

        return target;
    }

    public static final Collection<GrantedAuthority> getPrincipalAuthorities() {
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();

        if (null == currentUser) {
            return Collections.EMPTY_LIST;
        }
        if ((null == currentUser.getAuthorities()) || (currentUser.getAuthorities().size() < 1)) {
            return Collections.EMPTY_LIST;
        }
        Collection<? extends GrantedAuthority> authorities = currentUser.getAuthorities();
        if (log.isDebugEnabled()) {
            log.debug("current user[" + currentUser.getName() + "] principal authorities:"
                    + Arrays.toString(authorities.toArray()));
        }
        return new ArrayList(authorities);
    }

    /**
     * @param granted
     * @param ifNotGranted
     */
    public static final boolean ifNotGranted(Collection<GrantedAuthority> granted, String ifNotGranted) {
        if (StringUtils.hasText(ifNotGranted)) {
            Set<GrantedAuthority> grantedCopy = SpringSecurityAuthorizeUtils.removeAll(granted,
                    SpringSecurityAuthorizeUtils.parseAuthoritiesString(ifNotGranted));
            if (!grantedCopy.isEmpty()
                    && !granted.containsAll(SpringSecurityAuthorizeUtils.parseAuthoritiesString(ifNotGranted))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Ya sea para incluir todos los privilegios
     *
     * @param granted      Necesidad de comprobar permisos
     * @param ifAllGranted La cadena correspondiente tiene comas, en medio de \ t \ n \ r \ f símbolos, se eliminarán
     * @return Permisos Si los permisos y la cadena correspondiente para comprobar definen exactamente, devuelve true, false
     */
    public static final boolean ifAllGranted(Collection<GrantedAuthority> granted, String ifAllGranted) {
        if (StringUtils.hasText(ifAllGranted)) {
            if (granted.containsAll(SpringSecurityAuthorizeUtils.parseAuthoritiesString(ifAllGranted))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Un privilegio contiene cualquiera de los
     *
     * @param granted      Necesidad de comprobar permisos
     * @param ifAnyGranted La cadena correspondiente tiene comas, en medio de \ t \ n \ r \ f símbolos, se eliminarán
     * @return Si usted necesita para comprobar los permisos aparecen en la cadena correspondiente, devuelve true, false
     */
    public static final boolean ifAnyGranted(Collection<GrantedAuthority> granted, String ifAnyGranted) {
        if (StringUtils.hasText(ifAnyGranted)) {
            Set<GrantedAuthority> grantedCopy = SpringSecurityAuthorizeUtils.retainAll(granted,
                    SpringSecurityAuthorizeUtils.parseAuthoritiesString(ifAnyGranted));
            if (!grantedCopy.isEmpty()) {
                return true;
            }
        }
        return false;
    }

}