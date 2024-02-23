package serviceProvider.serviceProvider.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {

    @Query("SELECT s FROM ServiceEntity s JOIN FETCH s.providers WHERE s.id = :id")
    Optional<ServiceEntity> findByIdWithProviders(@Param("id") Long id);

    @Query("SELECT s FROM ServiceEntity s JOIN FETCH s.providers")
    List<ServiceEntity> findAllWithProviders();
}
