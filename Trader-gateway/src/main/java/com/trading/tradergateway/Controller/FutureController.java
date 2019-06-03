package com.trading.tradergateway.Controller;

import com.trading.tradergateway.Entity.Future;
import com.trading.tradergateway.Service.Interface.FutureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SpringBootApplication
@RestController
@RequestMapping("/future")
public class FutureController {
    private FutureService futureService;

    @Autowired
    public FutureController(FutureService futureService) {
        this.futureService = futureService;
    }

    @GetMapping("/all")
    public List getAllFutures() {
        return futureService.findAllFutures();
    }

    @GetMapping("/id")
    public Future getFutureByFutureID(@RequestParam("futureId")String futureID) {
        return futureService.findFutureByFutureID(futureID);
    }

    @GetMapping("/name")
    public List getFuturesByFutureName(@RequestParam("futureName")String futureName) {
        return futureService.findFuturesByFutureName(futureName);
    }
}
