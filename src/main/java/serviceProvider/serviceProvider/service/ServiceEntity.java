package serviceProvider.serviceProvider.service;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import serviceProvider.serviceProvider.provider.ProviderEntity;

@Entity
@Table(name = "service")
public class ServiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "services")
    @JsonIgnore
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
}
