package serviceProvider.serviceProvider.provider;

import java.util.List;

public interface ProviderService {
    ProviderDto findProviderById(Long id);
    List<ProviderDto> findAllProviders();
    ProviderDto createProvider(ProviderDto providerDto);
    ProviderDto updateProvider(Long id, ProviderDto provider);
    String deleteProvider(Long id);
}
