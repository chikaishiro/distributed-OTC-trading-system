package com.trading.tradergateway.Repository;

import com.trading.tradergateway.Entity.Broker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository("BrokerRepository")
public interface BrokerRepository extends JpaRepository<Broker, Long> {
    Broker findBrokerByBrokerName(String brokerName);
    Broker findBrokerByBrokerID(String brokerID);
}
