package serviceProvider.serviceProvider.service;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/service")
public class ServiceController {

    private final ServiceServiceImpl service;


    public ServiceController(ServiceServiceImpl service) {
        this.service = service;
    }
}
