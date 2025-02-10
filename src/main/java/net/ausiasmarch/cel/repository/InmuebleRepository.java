package net.ausiasmarch.cel.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import net.ausiasmarch.cel.entity.InmuebleEntity;
import net.ausiasmarch.cel.entity.SocioEntity;

public interface InmuebleRepository extends JpaRepository<InmuebleEntity, Long> {
    
     Page<InmuebleEntity> findByCupsContainingOrDireccionContainingOrCodigopostalContainingOrMunicipioContainingOrRefcatasContainingOrPotencia1ContainingOrPotencia2ContainingOrTensionContainingOrUsoContainingOrConsumoAnualContainingOrHabitosContainingOrIntencionContaining(
            String filter2, String filter3,String filter4, 
            String filter5, String filter6, String filter7, 
            String filter8, String filter9, String filter10, 
            String filter11, String filter12, String filter13, 
            
            Pageable oPageable);

   
            Page<InmuebleEntity> findBySocio_Id(Long socioId, Pageable pageable);

            @Query(value = "SELECT * FROM inmueble WHERE (cups LIKE %:strCups% OR direccion LIKE %:strDireccion%) AND id_socio=:id_socio", nativeQuery = true)
            Page<InmuebleEntity> findBySocioIdAndCupsContainingOrDireccionContaining(
                    Long id_socio, String strCups, String strDireccion, Pageable oPageable);

                    @Query("SELECT i FROM InmuebleEntity i WHERE i.socio.id = 0")
                    List<InmuebleEntity> findInmueblesSinSocio();
                    
        
    @Query(value = "SELECT b.* FROM inmueble b, conexion g WHERE b.id = g.inmueble and g.instalacion=:instalacion", nativeQuery = true)
    Page<InmuebleRepository> findAllXInstalacion(Long instalacion, Pageable oPageable);

            



}