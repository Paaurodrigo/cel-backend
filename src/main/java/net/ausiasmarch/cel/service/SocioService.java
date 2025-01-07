package net.ausiasmarch.cel.service;

import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import net.ausiasmarch.cel.entity.SocioEntity;
import net.ausiasmarch.cel.repository.SocioRepository;

@Service
public class SocioService implements ServiceInterface<SocioEntity>{

    @Autowired
    SocioRepository oSocioRepository;

    @Autowired
    RandomService oRandomService;

    private String[] arrNombres = {
    "Daniel", "Sofia", "Javier", "Valeria", "Alberto", 
    "Elena", "Raul", "Paula", "Victor", "Irene", 
    "Oscar", "Carla", "Lucia", "Nerea", "David", 
    "Clara", "Jorge", "Natalia", "Sergio", "Eva"};

    private String[] arrApellidos = {  
    "García", "Martínez", "López", "Sánchez", "Pérez", 
    "Gómez", "Rodríguez", "Fernández", "Moreno", "Jiménez", 
    "Ruiz", "Hernández", "Díaz", "Álvarez", "Torres", 
    "Castro", "Romero", "Ortiz", "Vargas", "Iglesias", 
    "Ramos", "Gil", "Molina", "Navarro", "Domínguez", 
    "Serrano", "Vega", "Delgado", "Blanco", "Suárez"};

    public Long randomCreate(Long cantidad) {
        for (int i = 0; i < cantidad; i++) {
            SocioEntity oSocioEntity = new SocioEntity();
            oSocioEntity.setNombre(arrNombres[oRandomService.getRandomInt(0, arrNombres.length - 1)]);
            oSocioEntity.setDNI(String.format("%08d", oRandomService.getRandomInt(0, 99999999)) + "TRWAGMYFPDXBNJZSQVHLCKE".charAt(oRandomService.getRandomInt(0, 99999999) % 23));
            oSocioEntity.setApellido1(arrApellidos[oRandomService.getRandomInt(0, arrApellidos.length - 1)]);
            oSocioEntity.setApellido2(arrApellidos[oRandomService.getRandomInt(0, arrApellidos.length - 1)]);
            oSocioEntity.setEmail(oSocioEntity.getNombre()+ "email" + oRandomService.getRandomInt(999, 9999) + "@gmail.com");
            oSocioRepository.save(oSocioEntity);
        }
        return oSocioRepository.count();
    }


    public Page<SocioEntity> getPage(Pageable oPageable, Optional<String> filter) {

        if (filter.isPresent()) {
            return oSocioRepository
                    .findByDNIContainingOrNombreContainingOrApellido1ContainingOrApellido2ContainingOrEmailContaining(
                            filter.get(), filter.get(), filter.get(), filter.get(), filter.get(),
                            oPageable);
        } else {
            return oSocioRepository.findAll(oPageable);
        }
    }

    public SocioEntity get(Long id) {
        return oSocioRepository.findById(id).get();
    }

    public Long count() {
        return oSocioRepository.count();
    }

    public Long delete(Long id) {
        oSocioRepository.deleteById(id);
        return 1L;
    }

    public SocioEntity create(SocioEntity oSocioEntity) {
        return oSocioRepository.save(oSocioEntity);
    }

    public SocioEntity update(SocioEntity oSocioEntity) {
        SocioEntity oSocioEntityFromDatabase = oSocioRepository.findById(oSocioEntity.getId()).get();
        if (oSocioEntity.getDNI() != null) {
            oSocioEntityFromDatabase.setDNI(oSocioEntity.getDNI());   
        }
        if (oSocioEntity.getNombre() != null) {
            oSocioEntityFromDatabase.setNombre(oSocioEntity.getNombre());
        }
        if (oSocioEntity.getApellido1() != null) {
            oSocioEntityFromDatabase.setApellido1(oSocioEntity.getApellido1());
        }
        if (oSocioEntity.getApellido2() != null) {
            oSocioEntityFromDatabase.setApellido2(oSocioEntity.getApellido2());
        }
        if (oSocioEntity.getEmail() != null) {
            oSocioEntityFromDatabase.setEmail(oSocioEntity.getEmail());
        }
        return oSocioRepository.save(oSocioEntityFromDatabase);
    }

    public Long deleteAll() {
        oSocioRepository.deleteAll();
        return this.count();
    }

    public SocioEntity randomSelection() {
        return oSocioRepository.findById((long) oRandomService.getRandomInt(1, (int) (long) this.count())).get();
    }


}
