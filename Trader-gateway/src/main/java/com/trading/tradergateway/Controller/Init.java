package com.trading.tradergateway.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@SpringBootApplication
@RestController
@CrossOrigin(origins = {"http://localhost:63342", "null"})
public class Init {
    @RequestMapping(value = "/Init",method = RequestMethod.POST)
    public String Init(HttpServletRequest request){
        String ip = request.getParameter("ip");
        String id = request.getParameter("id");
        HttpSession sess = request.getSession();
        sess.setAttribute("brokerip",ip);
        sess.setAttribute("id",id);
        return "ok";
    }
}
