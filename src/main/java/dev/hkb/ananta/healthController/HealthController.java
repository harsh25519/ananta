package dev.hkb.ananta.healthController;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class HealthController {
    @GetMapping("/ping")
    @Transactional(propagation = Propagation.NEVER)
    public String keepAlive() {
        return "OK";
    }
    @GetMapping("/")
    public RedirectView welcome() {
        return new RedirectView( "/ananta/v1/seller-products/browse");
    }
}
