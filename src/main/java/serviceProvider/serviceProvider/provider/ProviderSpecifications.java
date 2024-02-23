package serviceProvider.serviceProvider.provider;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Join;
import serviceProvider.serviceProvider.service.ServiceEntity;

public class ProviderSpecifications {
    public static Specification<ProviderEntity> hasName(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null || name.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<ProviderEntity> hasService(Long serviceId) {
        return (root, query, criteriaBuilder) -> {
            if (serviceId == null) {
                return criteriaBuilder.conjunction();
            }

            // Join provider services and filter by service ID
            Join<ProviderEntity, ServiceEntity> servicesJoin = root.join("services");
            return criteriaBuilder.equal(servicesJoin.get("id"), serviceId);
        };
    }

    public static Specification<ProviderEntity> withDynamicQuery(String name, Long serviceId) {
        return Specification.where(hasName(name)).and(hasService(serviceId));
    }
}
