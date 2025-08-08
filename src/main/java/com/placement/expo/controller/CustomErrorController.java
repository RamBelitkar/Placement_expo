package com.placement.expo.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String errorMessage;
        int statusCode;

        if (status != null) {
            statusCode = Integer.parseInt(status.toString());
            
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                errorMessage = "Page not found";
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                errorMessage = "Access denied";
            } else {
                errorMessage = "An error occurred";
                statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
            }
        } else {
            statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
            errorMessage = "An error occurred";
        }
        
        model.addAttribute("statusCode", statusCode);
        model.addAttribute("message", errorMessage);
        
        return "error";
    }
}
