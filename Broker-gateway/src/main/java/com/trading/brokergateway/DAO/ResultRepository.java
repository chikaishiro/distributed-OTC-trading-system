package com.trading.brokergateway.DAO;

import com.trading.brokergateway.Entity.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("ResultRepository")
public interface ResultRepository extends JpaRepository<Result, String> {
    Result findResultByResultID(String resultID);
}
