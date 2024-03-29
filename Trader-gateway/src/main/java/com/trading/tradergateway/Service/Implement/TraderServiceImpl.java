package com.trading.tradergateway.Service.Implement;

import com.trading.tradergateway.Entity.Trader;
import com.trading.tradergateway.JWT.JwtTokenUtil;
import com.trading.tradergateway.Repository.TraderRepository;
import com.trading.tradergateway.Service.Interface.TraderService;
import com.trading.tradergateway.Util.SnowflakeIdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("TraderService")
public class TraderServiceImpl implements TraderService {
    private TraderRepository traderRepository;
    private AuthenticationManager authenticationManager;
    private UserDetailsService userDetailsService;
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    public TraderServiceImpl(TraderRepository traderRepository, AuthenticationManager authenticationManager,
                             UserDetailsService userDetailsService, JwtTokenUtil jwtTokenUtil) {
        this.traderRepository = traderRepository;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public List findAllTraders() {
        return traderRepository.findAll();
    }

    @Override
    public Trader register(Trader trader) {
        String traderName = trader.getTraderName();
        String rawPassword = trader.getPassword();
        if(traderRepository.findTraderByTraderName(traderName) != null) {
            return null;
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encrytPassword = encoder.encode(rawPassword);
        trader.setTraderID(new SnowflakeIdWorker(0, 0).nextId());
        trader.setPassword(encrytPassword);
        trader.setRole("user");
        traderRepository.save(trader);
        return trader;
    }

    @Override
    public boolean checkDuplicate(Trader trader) {
        String traderName = trader.getTraderName();
        Trader newTrader = traderRepository.findTraderByTraderName(traderName);
        if(newTrader != null) return true;
        else return false;
    }

    @Override
    public String login(String traderName, String password) {
        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(traderName, password);
        Authentication authentication = authenticationManager.authenticate(upToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = userDetailsService.loadUserByUsername(traderName);
        return jwtTokenUtil.generateToken(userDetails);
    }

    @Override
    public String refreshToken(String oldToken) {
        String token = oldToken.substring("Bearer ".length());
        if (!jwtTokenUtil.isTokenExpired(token)) {
            return jwtTokenUtil.refreshToken(token);
        }
        return "error";
    }
}
