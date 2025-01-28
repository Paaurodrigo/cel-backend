package net.ausiasmarch.cel.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import net.ausiasmarch.cel.entity.InmuebleEntity;

public interface InmuebleRepository extends JpaRepository<InmuebleEntity, Long> {
    
     Page<InmuebleEntity> findByCupsContainingOrDireccionContainingOrCodigopostalContainingOrMunicipioContainingOrRefcatasContainingOrPotencia1ContainingOrPotencia2ContainingOrTensionContainingOrUsoContaining(
            String filter2, String filter3,String filter4, String filter5, String filter6, String filter7, String filter8, String filter9, String filter10,  Pageable oPageable);
            
             
}