package net.ausiasmarch.cel.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import net.ausiasmarch.cel.entity.InstalacionEntity;

public interface InstalacionRepository extends JpaRepository<InstalacionEntity, Long>  {
    
     Page<InstalacionEntity> findByNombreContainingOrCauContaining(
            String filter2, String filter3, Pageable oPageable);

            @Query(value = "SELECT i.* FROM instalacion i, conexion c WHERE i.id = c.instalacion and c.inmueble=:inmueble", nativeQuery = true)
            Page<InstalacionEntity> findAllXInmueble(Long inmueble, Pageable oPageable);
            
            @Query(value = "SELECT i.* FROM instalacion i, conexion c WHERE i.id = c.instalacion and c.inmueble=:inmueble and (i.nombre LIKE %:strFilter%)", nativeQuery = true)
            Page<InstalacionEntity> findByNombreContainingXInmueble(Long inmueble,String strFilter, Pageable oPageable);

            @Query("SELECT i FROM InstalacionEntity i WHERE i.potenciadisponible > 0")
            Page<InstalacionEntity> findDisponibles(Pageable pageable);
            
            

            Page<InstalacionEntity> findByPotenciadisponibleGreaterThan(Double potenciadisponible, Pageable pageable);

Page<InstalacionEntity> findByPotenciaDisponibleGreaterThanAndNombreContainingIgnoreCase(
        Double potenciadisponible, String nombre, Pageable pageable);


}
