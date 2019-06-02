package com.trading.tradergateway.Service.Implement;

import com.trading.tradergateway.Entity.Broker;
import com.trading.tradergateway.JWT.JwtTokenUtil;
import com.trading.tradergateway.Repository.BrokerRepository;
import com.trading.tradergateway.Service.Interface.BrokerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("brokerService")
public class BrokerServiceImpl implements BrokerService {
    private BrokerRepository brokerRepository;
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    public BrokerServiceImpl(BrokerRepository brokerRepository, JwtTokenUtil jwtTokenUtil) {
        this.brokerRepository = brokerRepository;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public List findAllBrokers() {
        List<Broker> brokers = brokerRepository.findAll();
        List result = new ArrayList();
        for (Broker broker : brokers) {
            Map map = new HashMap();
            map.put("broker_id", broker.getBrokerID());
            map.put("broker_name", broker.getBrokerName());
            result.add(map);
        }
        return result;
    }
}
