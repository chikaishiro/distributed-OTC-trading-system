package com.trading.tradergateway.Controller;

import com.trading.tradergateway.Entity.Future;
import com.trading.tradergateway.Service.Interface.FutureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SpringBootApplication
@RestController
@CrossOrigin(origins={"http://localhost:63342","null"})
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
