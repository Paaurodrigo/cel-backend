package net.ausiasmarch.cel.service;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import net.ausiasmarch.cel.entity.InmuebleEntity;
import net.ausiasmarch.cel.entity.InstalacionEntity;
import net.ausiasmarch.cel.entity.SocioEntity;
import net.ausiasmarch.cel.exception.ResourceNotFoundException;
import net.ausiasmarch.cel.exception.UnauthorizedAccessException;
import net.ausiasmarch.cel.repository.InmuebleRepository;
import net.ausiasmarch.cel.repository.SocioRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class InmuebleService implements ServiceInterface<InmuebleEntity>{
    
 @Autowired
    InmuebleRepository oInmuebleRepository;

    @Autowired
    RandomService oRandomService;

    @Autowired
    AuthService oAuthService;

    @Autowired
    SocioRepository oSocioRepository;

    @Autowired
    public InmuebleService(InmuebleRepository oInmuebleRepository, SocioRepository oSocioRepository) {
        this.oInmuebleRepository = oInmuebleRepository;
        this.oSocioRepository = oSocioRepository; // Inyección de dependencia
    }

    private String[] arrCalles = {
       "Calle",
  "Avenida",
  "Boulevard",
  "Plaza",
  "Paseo",
  "Carretera",
  "Camino",
  "Callejón"};

        private String[] arrNombres = {
            "Calle Mayor", "Avenida de la Constitución", "Boulevard del Sol", "Plaza de España",
  "Paseo Marítimo", "Carretera de los Pinos", "Camino Real", "Callejón del Gato",
  "Calle de las Flores", "Avenida de los Olivos", "Boulevard de la Paz", "Plaza Mayor",
  "Paseo de la Libertad", "Carretera Nacional", "Camino de los Cerezos", "Callejón de los Sueños",
  "Calle del Río", "Avenida del Mar", "Boulevard de los Jardines", "Plaza del Pueblo",
  "Paseo de la Esperanza", "Carretera de la Sierra", "Camino de las Estrellas", "Callejón del Misterio"};

    public Long randomCreate(Long cantidad) {
        if (oAuthService.isAdmin()) {
        for (int i = 0; i < cantidad; i++) {
            InmuebleEntity oInmuebleEntity = new InmuebleEntity();
            oInmuebleEntity.setCups(String.valueOf(oRandomService.getRandomInt(999, 9999)));
            oInmuebleEntity.setDireccion(arrCalles[oRandomService.getRandomInt(0, arrCalles.length - 1)]);
            oInmuebleEntity.setCodigoPostal(oRandomService.getRandomInt(9999, 99999));
            oInmuebleEntity.setMunicipio(arrNombres[oRandomService.getRandomInt(0, arrNombres.length - 1)]);
            oInmuebleEntity.setRefCatas("ref1");
            oInmuebleEntity.setPotencia1(443);
            oInmuebleEntity.setPotencia2(560);
            oInmuebleEntity.setTension(445);
            oInmuebleEntity.setUso(arrNombres[oRandomService.getRandomInt(0, arrNombres.length - 1)]);
            oInmuebleEntity.setConsumoAnual(oRandomService.getRandomInt(3, 200));
            oInmuebleEntity.setHabitos(arrNombres[oRandomService.getRandomInt(0, arrNombres.length - 1)]);
            oInmuebleEntity.setIntencion("ahorrar");
            oInmuebleEntity.setRecomendacion("atopte");

            oInmuebleRepository.save(oInmuebleEntity);
        }
        return oInmuebleRepository.count();
         }else {
        throw new UnauthorizedAccessException("No tienes permisos para crear el usuario");
    }
    }
  

     public Page<InmuebleEntity> getPage(Pageable oPageable, Optional<String> filter) {

        if (filter.isPresent()) {
            return oInmuebleRepository
                    .findByCupsContainingOrDireccionContainingOrCodigopostalContainingOrMunicipioContainingOrRefcatasContainingOrPotencia1ContainingOrPotencia2ContainingOrTensionContainingOrUsoContainingOrConsumoAnualContainingOrHabitosContainingOrIntencionContaining(
                            filter.get(), filter.get(),filter.get(), filter.get(), filter.get(), filter.get(), filter.get(), filter.get(), filter.get(), filter.get(), filter.get(), filter.get(), 
                            oPageable);
        } else {
            return oInmuebleRepository.findAll(oPageable);
        }
    }


    public Page<InmuebleEntity> getPageXSocio(Pageable oPageable, Optional<String> filter, Optional<Long> id_socio) {
        if (filter.isPresent()) {
            if (id_socio.isPresent()) {
                return oInmuebleRepository
                        .findBySocioIdAndCupsContainingOrDireccionContaining(
                                id_socio.get(), filter.get(), filter.get(), oPageable);
            } else {
                throw new ResourceNotFoundException("Socio no encontrado");
            }            
        } else {
            if (id_socio.isPresent()) {
                return oInmuebleRepository.findBySocio_Id(id_socio.get(), oPageable);
            } else {
                throw new ResourceNotFoundException("Socio no encontrado");
            }
        }
    }


    public InmuebleEntity get(Long id) {
        return oInmuebleRepository.findById(id).get();
    }

    public Long count() {
        return oInmuebleRepository.count();
    }

    public Long delete(Long id) {
        if (oAuthService.isAdmin()) {
        oInmuebleRepository.deleteById(id);
        return 1L;
    }else {
        throw new UnauthorizedAccessException("No tienes permisos para crear el usuario");
    }
    }

     public InmuebleEntity create(InmuebleEntity oInmuebleEntity) {
        
                return oInmuebleRepository.save(oInmuebleEntity);
        
     }

 public InmuebleEntity update(InmuebleEntity oInmuebleEntity) {
        InmuebleEntity oInmuebleEntityFromDatabase = oInmuebleRepository.findById(oInmuebleEntity.getId()).get();

        if (oInmuebleEntity.getDireccion() != null) {
            oInmuebleEntityFromDatabase.setDireccion(oInmuebleEntity.getDireccion());
        }
        
        if (oInmuebleEntity.getCodigoPostal() != null) {
            oInmuebleEntityFromDatabase.setCodigoPostal(oInmuebleEntity.getCodigoPostal());
        }
        
        if (oInmuebleEntity.getMunicipio() != null) {
            oInmuebleEntityFromDatabase.setMunicipio(oInmuebleEntity.getMunicipio());
        }
        
        if (oInmuebleEntity.getRefCatas() != null) {
            oInmuebleEntityFromDatabase.setRefCatas(oInmuebleEntity.getRefCatas());
        }
        
        if (oInmuebleEntity.getPotencia1() != null) {
            oInmuebleEntityFromDatabase.setPotencia1(oInmuebleEntity.getPotencia1());
        }
        
        if (oInmuebleEntity.getPotencia2() != null) {
            oInmuebleEntityFromDatabase.setPotencia2(oInmuebleEntity.getPotencia2());
        }
        
        if (oInmuebleEntity.getTension() != null) {
            oInmuebleEntityFromDatabase.setTension(oInmuebleEntity.getTension());
        }
        
        if (oInmuebleEntity.getUso() != null) {
            oInmuebleEntityFromDatabase.setUso(oInmuebleEntity.getUso());
        }

        if (oInmuebleEntity.getConsumoAnual() != null) {
            oInmuebleEntityFromDatabase.setConsumoAnual(oInmuebleEntity.getConsumoAnual());
        }
        
        if (oInmuebleEntity.getHabitos() != null) {
            oInmuebleEntityFromDatabase.setHabitos(oInmuebleEntity.getHabitos());
        }
        
        if (oInmuebleEntity.getIntencion() != null) {
            oInmuebleEntityFromDatabase.setIntencion(oInmuebleEntity.getIntencion());
        }
        
        if (oInmuebleEntity.getRecomendacion() != null) {
            oInmuebleEntityFromDatabase.setRecomendacion(oInmuebleEntity.getRecomendacion());
        }
       
        return oInmuebleRepository.save(oInmuebleEntityFromDatabase);
    }

     public Long deleteAll() {
        oInmuebleRepository.deleteAll();
        return this.count();
    }


    public InmuebleEntity randomSelection() {
        return oInmuebleRepository.findById((long) oRandomService.getRandomInt(1, (int) (long) this.count())).get();
    }


    public Page<InmuebleEntity> getPageXInstalacion(Pageable oPageable, Optional<String> filter,
    Optional<Long> instalacion) {
if (filter.isPresent()) {

    if (instalacion.isPresent()) {
        return oInmuebleRepository
                .findByCupsContainingXInmueble(instalacion.get(), filter.get(),
                        oPageable);
    } else {
        return oInmuebleRepository
                .findByCupsContaining(
                        filter.get(),
                        oPageable);
    }
} else {

    if (instalacion.isPresent()) {
        return oInmuebleRepository.findAllXInstalacion(instalacion.get(), oPageable);
    } else {
        return oInmuebleRepository.findAll(oPageable);
    }
}
}

    







}
