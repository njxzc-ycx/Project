package com.njxzc.shopsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HospitalController {

    @RequestMapping("/toSignIn")
    private String toSignIn() throws Exception{
        return "signin";
    }


}
