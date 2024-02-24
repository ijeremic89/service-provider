package serviceProvider.serviceProvider.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import serviceProvider.serviceProvider.service.model.ServiceEntity;
import serviceProvider.serviceProvider.service.projection.ServiceDescriptionProjection;

@Repository
public interface ServiceRepository extends JpaRepository<ServiceEntity, Long>, JpaSpecificationExecutor<ServiceEntity> {

    @Query("SELECT s FROM ServiceEntity s LEFT JOIN FETCH s.providers WHERE s.id = :id")
    Optional<ServiceEntity> findByIdWithProviders(@Param("id") Long id);

    @Query("SELECT s FROM ServiceEntity s LEFT JOIN FETCH s.providers")
    List<ServiceEntity> findAllWithProviders();

    @Query("SELECT s.description AS description FROM ServiceEntity s")
    List<ServiceDescriptionProjection> findAllServiceDescriptions(); // Fetches descriptions of all services
}
