package serviceProvider.serviceProvider.provider;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import serviceProvider.serviceProvider.provider.model.ProviderDTO;

@RestController
@RequestMapping("/provider")
public class ProviderController {

    private final ProviderServiceImpl providerService;

    public ProviderController(ProviderServiceImpl providerService) {
        this.providerService = providerService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProviderDTO> getProvider(@PathVariable Long id) {
        return new ResponseEntity<>(providerService.findProviderById(id), HttpStatus.OK);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<ProviderDTO>> getAllProviders() {
        return new ResponseEntity<>(providerService.findAllProviders(), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<ProviderDTO> createProvider(@RequestBody ProviderDTO providerDTO) {
        return new ResponseEntity<>(providerService.createProvider(providerDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProviderDTO> updateProvider(@PathVariable Long id, @RequestBody ProviderDTO providerDTO) {
        return new ResponseEntity<>(providerService.updateProvider(id, providerDTO), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProvider(@PathVariable Long id) {
        return new ResponseEntity<>(providerService.deleteProvider(id), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProviderDTO>> searchProviders(
        @RequestParam(required = false) String name,
        @RequestParam(required = false) Long serviceId) {
        return new ResponseEntity<>(providerService.findProvidersByCriteria(name, serviceId), HttpStatus.OK);
    }
}
