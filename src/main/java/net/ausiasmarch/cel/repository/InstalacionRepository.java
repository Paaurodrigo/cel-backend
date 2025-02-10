package net.ausiasmarch.cel.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import net.ausiasmarch.cel.entity.InstalacionEntity;

public interface InstalacionRepository extends JpaRepository<InstalacionEntity, Long>  {
    
     Page<InstalacionEntity> findByNombreContaining(
            String filter2, Pageable oPageable);

     @Query(value = "SELECT i.* FROM instalacion i, conexion c WHERE i.id = c.id_instalacion and c.id_inmueble=:id_inmueble", nativeQuery = true)
    Page<InstalacionEntity> findAllXInmueble(Long id_inmueble, Pageable oPageable);

    @Query(value = "SELECT i.* FROM instalacion i, conexion c WHERE c.id = c.id_instalacion and c.id_inmueble=:id_inmueble and (i.nombre LIKE %:strFilter%)", nativeQuery = true)
    Page<InstalacionEntity> findByNombreContainingXInmueble(Long id_inmueble,String strFilter, Pageable oPageable);


}
