package serviceProvider.serviceProvider.service.model;

import java.util.HashSet;
import java.util.Set;

import serviceProvider.serviceProvider.provider.model.ProviderWithoutServicesDTO;

public class ServiceDTO {

    private Long id;
    private String description;
    private Set<ProviderWithoutServicesDTO> providers = new HashSet<>();

    public ServiceDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<ProviderWithoutServicesDTO> getProviders() {
        return providers;
    }

    public void setProviders(Set<ProviderWithoutServicesDTO> providers) {
        this.providers = providers;
    }
}
