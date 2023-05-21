package chargeit.chargesimulator;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/simulator")
public class SimulatorController {
    @PutMapping("/charge")
    public void charge() {
        System.out.print("OK!");
    }
}
