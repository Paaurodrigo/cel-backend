package net.ausiasmarch.cel.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import net.ausiasmarch.cel.entity.SocioEntity;


public interface SocioRepository extends JpaRepository<SocioEntity, Long> {

    Page<SocioEntity> findByDNIContainingOrNombreContainingOrApellido1ContainingOrApellido2ContainingOrEmailContaining(
            String filter2, String filter3, String filter4, String filter5, String filter6, Pageable oPageable);

            Optional<SocioEntity> findByEmail(String email);

            Optional<SocioEntity> findByEmailAndPassword(String email, String password);

            Optional<SocioEntity> findByDNI(String dni);

            boolean existsByEmail(String email);

            boolean existsByDNI(String dni);
            
            


}
