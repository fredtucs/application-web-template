package org.wifry.fooddelivery.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service("accessDeniedHandler")
public class AccessDeniedHandler extends AccessDeniedHandlerImpl {

	private static final Logger log = LoggerFactory.getLogger(AccessDeniedHandler.class);

	@Override
	public void handle(HttpServletRequest request,
			HttpServletResponse response, AccessDeniedException exception)
			throws IOException, ServletException {
		log.info("############### Access Denied Handler!");
		setErrorPage("/pages/static/accessDenied.html");
		super.handle(request, response, exception);
	}

}