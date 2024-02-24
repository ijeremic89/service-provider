package serviceProvider.serviceProvider.service;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Join;
import serviceProvider.serviceProvider.provider.model.ProviderEntity;
import serviceProvider.serviceProvider.service.model.ServiceEntity;

public class ServiceSpecifications {

    public static Specification<ServiceEntity> hasDescription(String description) {
        return (root, query, criteriaBuilder) -> {
            if (description == null || description.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + description.toLowerCase() + "%");
        };
    }

    public static Specification<ServiceEntity> hasProvider(Long providerId) {
        return (root, query, criteriaBuilder) -> {
            if (providerId == null) {
                return criteriaBuilder.conjunction();
            }

            // Join service providers and filter by provider ID
            Join<ServiceEntity, ProviderEntity> providerJoin = root.join("providers");
            return criteriaBuilder.equal(providerJoin.get("id"), providerId);
        };
    }

    public static Specification<ServiceEntity> withDynamicQuery(String description, Long providerId) {
        return Specification.where(hasDescription(description)).and(hasProvider(providerId));
    }
}
