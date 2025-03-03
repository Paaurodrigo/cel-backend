package net.ausiasmarch.cel.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import net.ausiasmarch.cel.api.Inmueble;
import net.ausiasmarch.cel.entity.ConexionEntity;
import net.ausiasmarch.cel.entity.InmuebleEntity;
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
    private InmuebleRepository oInmuebleRepository;

    @Autowired
    private InstalacionRepository oInstalacionRepository;

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
public ConexionEntity create(ConexionEntity conexionEntity) {
    if (oAuthService.isAdmin()) {
        // Obtener el inmueble
        InmuebleEntity inmueble = oInmuebleRepository.findById(conexionEntity.getInmueble().getId())
                .orElseThrow(() -> new IllegalArgumentException("Inmueble no encontrado"));
        conexionEntity.setInmueble(inmueble);

        // Obtener la instalación
        InstalacionEntity instalacion = oInstalacionRepository.findById(conexionEntity.getInstalacion().getId())
                .orElseThrow(() -> new IllegalArgumentException("Instalación no encontrada"));
        System.out.println("Instalación encontrada: " + instalacion.getId());

        // Comprobamos si potenciaDisponible es null
        Double potenciaDisponible = instalacion.getPotenciaDisponible();
        System.out.println("Potencia Disponible antes de la operación: " + potenciaDisponible);

        if (potenciaDisponible == null) {
            potenciaDisponible = instalacion.getPotenciaTotal(); // Asigna la potencia total si es null
            instalacion.setPotenciaDisponible(potenciaDisponible);
            System.out.println("Potencia Disponible era null, asignado potencia total: " + potenciaDisponible);
            instalacion = oInstalacionRepository.save(instalacion); // Guardamos solo si cambia
        }

        // Verificamos si hay suficiente potencia disponible
        System.out.println("Potencia requerida para la conexión: " + conexionEntity.getPotencia());
        if (potenciaDisponible < conexionEntity.getPotencia()) {
            throw new IllegalArgumentException("No hay suficiente potencia disponible en la instalación");
        }

        // Verificar que la potencia total no sea 0
        double potenciaTotal = instalacion.getPotenciaTotal();
        System.out.println("Potencia Total de la instalación: " + potenciaTotal);
        if (potenciaTotal <= 0) {
            throw new IllegalArgumentException("La potencia total de la instalación no puede ser 0 o negativa.");
        }

        // Restamos la potencia de la conexión de la potencia disponible
        double nuevaPotenciaDisponible = potenciaDisponible - conexionEntity.getPotencia();
        instalacion.setPotenciaDisponible(nuevaPotenciaDisponible);
        System.out.println("Nueva potencia disponible después de la conexión: " + nuevaPotenciaDisponible);

        // Calculamos el porcentaje de la conexión
        double porcentaje = (conexionEntity.getPotencia() / potenciaTotal) * 100;
        conexionEntity.setPorcentaje(porcentaje);
        System.out.println("Porcentaje de la conexión: " + porcentaje);

        // Guardamos la conexión antes de modificar la instalación
        ConexionEntity nuevaConexion = oConexionRepository.save(conexionEntity);
        System.out.println("Conexión guardada con ID: " + nuevaConexion.getId());

        // Guardamos la instalación con la potencia actualizada
        instalacion = oInstalacionRepository.save(instalacion); // Vuelve a guardar después de modificar la potencia
        System.out.println("Instalación guardada con la nueva potencia disponible: " + instalacion.getPotenciaDisponible());

        // Verificar que la potencia fue actualizada correctamente
        System.out.println("Potencia disponible después de guardada: " + instalacion.getPotenciaDisponible());

        return nuevaConexion;
    } else {
        throw new UnauthorizedAccessException("No tienes permisos para crear una conexión");
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

    public Page<ConexionEntity> getPageByInstalacion(Long id_instalacion, Pageable pageable) {
        return oConexionRepository.findByInstalacion(id_instalacion, pageable);
    }
    
 
  
  

    public Long deleteAll() {
        if (oAuthService.isAdmin()) {
        oConexionRepository.deleteAll();
        return this.count();
    } else {
        throw new UnauthorizedAccessException("No tienes permisos para crear el usuario");
    }
    }

    @Override
    public Long count() {
        return oConexionRepository.count();
    }

    

    @Override
    public Long delete(Long id) {
        if (oAuthService.isAdmin()) {
            // Obtener la conexión a eliminar
            ConexionEntity oConexionEntity = get(id); // Llama a get para validar existencia
            
            // Obtener la instalación asociada a la conexión
            InstalacionEntity instalacion = oConexionEntity.getInstalacion();
            
            if (instalacion != null) {
                // Restaurar la potencia disponible de la instalación
                double nuevaPotenciaDisponible = instalacion.getPotenciaDisponible() + oConexionEntity.getPotencia();
                
                // Actualizar la potencia disponible de la instalación
                instalacion.setPotenciaDisponible(nuevaPotenciaDisponible);
                
                // Guardar los cambios en la instalación
                oInstalacionRepository.save(instalacion);
            }
            
            // Eliminar la conexión
            oConexionRepository.delete(oConexionEntity);
            
            // Retornar el id de la conexión eliminada
            return id;
        } else {
            throw new UnauthorizedAccessException("No tienes permisos para eliminar esta conexión");
        }
    }
    

    public ConexionEntity update(ConexionEntity oConexionEntity) {
        if (oAuthService.isAdmin()) {
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
    } else {
        throw new UnauthorizedAccessException("No tienes permisos para crear el usuario");
    }
    }

    public Long deleteByInmuebleAndInstalacion(Long inmuebleId, Long instalacionId) {
        if (oAuthService.isAdmin()) {
        ConexionEntity conexionRealizada = oConexionRepository.findByInmuebleIdAndInstalacionId(inmuebleId,
                instalacionId);
        if (conexionRealizada != null) {
            oConexionRepository.delete(conexionRealizada);
            return 1L;
        } else {
            throw new RuntimeException("No se encontró la la instalacion en el inmueble seleccionado.");
        }
    } else {
        throw new UnauthorizedAccessException("No tienes permisos para crear el usuario");
    }
    }

    @Override
    public ConexionEntity randomSelection() {
        return oConexionRepository.findAll()
                .get(oRandomService.getRandomInt(0, (int) (oConexionRepository.count() - 1)));
     
    }

    public Long randomCreate(Long cantidad) {
        if (oAuthService.isAdmin()) {
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
    } else {
        throw new UnauthorizedAccessException("No tienes permisos para crear el usuario");
    }
    }

   

}
