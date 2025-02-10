package net.ausiasmarch.cel.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import net.ausiasmarch.cel.entity.InmuebleEntity;
import net.ausiasmarch.cel.entity.InstalacionEntity;
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
                    
      //segun contante este es el nuevo  
         @Query(value = "SELECT b.* FROM inmueble b, conexion g WHERE b.id = g.inmueble and g.instalacion=:instalacion", nativeQuery = true)
         Page<InmuebleEntity> findAllXInstalacion(Long instalacion, Pageable oPageable);

         Page<InmuebleEntity> findByCupsContaining(
            String filter2, Pageable oPageable);

        
         @Query(value = "SELECT i.* FROM istalacion i, conexion c WHERE i.id = c.instalacion and c.inmueble=:inmueble and (i.cups LIKE %:strFilter%)", nativeQuery = true)
         Page<InmuebleEntity> findByCupsContainingXInmueble(Long inmueble,String strFilter, Pageable oPageable);



           
            

            



}