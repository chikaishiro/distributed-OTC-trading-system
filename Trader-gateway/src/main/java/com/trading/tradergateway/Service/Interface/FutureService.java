package com.trading.tradergateway.Service.Interface;

import com.trading.tradergateway.Entity.Future;

import java.util.List;

public interface FutureService {
    List findAllFutures();

    Future findFutureByFutureID(String futureID);

    List findFuturesByFutureName(String futureName);
}
