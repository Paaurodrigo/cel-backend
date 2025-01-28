package net.ausiasmarch.cel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.ausiasmarch.cel.entity.TipoSocioEntity;


@Repository
public interface TipoSocioRepository extends JpaRepository<TipoSocioEntity, Long> {
    
}