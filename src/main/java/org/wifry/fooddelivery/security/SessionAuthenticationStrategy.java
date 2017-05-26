package org.wifry.fooddelivery.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class SessionAuthenticationStrategy extends ConcurrentSessionControlAuthenticationStrategy {

    private SessionRegistry sessionRegistry;

    public SessionAuthenticationStrategy(SessionRegistry sessionRegistry) {
        super(sessionRegistry);
        this.sessionRegistry = sessionRegistry;
    }

    @Override
    public void onAuthentication(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {

        List<SessionInformation> sessionInformationList = sessionRegistry.getAllSessions(authentication.getPrincipal(), false);
        // Se invalidan las otras sesiones
        for (SessionInformation sessionInfo : sessionInformationList) {
            if (!request.getSession().getId().equals(sessionInfo.getSessionId())) {
                sessionInfo.expireNow();
            }

        }
        super.onAuthentication(authentication, request, response);

    }


}