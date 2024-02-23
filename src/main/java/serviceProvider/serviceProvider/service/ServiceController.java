package serviceProvider.serviceProvider.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import serviceProvider.serviceProvider.service.model.ServiceDTO;

@RestController
@RequestMapping("/service")
public class ServiceController {

    private final ServiceServiceImpl service;

    public ServiceController(ServiceServiceImpl service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceDTO> getService(@PathVariable Long id) {
        return new ResponseEntity<>(service.findServiceById(id), HttpStatus.OK);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<ServiceDTO>> getAllServices() {
        return new ResponseEntity<>(service.findAllServices(), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<ServiceDTO> createService(@RequestBody ServiceDTO serviceDTO) {
        return new ResponseEntity<>(service.createService(serviceDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceDTO> updateService(@PathVariable Long id, @RequestBody ServiceDTO serviceDTO) {
        return new ResponseEntity<>(service.updateService(id, serviceDTO), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteService(@PathVariable Long id) {
        return new ResponseEntity<>(service.deleteService(id), HttpStatus.OK);
    }
}
