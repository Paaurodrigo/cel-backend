package net.ausiasmarch.cel.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import net.ausiasmarch.cel.entity.InstalacionEntity;

public interface InstalacionRepository extends JpaRepository<InstalacionEntity, Long>  {
    
     Page<InstalacionEntity> findByNombreContaining(
            String filter2, Pageable oPageable);

}
