package serviceProvider.serviceProvider.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/service")
public class ServiceController {

    private final ServiceServiceImpl service;


    public ServiceController(ServiceServiceImpl service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceDto> getService(@PathVariable Long id) {
        return new ResponseEntity<>(service.findServiceById(id), HttpStatus.OK);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<ServiceDto>> getAllServices() {
        return new ResponseEntity<>(service.findAllServices(), HttpStatus.OK);
    }
}
