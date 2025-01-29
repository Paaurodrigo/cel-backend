package net.ausiasmarch.cel.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import net.ausiasmarch.cel.entity.InmuebleEntity;
import net.ausiasmarch.cel.entity.SocioEntity;

public interface InmuebleRepository extends JpaRepository<InmuebleEntity, Long> {
    
     Page<InmuebleEntity> findByCupsContainingOrDireccionContainingOrCodigopostalContainingOrMunicipioContainingOrRefcatasContainingOrPotencia1ContainingOrPotencia2ContainingOrTensionContainingOrUsoContaining(
            String filter2, String filter3,String filter4, String filter5, String filter6, String filter7, String filter8, String filter9, String filter10,  Pageable oPageable);

       
            Page<InmuebleEntity> findBySocioIdAndCupsContainingOrDireccionContainingOrCodigopostalContainingOrMunicipioContainingOrRefcatasContainingOrPotencia1ContainingOrPotencia2ContainingOrTensionContainingOrUsoContaining(
               Long id_socio, String cups, String direccion, String codigopostal, 
               String municipio, String refcatas, String potencia1, String potencia2, 
               String tension, String uso, Pageable oPageable);
   
       Page<InmuebleEntity> findById_socio(SocioEntity id_socio, Pageable oPageable);


}