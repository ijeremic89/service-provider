package serviceProvider.serviceProvider.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/provider")
public class ProviderController {

    @Autowired
    private FindProvider findProvider;

    @GetMapping("/{providerId}")
    public ProviderDto getProvider(@PathVariable Long providerId)  {
        return findProvider.findProviderById(providerId);
    }
}
