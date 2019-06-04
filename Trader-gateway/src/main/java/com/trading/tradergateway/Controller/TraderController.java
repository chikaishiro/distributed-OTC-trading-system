package com.trading.tradergateway.Controller;

import com.trading.tradergateway.Entity.Trader;
import com.trading.tradergateway.Service.Interface.TraderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.List;

@SpringBootApplication
@RestController
@CrossOrigin(origins={"http://localhost:63342","null"})
@RequestMapping("/trader")
@Validated
public class TraderController {
    private TraderService traderService;
    @Autowired
    public TraderController(TraderService traderService) {
        this.traderService = traderService;
    }

    @GetMapping("/all")
    public List<Trader> getAllTraders() {
        return traderService.findAllTraders();
    }

    @PostMapping("/register")
    public String register(@RequestBody @Valid Trader trader, BindingResult bindingResult) throws AuthenticationException {
        boolean check = traderService.checkDuplicate(trader);
        if(check) return "用户已存在";
        else {
            traderService.register(trader);
            System.out.println("reg ok");
            return "用户注册成功";
        }
    }

    @PostMapping("/login")
    public String login(@RequestBody Trader trader) throws AuthenticationException {
        String token = traderService.login(trader.getTraderName(), trader.getPassword());
        System.out.println("成功" + token);
        return token;
    }

    @GetMapping("/refreshToken")
    public String refreshToken(@RequestHeader String authorization) throws AuthenticationException {
        String result = traderService.refreshToken(authorization);
        if (result.equals("error")) return "fail";
        else return result;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public String handleConstraintViolationException(ConstraintViolationException cve) {
        String errorMessage = cve.getConstraintViolations().iterator().next().getMessage();
        return errorMessage;
    }
}
