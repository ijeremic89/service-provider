package serviceProvider.serviceProvider.provider;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import serviceProvider.serviceProvider.provider.model.ProviderEntity;

public interface ProviderRepository extends JpaRepository<ProviderEntity, Long>, JpaSpecificationExecutor<ProviderEntity> {

    @Query("SELECT p FROM ProviderEntity p JOIN FETCH p.services WHERE p.id = :id")
    Optional<ProviderEntity> findByIdWithServices(@Param("id") Long id);

    @Query("SELECT p FROM ProviderEntity p JOIN FETCH p.services")
    List<ProviderEntity> findAllWithServices();
}
