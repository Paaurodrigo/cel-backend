package net.ausiasmarch.cel.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import net.ausiasmarch.cel.api.Instalacion;
import net.ausiasmarch.cel.entity.InstalacionEntity;
import net.ausiasmarch.cel.repository.InstalacionRepository;


@Service
public class InstalacionService {
    

 @Autowired
    InstalacionRepository oInstalacionRepository;

    @Autowired
    RandomService oRandomService;
    private String[] arrNombres = {
            "SolarEnergía 1", "EnergíaVerde Paneles", "SolarPower 360", "EcoSol Paneles",
            "GreenSun Energia", "SunTech Instalaciones", "SolarX Proyectos", "Energía Solar Plus",
            "Futuro Solar", "EcoPaneles Fotovoltaicos"};

    public Long randomCreate(Long cantidad) {

        for (int i = 0; i < cantidad; i++) {
            InstalacionEntity oInstalacionEntity = new InstalacionEntity();
            oInstalacionEntity.setNombre(String.valueOf(arrNombres[oRandomService.getRandomInt(0, arrNombres.length - 1)]));
            oInstalacionEntity.setPaneles(oRandomService.getRandomInt(999, 9999));
            oInstalacionEntity.setPotenciaPanel(345.55);
            oInstalacionEntity.setPotenciaTotal(Double.valueOf(oRandomService.getRandomInt(999, 9999)));
            oInstalacionEntity.setPotenciaDisponible(Double.valueOf(oInstalacionEntity.getPotenciaTotal()));
            oInstalacionEntity.setPrecioKw(oRandomService.getRandomInt(999, 9999));
            
            oInstalacionRepository.save(oInstalacionEntity);
        }
        return oInstalacionRepository.count();
    }

      public Page<InstalacionEntity> getPage(Pageable oPageable, Optional<String> filter) {

        if (filter.isPresent()) {
            return oInstalacionRepository
                    .findByNombreContaining(
                            filter.get(),
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

    public Long delete(Long id) {
        oInstalacionRepository.deleteById(id);
        return 1L;
    }

        public InstalacionEntity create(InstalacionEntity oInstalacionEntity) {
            oInstalacionEntity.setPotenciaDisponible(oInstalacionEntity.getPotenciaTotal()); // O el valor que sea necesario
                return oInstalacionRepository.save(oInstalacionEntity);
        
        }

 public InstalacionEntity update(InstalacionEntity oInstalacionEntity) {
        InstalacionEntity oInstalacionEntityFromDatabase = oInstalacionRepository.findById(oInstalacionEntity.getId()).get();

        if (oInstalacionEntity.getNombre() != null) {
            oInstalacionEntityFromDatabase.setNombre(oInstalacionEntity.getNombre());
        }
        if( oInstalacionEntity.getPaneles() != null) {
            oInstalacionEntityFromDatabase.setPaneles(oInstalacionEntity.getPaneles());
        }
        if( oInstalacionEntity.getPotenciaPanel() != null) {
            oInstalacionEntityFromDatabase.setPotenciaPanel(oInstalacionEntity.getPotenciaPanel());
        }
        if( oInstalacionEntity.getPotenciaTotal() != null) {
            oInstalacionEntityFromDatabase.setPotenciaTotal(oInstalacionEntity.getPotenciaTotal());
        }
        if( oInstalacionEntity.getPotenciaDisponible() != null) {
            oInstalacionEntityFromDatabase.setPotenciaDisponible(oInstalacionEntity.getPotenciaDisponible());
        }
        if( oInstalacionEntity.getPrecioKw() != null) {
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

   


}
