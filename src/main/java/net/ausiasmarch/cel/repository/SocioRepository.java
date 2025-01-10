package net.ausiasmarch.cel.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import net.ausiasmarch.cel.entity.SocioEntity;


public interface SocioRepository extends JpaRepository<SocioEntity, Long> {

    Page<SocioEntity> findByDNIContainingOrNombreContainingOrApellido1ContainingOrApellido2ContainingOrEmailContainingOrTelefonoContaining(
            String filter2, String filter3, String filter4, String filter5, String filter6, String filter7, Pageable oPageable);

    
}
