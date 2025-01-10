package net.ausiasmarch.cel.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import net.ausiasmarch.cel.entity.InmuebleEntity;

public interface InmuebleRepository extends JpaRepository<InmuebleEntity, Long> {
    
     Page<InmuebleEntity> findByCupsContainingOrDireccionContaining(
            String filter2, String filter3,  Pageable oPageable);
            
                Optional<InmuebleEntity> findByCups(Long cups);
}
