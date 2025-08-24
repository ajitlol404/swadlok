package com.swadlok.controller;

import com.swadlok.dto.MessageDto;
import com.swadlok.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.swadlok.dto.MessageDto.MessageType.DANGER;
import static com.swadlok.utility.AppConstant.TITLE;

@Controller
@RequiredArgsConstructor
@RequestMapping("/setup")
public class SetupController {

    private final UserService userService;

    @GetMapping
    public String showSetupForm(Model model, RedirectAttributes redirectAttributes) {

        model.addAttribute(TITLE, "SL - Setup");

        if (userService.areThereAdminUsers()) {
            redirectAttributes.addFlashAttribute("alertMessage", new MessageDto(DANGER, "Admin account already exists. Please sign in to continue"));
            return "redirect:/signin";
        }

        return "setup/setup";
    }

}
