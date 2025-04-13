package net.ausiasmarch.cel.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import net.ausiasmarch.cel.entity.ConexionEntity;

public interface ConexionRepository extends JpaRepository<ConexionEntity, Long> {
    
     Page<ConexionEntity> findByInmuebleId(Long inmuebleId, Pageable pageable);

    @Query(value = "SELECT * FROM Conexion WHERE inmueble = :inmuebleId", nativeQuery = true)
    Page<ConexionEntity> findByInmuebleIdSQL(@Param("inmuebleId") Long inmuebleId, Pageable pageable);
    ConexionEntity findByInmuebleIdAndInstalacionId(Long inmuebleId, Long instalacionId);


    List<ConexionEntity> findByInstalacionId(Long instalacionId);

    List<ConexionEntity> findByInmuebleId(Long inmuebleId);
    //Contante
    Page<ConexionEntity> findByIdContaining(
        String filter1, Pageable oPageable);

  // Método personalizado corregido
  Page<ConexionEntity> findByInstalacionNombreContainingOrInmuebleCupsContaining(
    String instalacionNombre, String inmuebleCups, Pageable pageable);

    
        @Query("SELECT c FROM ConexionEntity c WHERE c.instalacion.id = :id_instalacion")
        Page<ConexionEntity> findByInstalacion(@Param("id_instalacion") Long id_instalacion, Pageable pageable);
        

}
