package net.ausiasmarch.cel.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import net.ausiasmarch.cel.api.Inmueble;
import net.ausiasmarch.cel.entity.ConexionEntity;
import net.ausiasmarch.cel.entity.InstalacionEntity;
import net.ausiasmarch.cel.exception.ResourceNotFoundException;
import net.ausiasmarch.cel.exception.UnauthorizedAccessException;
import net.ausiasmarch.cel.repository.ConexionRepository;
import net.ausiasmarch.cel.repository.InmuebleRepository;
import net.ausiasmarch.cel.repository.InstalacionRepository;
import net.ausiasmarch.cel.repository.SocioRepository;

@Service
public class ConexionService implements ServiceInterface<ConexionEntity> {

    @Autowired
    ConexionRepository oConexionRepository;

    @Autowired
    private InmuebleRepository InmuebleRepository;

    @Autowired
    private InstalacionRepository InstalacionRepository;

    @Autowired
    AuthService oAuthService;

    @Autowired
    InstalacionService oInstalacionService;

    @Autowired
    InmuebleService oInmuebleService;

    @Autowired
    RandomService oRandomService;
   

    private String[] arrFechas = {
        "14-05-2013", "23-07-2015", "05-09-2018", "19-11-2020", "08-06-2014",
        "30-12-2016", "22-03-2019", "17-10-2021", "09-01-2012", "25-08-2017",
        "14-05-2013", "06-04-2015", "11-09-2016", "02-02-2018", "28-07-2020",
        "31-10-2022"
    };
    


    @Override
    public ConexionEntity create(ConexionEntity ConexionEntity) {
    if (oAuthService.isAdmin()) {
        ConexionEntity.setInmueble(InmuebleRepository.findById(ConexionEntity.getInmueble().getId()).get());
        ConexionEntity.setInstalacion(InstalacionRepository.findById(ConexionEntity.getInstalacion().getId()).get());
        return oConexionRepository.save(ConexionEntity);
    } else {
        throw new UnauthorizedAccessException("No tienes permisos para crear el usuario");
    }
    }


    // Delete
   

    public ConexionEntity findById(Long id) {
        return oConexionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Istalacion no encontrada con id: " + id));
    }

    @Override
    public ConexionEntity get(Long id) {
        return oConexionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Conexion con ID " + id + " no encontrado."));
    }

 


    @Override
    public Page<ConexionEntity> getPage(Pageable oPageable, Optional<String> filter) {
        if (filter.isPresent()) {
            return oConexionRepository.findByInstalacionNombreContainingOrInmuebleCupsContaining(
                filter.get(), filter.get(), oPageable);
        } else {
            return oConexionRepository.findAll(oPageable);
        }
    }

 
  
  

    public Long deleteAll() {
        oConexionRepository.deleteAll();
        return this.count();
    }

    @Override
    public Long count() {
        return oConexionRepository.count();
    }

    @Override
    public Long delete(Long id) {
        ConexionEntity oConexionEntity = get(id); // Llama a get para validar existencia
        oConexionRepository.delete(oConexionEntity);
        return id;
    }

    public ConexionEntity update(ConexionEntity oConexionEntity) {
        ConexionEntity oConexionEntityFromDatabase = oConexionRepository
                .findById(oConexionEntity.getId()).get();
        if (oConexionEntity.getFecha() != null) {
            oConexionEntityFromDatabase.setFecha(oConexionEntity.getFecha());
        }
        if (oConexionEntity.getPotencia() != 0) {
            oConexionEntityFromDatabase.setPotencia(oConexionEntityFromDatabase.getPotencia());
        }
        if (oConexionEntity.getPorcentaje() != 0) {
            oConexionEntityFromDatabase.setPorcentaje(oConexionEntityFromDatabase.getPorcentaje());
        }
        if (oConexionEntity.getInstalacion() != null) {
            oConexionEntityFromDatabase.setInstalacion(oInstalacionService.get(oConexionEntity.getInstalacion().getId()));
        }
        if (oConexionEntity.getInmueble() != null) {
            oConexionEntityFromDatabase.setInmueble(oInmuebleService.get(oConexionEntity.getInmueble().getId()));
        }
        return oConexionRepository.save(oConexionEntityFromDatabase);
    }

    public Long deleteByInmuebleAndInstalacion(Long inmuebleId, Long instalacionId) {
        ConexionEntity conexionRealizada = oConexionRepository.findByInmuebleIdAndInstalacionId(inmuebleId,
                instalacionId);
        if (conexionRealizada != null) {
            oConexionRepository.delete(conexionRealizada);
            return 1L;
        } else {
            throw new RuntimeException("No se encontró la la instalacion en el inmueble seleccionado.");
        }
    }

    @Override
    public ConexionEntity randomSelection() {
        return oConexionRepository.findAll()
                .get(oRandomService.getRandomInt(0, (int) (oConexionRepository.count() - 1)));
     
    }

    public Long randomCreate(Long cantidad) {
        for (int i = 0; i < cantidad; i++) {
            ConexionEntity oConexionEntity = new ConexionEntity();
            oConexionEntity.setPotencia(oRandomService.getRandomInt(2, 234));
            oConexionEntity.setFecha(arrFechas[oRandomService.getRandomInt(0, arrFechas.length - 1)]);
            oConexionEntity.setPorcentaje(oRandomService.getRandomInt(0, 100));
            oConexionEntity.setInstalacion(oInstalacionService.randomSelection());
            oConexionEntity.setInmueble(oInmuebleService.randomSelection());    
            oConexionRepository.save(oConexionEntity);
        }
        return oConexionRepository.count();
    }

   

}
