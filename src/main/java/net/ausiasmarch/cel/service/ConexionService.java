package net.ausiasmarch.cel.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
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
    private EmailService emailService;

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
    @Transactional
    public ConexionEntity create(ConexionEntity conexionEntity) {
        // Verificar permisos de administrador
        validateAdminPermissions();
    
        // Validar y obtener el inmueble
        InmuebleEntity inmueble = validateAndGetInmueble(conexionEntity.getInmueble().getId());
    
        // Validar y obtener la instalación
        InstalacionEntity instalacion = validateAndGetInstalacion(conexionEntity.getInstalacion().getId());
    
        // Validar y actualizar la potencia disponible
        validateAndUpdatePotenciaDisponible(instalacion, conexionEntity.getPotencia());
    
        // Calcular el porcentaje de la conexión
        double porcentaje = calculatePorcentaje(instalacion.getPotenciaTotal(), conexionEntity.getPotencia());
        conexionEntity.setPorcentaje(porcentaje);
        
        // Asignar el inmueble y la instalación a la conexión
        conexionEntity.setInmueble(inmueble);
        conexionEntity.setInstalacion(instalacion);
    
        // Guardar la conexión
        ConexionEntity nuevaConexion = oConexionRepository.save(conexionEntity);
    
        // Guardar la instalación con la potencia actualizada (solo una vez)
        oInstalacionRepository.save(instalacion);
    
        // **💡 NUEVA LÓGICA: Enviar correo electrónico al propietario del inmueble**
        if (inmueble.getSocio() != null && inmueble.getSocio().getEmail() != null) {
           enviarmail(nuevaConexion);
        } else {
            System.out.println("⚠️ No se pudo enviar el correo: El inmueble no tiene un propietario con email registrado.");
        }
    
        return nuevaConexion;
    }
    
    public void guardarFirma(Long idConexion, String firmaBase64) {
        ConexionEntity conexion = oConexionRepository.findById(idConexion)
            .orElseThrow(() -> new RuntimeException("Conexión no encontrada"));
    
        conexion.setFirma(firmaBase64);
        oConexionRepository.save(conexion);
    }

    public void enviarmail(ConexionEntity conexionEntity) {

        InmuebleEntity inmueble = validateAndGetInmueble(conexionEntity.getInmueble().getId());

        String emailPropietario = inmueble.getSocio().getEmail();
        String subject = "Nueva conexión de energía - Consentimiento y siguiente paso";
        
        String body = "Estimado/a " + inmueble.getSocio().getNombre() + ",\n\n"
            + "Se ha realizado una nueva conexión entre su inmueble y la instalación de autoconsumo.\n"
            + "Para continuar con el proceso, por favor, haga clic en el siguiente enlace para firmar la autorización:\n\n"
            + "https://www.solarcel.online/conexion/firma/" + conexionEntity.getId() + "\n\n"
            + "Una vez haya firmado, el siguiente paso será realizar la transferencia bancaria al siguiente número de cuenta:\n"
            + "IBAN: ES00 0000 0000 0000 0000 0000\n\n"
            + "El ID de la conexion es " + conexionEntity.getId() + ".\n"
            + "Por favor, incluya en el concepto de la transferencia su nombre y el ID de la conexión.\n\n"
            + "El importe sera de " + conexionEntity.getPotencia()*conexionEntity.getInstalacion().getPrecioKw() + " euros.\n"
            + "Gracias por su colaboración.\n"
            + "Atentamente,\n"
            + "Comunidad Energética Local.";
        

        // **📧 Enviar el correo**
        emailService.sendEmail(emailPropietario, subject, body);
    }

    public void reenviarmail(ConexionEntity conexionEntity) {

        InmuebleEntity inmueble = validateAndGetInmueble(conexionEntity.getInmueble().getId());

        String emailPropietario = inmueble.getSocio().getEmail();
        String subject = "Recordatorio: Firma pendiente para la conexión de energía";
        
        String body = "Estimado/a " + inmueble.getSocio().getNombre() + ",\n\n"
                + "Le recordamos que aún no ha firmado la autorización para la conexión entre su inmueble y la instalación de autoconsumo.\n\n"
                + "Se ha enviado nuevamente la solicitud de firma. Es imprescindible que complete este proceso para mantener activa la conexión.\n"
                + "Si no realiza la firma en el plazo establecido, la conexión será cancelada automáticamente.\n\n"
                + "Para continuar con el proceso, por favor, haga clic en el siguiente enlace para firmar la autorización:\n\n"
            + "https://www.solarcel.online/conexion/firma/" + conexionEntity.getId() + "\n\n"
            + "Una vez haya firmado, el siguiente paso será realizar la transferencia bancaria al siguiente número de cuenta:\n"
            + "IBAN: ES00 0000 0000 0000 0000 0000\n\n"
            + "El ID de la conexion es " + conexionEntity.getId() + ".\n"
            + "Por favor, incluya en el concepto de la transferencia su nombre y el ID de la conexión.\n\n"
            + "El importe sera de " + conexionEntity.getPotencia()*conexionEntity.getInstalacion().getPrecioKw() + " euros.\n"
            + "Gracias por su colaboración.\n"
            + "Atentamente,\n"
            + "Comunidad Energética Local.";
        

        // **📧 Enviar el correo**
        emailService.sendEmail(emailPropietario, subject, body);
    }
    
    
    
    // Método para validar permisos de administrador
    private void validateAdminPermissions() {
        if (!oAuthService.isAdmin()) {
            throw new UnauthorizedAccessException("No tienes permisos para crear una conexión");
        }
    }
    
    // Método para validar y obtener el inmueble
    private InmuebleEntity validateAndGetInmueble(Long inmuebleId) {
        return oInmuebleRepository.findById(inmuebleId)
                .orElseThrow(() -> new IllegalArgumentException("Inmueble no encontrado con ID: " + inmuebleId));
    }
    
    // Método para validar y obtener la instalación
    private InstalacionEntity validateAndGetInstalacion(Long instalacionId) {
        return oInstalacionRepository.findById(instalacionId)
                .orElseThrow(() -> new IllegalArgumentException("Instalación no encontrada con ID: " + instalacionId));
    }
    
    // Método para validar y actualizar la potencia disponible
    private void validateAndUpdatePotenciaDisponible(InstalacionEntity instalacion, Double potenciaRequerida) {
        // Verificar que la potencia requerida no sea nula o negativa
        if (potenciaRequerida == null || potenciaRequerida <= 0) {
            throw new IllegalArgumentException("La potencia requerida debe ser un valor positivo");
        }
    
        // Obtener la potencia disponible (o asignar la potencia total si es null)
        Double potenciaDisponible = instalacion.getPotenciaDisponible();
        if (potenciaDisponible == null) {
            potenciaDisponible = instalacion.getPotenciaTotal();
            instalacion.setPotenciaDisponible(potenciaDisponible);
        }
    
        // Verificar que la potencia total no sea nula o negativa
        Double potenciaTotal = instalacion.getPotenciaTotal();
        if (potenciaTotal == null || potenciaTotal <= 0) {
            throw new IllegalArgumentException("La potencia total de la instalación no puede ser nula o negativa");
        }
    
        // Verificar que haya suficiente potencia disponible
        if (potenciaDisponible < potenciaRequerida) {
            throw new IllegalArgumentException("No hay suficiente potencia disponible en la instalación");
        }
    
        // Actualizar la potencia disponible (solo una vez)
        instalacion.setPotenciaDisponible(potenciaDisponible - potenciaRequerida);
    }
    
    // Método para calcular el porcentaje de la conexión
    private double calculatePorcentaje(Double potenciaTotal, Double potenciaRequerida) {
        if (potenciaTotal == null || potenciaTotal <= 0) {
            throw new IllegalArgumentException("La potencia total de la instalación no puede ser nula o negativa");
        }
        return (potenciaRequerida / potenciaTotal);
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
            oConexionEntity.setFecha(LocalDateTime.now());
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
