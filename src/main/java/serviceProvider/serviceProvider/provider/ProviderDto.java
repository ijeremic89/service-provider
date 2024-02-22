package serviceProvider.serviceProvider.provider;

import java.util.HashSet;
import java.util.Set;

import serviceProvider.serviceProvider.service.ServiceDto;

public class ProviderDto {

    private Long id;
    private String name;
    private Set<ServiceDto> services = new HashSet<>();

    public ProviderDto() {
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
