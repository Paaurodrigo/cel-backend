package net.ausiasmarch.cel.service;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import net.ausiasmarch.cel.entity.TipoSocioEntity;
import net.ausiasmarch.cel.repository.TipoSocioRepository;

@Service
public class TipoSocioService {

    @Autowired
    private TipoSocioRepository oTipoSocioRepository;

    // Método para obtener un TipoSocio por ID
    public TipoSocioEntity get(Long id) {
        Optional<TipoSocioEntity> TipoSocio = oTipoSocioRepository.findById(id);
        if (TipoSocio.isPresent()) {
            return TipoSocio.get();
        } else {
            throw new EntityNotFoundException("Usuario no encontrado con ID: " + id);
        }
    }

    // Método para obtener la lista paginada de TipoSocio
    public Page<TipoSocioEntity> getPage(Pageable oPageable, Optional<String> filter) {
        if (filter.isPresent()) {
            return oTipoSocioRepository.findByDescripcionContaining(filter.get(), oPageable);
        } else {
            return oTipoSocioRepository.findAll(oPageable);
        }
    }
}