package serviceProvider.serviceProvider.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/provider")
public class ProviderController {

    @Autowired
    private FindProvider findProvider;

    @GetMapping("/{id}")
    public ResponseEntity<ProviderDto> getProvider(@PathVariable Long id)  {
        return ResponseEntity.ok(findProvider.findProviderById(id));
    }
}
