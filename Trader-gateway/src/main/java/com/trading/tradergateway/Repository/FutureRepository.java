package com.trading.tradergateway.Repository;

import com.trading.tradergateway.Entity.Future;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository("FutureRepository")
public interface FutureRepository extends JpaRepository<Future, String> {
    Future findFutureByFutureID(String futureID);
    List<Future> findFuturesByFutureName(String futureName);
}
