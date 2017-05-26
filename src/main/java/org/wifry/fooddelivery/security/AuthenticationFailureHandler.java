package org.wifry.fooddelivery.security;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.ProviderNotFoundException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * User: AW Date: 24/02/13
 */
@Component
public class AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {

        if (exception instanceof AuthenticationCredentialsNotFoundException) {
            setDefaultFailureUrl("/login.html?error=4");
        } else if (exception instanceof SessionAuthenticationException) {
            setDefaultFailureUrl("/login.html?error=3");
        } else if (exception instanceof ProviderNotFoundException) {
            setDefaultFailureUrl("/login.html?error=2");
        } else {
            setDefaultFailureUrl("/login.html?error=1");
        }
        super.onAuthenticationFailure(request, response, exception);
    }
}
