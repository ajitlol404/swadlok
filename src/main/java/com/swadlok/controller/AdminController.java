package com.swadlok.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.swadlok.utility.AppConstant.ADMIN_BASE_PATH;
import static com.swadlok.utility.AppConstant.TITLE;

@Slf4j
@Controller
@RequestMapping(ADMIN_BASE_PATH)
public class AdminController {

    @GetMapping
    public String adminController(Model model, RedirectAttributes redirectAttributes) {

        model.addAttribute(TITLE, "SL - Dashboard");

        return "admin/dashboard";
    }

}
