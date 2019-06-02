package com.trading.tradergateway.Service.Interface;


import com.trading.tradergateway.Entity.Trader;
import java.util.List;

public interface TraderService {

    List findAllTraders();

    boolean checkDuplicate(Trader trader);

    Trader register(Trader trader);

    String login(String traderName, String password);

    String refreshToken(String oldToken);
}
