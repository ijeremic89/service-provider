package serviceProvider.serviceProvider.service.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import serviceProvider.serviceProvider.provider.model.ProviderEntity;

@Entity
@Table(name = "service")
public class ServiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "services")
    private Set<ProviderEntity> providers = new HashSet<>();

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

    public Set<ProviderEntity> getProviders() {
        return providers;
    }

    public void setProviders(Set<ProviderEntity> providers) {
        this.providers = providers;
    }

    public void addProvider(ProviderEntity provider) {
        this.providers.add(provider);
        provider.getServices().add(this);
    }

    public void removeProvider(ProviderEntity provider) {
        this.getProviders().remove(provider);
        provider.getServices().remove(this);
    }

    public void removeProviders() {
        for (ProviderEntity provider : new HashSet<>(providers)) {
            removeProvider(provider);
        }
    }
}
