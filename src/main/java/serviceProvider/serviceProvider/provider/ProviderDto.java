package serviceProvider.serviceProvider.provider;

import java.util.Set;
import java.util.stream.Collectors;

import serviceProvider.serviceProvider.service.ServiceDto;

public class ProviderDto {

    private Long id;
    private String name;
    private Set<ServiceDto> services;

    public ProviderDto(ProviderEntity provider) {
        this.id = provider.getId();
        this.name = provider.getName();
        this.services = provider.getServices()
                                .stream()
                                .map(service -> new ServiceDto(service.getId(), service.getDescription()))
                                .collect(Collectors.toSet());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<ServiceDto> getServices() {
        return services;
    }

    public void setServices(Set<ServiceDto> services) {
        this.services = services;
    }
}
