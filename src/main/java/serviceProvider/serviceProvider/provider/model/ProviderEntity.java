package serviceProvider.serviceProvider.provider.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import serviceProvider.serviceProvider.service.model.ServiceEntity;

@Entity
@Table(name="provider")
public class ProviderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "provider_services",
        joinColumns = @JoinColumn(name = "provider_id"),
        inverseJoinColumns = @JoinColumn(name = "service_id"))
    private Set<ServiceEntity> services = new HashSet<>();

    public ProviderEntity() {

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

    public Set<ServiceEntity> getServices() {
        return services;
    }

    public void setServices(Set<ServiceEntity> services) {
        this.services = services;
    }

    public void addService(ServiceEntity service) {
        this.services.add(service);
        service.getProviders().add(this);
    }
    public void removeService(ServiceEntity service) {
        this.getServices().remove(service);
        service.getProviders().remove(this);
    }
    public void removeServices() {
        for (ServiceEntity service : new HashSet<>(services)) {
            removeService(service);
        }
    }
}
