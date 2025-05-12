package net.ausiasmarch.cel.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import net.ausiasmarch.cel.api.Instalacion;
import net.ausiasmarch.cel.entity.ConexionEntity;
import net.ausiasmarch.cel.entity.InstalacionEntity;
import net.ausiasmarch.cel.repository.ConexionRepository;
import net.ausiasmarch.cel.repository.InstalacionRepository;

@Service
public class InstalacionService {

    @Autowired
    InstalacionRepository oInstalacionRepository;

    @Autowired
    ConexionRepository oConexionRepository;

    @Autowired
    RandomService oRandomService;
    private String[] arrNombres = {
            "SolarEnergía 1", "EnergíaVerde Paneles", "SolarPower 360", "EcoSol Paneles",
            "GreenSun Energia", "SunTech Instalaciones", "SolarX Proyectos", "Energía Solar Plus",
            "Futuro Solar", "EcoPaneles Fotovoltaicos" };

    public Long randomCreate(Long cantidad) {

        for (int i = 0; i < cantidad; i++) {
            InstalacionEntity oInstalacionEntity = new InstalacionEntity();
            oInstalacionEntity
                    .setNombre(String.valueOf(arrNombres[oRandomService.getRandomInt(0, arrNombres.length - 1)]));
            oInstalacionEntity.setPaneles(oRandomService.getRandomInt(999, 9999));
            oInstalacionEntity.setPotenciaPanel(345.55);
            oInstalacionEntity.setPotenciaTotal(Double.valueOf(oRandomService.getRandomInt(999, 9999)));
            oInstalacionEntity.setPotenciaDisponible(Double.valueOf(oInstalacionEntity.getPotenciaTotal()));
            oInstalacionEntity.setPrecioKw(oRandomService.getRandomInt(999, 9999));

            oInstalacionRepository.save(oInstalacionEntity);
        }
        return oInstalacionRepository.count();
    }

    public Page<InstalacionEntity> getDisponiblesPage(Pageable oPageable, Optional<String> filter) {
        if (filter.isPresent()) {
            return oInstalacionRepository
                    .findByPotenciaDisponibleGreaterThanAndNombreContainingIgnoreCase(
                            0.0, filter.get(), oPageable);
        } else {
            return oInstalacionRepository.findByPotenciadisponibleGreaterThan(0.0, oPageable);
        }
    }
    

    public Page<InstalacionEntity> getPage(Pageable oPageable, Optional<String> filter) {

        if (filter.isPresent()) {
            return oInstalacionRepository
                    .findByNombreContainingOrCauContaining(
                            filter.get(), filter.get(),
                            oPageable);
        } else {
            return oInstalacionRepository.findAll(oPageable);
        }
    }

    public InstalacionEntity get(Long id) {
        return oInstalacionRepository.findById(id).get();
    }

    public Long count() {
        return oInstalacionRepository.count();
    }

    public Long delete(Long id, boolean force) {
        // Comprobamos si la instalación existe
        InstalacionEntity instalacion = oInstalacionRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Instalación no encontrada"));

        // Buscamos las conexiones asociadas
        List<ConexionEntity> conexiones = oConexionRepository.findByInstalacionId(id);

        if (!conexiones.isEmpty()) {
            if (!force) {
                // Si hay conexiones y no se ha pedido "force", lanzamos excepción controlada
                throw new IllegalStateException("La instalación tiene conexiones asociadas. Confirma si deseas eliminar también las conexiones.");
            }
            // Si se confirma, eliminamos las conexiones primero
            oConexionRepository.deleteAll(conexiones);
        }

        // Ahora eliminamos la instalación
        oInstalacionRepository.deleteById(id);

        return id;
    }

    public InstalacionEntity create(InstalacionEntity oInstalacionEntity) {
        oInstalacionEntity.setPotenciaDisponible(oInstalacionEntity.getPotenciaTotal()); // O el valor que sea necesario
        return oInstalacionRepository.save(oInstalacionEntity);

    }

    public InstalacionEntity update(InstalacionEntity oInstalacionEntity) {
        InstalacionEntity oInstalacionEntityFromDatabase = oInstalacionRepository.findById(oInstalacionEntity.getId())
                .get();

        if (oInstalacionEntity.getNombre() != null) {
            oInstalacionEntityFromDatabase.setNombre(oInstalacionEntity.getNombre());
        }
        if (oInstalacionEntity.getPaneles() != null) {
            oInstalacionEntityFromDatabase.setPaneles(oInstalacionEntity.getPaneles());
        }
        if (oInstalacionEntity.getPotenciaPanel() != null) {
            oInstalacionEntityFromDatabase.setPotenciaPanel(oInstalacionEntity.getPotenciaPanel());
        }
        if (oInstalacionEntity.getPotenciaTotal() != null) {
            oInstalacionEntityFromDatabase.setPotenciaTotal(oInstalacionEntity.getPotenciaTotal());
        }
        if (oInstalacionEntity.getPotenciaDisponible() != null) {
            oInstalacionEntityFromDatabase.setPotenciaDisponible(oInstalacionEntity.getPotenciaDisponible());
        }
        if (oInstalacionEntity.getPrecioKw() != null) {
            oInstalacionEntityFromDatabase.setPrecioKw(oInstalacionEntity.getPrecioKw());
        }

        return oInstalacionRepository.save(oInstalacionEntityFromDatabase);
    }

    public Long deleteAll() {
        oInstalacionRepository.deleteAll();
        return this.count();
    }

    public InstalacionEntity randomSelection() {
        return oInstalacionRepository.findById((long) oRandomService.getRandomInt(1, (int) (long) this.count())).get();
    }

    public Page<InstalacionEntity> getPageXInmueble(Pageable oPageable, Optional<String> filter,
            Optional<Long> inmueble) {
        if (filter.isPresent()) {

            if (inmueble.isPresent()) {
                return oInstalacionRepository
                        .findByNombreContainingXInmueble(inmueble.get(), filter.get(),
                                oPageable);
            } else {
                return oInstalacionRepository
                        .findByNombreContainingOrCauContaining(
                                filter.get(),filter.get(),
                                oPageable);
            }
        } else {

            if (inmueble.isPresent()) {
                return oInstalacionRepository.findAllXInmueble(inmueble.get(), oPageable);
            } else {
                return oInstalacionRepository.findAll(oPageable);
            }
        }
    }

}
