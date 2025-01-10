package net.ausiasmarch.cel.service;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import net.ausiasmarch.cel.entity.InmuebleEntity;
import net.ausiasmarch.cel.repository.InmuebleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class InmuebleService implements ServiceInterface<InmuebleEntity>{
    
 @Autowired
    InmuebleRepository oInmuebleRepository;

    @Autowired
    RandomService oRandomService;

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

        for (int i = 0; i < cantidad; i++) {
            InmuebleEntity oInmuebleEntity = new InmuebleEntity();
            oInmuebleEntity.setCups(Long.valueOf(oRandomService.getRandomInt(999, 9999)));
            oInmuebleEntity.setDireccion(arrCalles[oRandomService.getRandomInt(0, arrCalles.length - 1)] + arrNombres[oRandomService.getRandomInt(0, arrNombres.length - 1)] + " " + oRandomService.getRandomInt(1, 130));
            oInmuebleRepository.save(oInmuebleEntity);
        }
        return oInmuebleRepository.count();
    }

     public Page<InmuebleEntity> getPage(Pageable oPageable, Optional<String> filter) {

        if (filter.isPresent()) {
            return oInmuebleRepository
                    .findByCupsContainingOrDireccionContaining(
                            filter.get(), filter.get(),
                            oPageable);
        } else {
            return oInmuebleRepository.findAll(oPageable);
        }
    }

    public InmuebleEntity get(Long id) {
        return oInmuebleRepository.findById(id).get();
    }

    public Long count() {
        return oInmuebleRepository.count();
    }

    public Long delete(Long id) {
        oInmuebleRepository.deleteById(id);
        return 1L;
    }

    public InmuebleEntity create(InmuebleEntity oInmuebleEntity) {
        return oInmuebleRepository.save(oInmuebleEntity);
    }


 public InmuebleEntity update(InmuebleEntity oInmuebleEntity) {
        InmuebleEntity oInmuebleEntityFromDatabase = oInmuebleRepository.findByCups(oInmuebleEntity.getCups()).get();
        
        if (oInmuebleEntity.getDireccion() != null) {
            oInmuebleEntityFromDatabase.setDireccion(oInmuebleEntity.getDireccion());
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



}
