package serviceProvider.serviceProvider.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import serviceProvider.serviceProvider.exceptions.ProviderNotFoundException;

@Service
public class FindProvider {

    @Autowired
    private ProviderRepository providerRepository;

    public ProviderDto findProviderById(Long id) {
        return new ProviderDto(providerRepository.findById(id)
                                                 .orElseThrow(() -> new ProviderNotFoundException(id)));
    }
}
