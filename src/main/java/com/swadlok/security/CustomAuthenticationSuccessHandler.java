package com.swadlok.security;

import com.swadlok.entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.swadlok.entity.User.Role.*;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        String redirectURL = switch (getUserRole(authentication)) {
            case ROLE_ADMIN -> "/admin";
            case ROLE_CUSTOMER -> "/customer";
            case ROLE_MANAGER -> "/manager";
            case ROLE_DELIVERY -> "/delivery";
            default -> "/";
        };

        response.sendRedirect(request.getContextPath() + redirectURL);

    }

    private User.Role getUserRole(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(User.Role::valueOf) // Assumes role names match enum names exactly
                .filter(role -> role == ROLE_ADMIN || role == ROLE_CUSTOMER || role == ROLE_MANAGER || role == ROLE_DELIVERY)
                .findFirst()
                .orElse(null);
    }

}
