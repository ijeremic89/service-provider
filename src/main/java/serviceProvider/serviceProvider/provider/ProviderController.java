package serviceProvider.serviceProvider.provider;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
        return ResponseEntity.ok(providerService.findProviderById(id));
    }
}
