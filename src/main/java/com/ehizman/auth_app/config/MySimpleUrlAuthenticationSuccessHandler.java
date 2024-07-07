package com.ehizman.auth_app.config;

import com.ehizman.auth_app.user_management.AppUser;
import com.ehizman.auth_app.user_management.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MySimpleUrlAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    private Logger log = LoggerFactory.getLogger(MySimpleUrlAuthenticationSuccessHandler.class);
    @Autowired
    private UserService userService;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        handle(request, response, authentication);
        clearAuthenticationAttributes(request);
    }

    protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        String targetUrl = determineTargetUrl(authentication);

        if (response.isCommitted()) {
            log.debug(
                    "Response has already been committed. Unable to redirect to "
                            + targetUrl);
            return;
        }

        redirectStrategy.sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(final Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String authenticatedUser = userDetails.getUsername();
        AppUser user = userService.findUserByUsername(authenticatedUser);
        if (user.get_2faFactorSid() == null){
            log.info("User has not created factor");
            return "/setup-2fa";
        } else if (user.get_2faFactorSid() != null && !user.isI2FAEnabled()) {
            log.info("User has created factor but has not verified factor");
            return "/verify-factor";
        } else{
            log.info("User has verified factor");
            return "/verify-token";
        }
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }
}
