package com.swadlok.controller;

import com.swadlok.dto.MessageDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

import static com.swadlok.dto.MessageDto.MessageType.DANGER;
import static com.swadlok.dto.MessageDto.MessageType.SUCCESS;
import static com.swadlok.utility.AppConstant.TITLE;
import static org.springframework.security.web.WebAttributes.AUTHENTICATION_EXCEPTION;

@Slf4j
@Controller
public class AuthController {

    @GetMapping("/signin")
    public String handleSignIn(Model model, @RequestParam Map<String, String> queryMap, RedirectAttributes redirectAttributes) {

        model.addAttribute(TITLE, "SL - Sign In");

        // Preventing access to signin page after successful signin
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "auth/sign-in";
        }
        return "redirect:/";
    }

    @GetMapping("/signin-fail")
    public String handleSignInFail(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        HttpSession session = request.getSession(false);
        String errorMessage = null;
        if (session != null) {
            AuthenticationException authenticationException = (AuthenticationException) session.getAttribute(AUTHENTICATION_EXCEPTION);

            if (authenticationException != null) {
                if (authenticationException.getMessage().equalsIgnoreCase("Bad credentials")) {
                    errorMessage = "Incorrect Credentials";
                } else if (authenticationException.getMessage().equalsIgnoreCase("User not found")) {
                    errorMessage = "Incorrect Credentials";
                } else {
                    errorMessage = authenticationException.getMessage();
                }
            }
        }

        redirectAttributes.addFlashAttribute("alertMessage", new MessageDto(DANGER, errorMessage));
        return "redirect:/signin";
    }

    @GetMapping("/signout-success")
    public String handleSignOut(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("alertMessage", new MessageDto(SUCCESS, "You have been logged out"));
        return "redirect:/signin";
    }

}
