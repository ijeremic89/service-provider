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
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;

@RestController
@RequestMapping("/provider")
public class ProviderController {

    @Resource
    private ProviderServiceImpl providerService;

    @GetMapping("/{id}")
    public ResponseEntity<ProviderDto> getProvider(@PathVariable Long id) {
        return new ResponseEntity<>(providerService.findProviderById(id), HttpStatus.OK);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<ProviderDto>> getAllProviders() {
        return new ResponseEntity<>(providerService.findAllProviders(), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<ProviderDto> createProvider(@RequestBody ProviderDto providerDto) {
        return new ResponseEntity<>(providerService.createProvider(providerDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProviderDto> updateProvider(@PathVariable Long id, @RequestBody ProviderDto providerDto) {
        return new ResponseEntity<>(providerService.updateProvider(id, providerDto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProvider(@PathVariable Long id) {
        return new ResponseEntity<>(providerService.deleteProvider(id), HttpStatus.OK);
    }
}
