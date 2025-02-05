package net.ausiasmarch.cel.service;

import java.util.Base64;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import net.ausiasmarch.cel.api.Inmueble;
import net.ausiasmarch.cel.entity.InmuebleEntity;
import net.ausiasmarch.cel.entity.SocioEntity;
import net.ausiasmarch.cel.entity.TipoSocioEntity;
import net.ausiasmarch.cel.repository.InmuebleRepository;
import net.ausiasmarch.cel.repository.SocioRepository;
import net.ausiasmarch.cel.repository.TipoSocioRepository;

@Service
public class SocioService implements ServiceInterface<SocioEntity>{

    @Autowired
    SocioRepository oSocioRepository;

    @Autowired
    RandomService oRandomService;

    @Autowired
    TipoSocioRepository oTipoSocioRepository;

    @Autowired
    InmuebleRepository oInmuebleRepository;

   
    

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

   
     
    private static final char[] LETTERS = {'T', 'R', 'W', 'A', 'G', 'M', 'Y', 'F', 'P', 'D', 'X', 'B',
    'N', 'J', 'Z', 'S', 'Q', 'V', 'H', 'L', 'C', 'K', 'E'};

private Random random = new Random();

public String generateValidDNI() {
// Generar un número de 8 dígitos entre 10000000 y 99999999
int dniNumber = random.nextInt(90000000) + 10000000;

// Calcular el residuo para determinar la letra (dividir entre 23)
int letterIndex = dniNumber % 23;
char dniLetter = LETTERS[letterIndex];

// Depuración: imprimir el número, el residuo y la letra calculada
System.out.println("Número: " + dniNumber + ", Residuo: " + letterIndex + ", Letra: " + dniLetter);

// Devolver el DNI completo en formato "12345678A"
return String.format("%08d%c", dniNumber, dniLetter);
}

public SocioEntity uploadFotoDNI(Long id, byte[] fotoDNI) {
    Optional<SocioEntity> oSocio = oSocioRepository.findById(id);
    if (oSocio.isPresent()) {
        SocioEntity socio = oSocio.get();
        socio.setFotoDNI(fotoDNI);  // Actualizamos el campo fotoDNI con la nueva foto
        return oSocioRepository.save(socio);  // Guardamos el socio actualizado
    }
    return null;  // Si no se encuentra el socio, retornamos null
}

    public Long randomCreate(Long cantidad) {
        for (int i = 0; i < cantidad; i++) {
            SocioEntity oSocioEntity = new SocioEntity();
            oSocioEntity.setNombre(arrNombres[oRandomService.getRandomInt(0, arrNombres.length - 1)]);
            oSocioEntity.setDNI(generateValidDNI());
            oSocioEntity.setApellido1(arrApellidos[oRandomService.getRandomInt(0, arrApellidos.length - 1)]);
            oSocioEntity.setApellido2(arrApellidos[oRandomService.getRandomInt(0, arrApellidos.length - 1)]);
            oSocioEntity.setEmail(oSocioEntity.getNombre() + oRandomService.getRandomInt(999, 9999) + "@gmail.com");
            oSocioEntity.setTelefono("6" + oRandomService.getRandomInt(999, 9999));
            oSocioEntity.setFotoDNI(null);
            oSocioEntity.setCodigopostal(oRandomService.getRandomInt(9999, 99999));
            oSocioEntity.setDireccionfiscal("Av. " + oRandomService.getRandomInt(999, 9999) + ", " + oRandomService.getRandomInt(999, 9999) + ", " + oRandomService.getRandomInt(999, 9999));
            oSocioRepository.save(oSocioEntity);
        }
        return oSocioRepository.count();
    }


    public Page<SocioEntity> getPage(Pageable oPageable, Optional<String> filter) {

        if (filter.isPresent()) {
            return oSocioRepository
                    .findByDNIContainingOrNombreContainingOrApellido1ContainingOrApellido2ContainingOrEmailContainingOrTelefonoContainingOrDireccionfiscalContainingOrCodigopostalContainingOrInmueblesContaining(
                            filter.get(), filter.get(), filter.get(), filter.get(), filter.get(), filter.get(), filter.get(), filter.get(),filter.get(),
                            oPageable);
        } else {
            return oSocioRepository.findAll(oPageable);
        }
    }

    public SocioEntity get(Long id) {
        Optional<SocioEntity> oSocio = oSocioRepository.findById(id);
        if (oSocio.isPresent()) {
            SocioEntity socio = oSocio.get();
            // Si fotoDNI no es nulo, convertirla a base64
            if (socio.getFotoDNI() != null) {
                String base64Foto = Base64.getEncoder().encodeToString(socio.getFotoDNI());
                // Ahora base64Foto es un String, que puedes enviar al frontend.
                // No necesitas usar setFotoDNI para esto, ya que solo se utiliza para almacenar el array de bytes.
                // Solo enviamos el base64Foto al frontend
            }
            return socio;
        }
        return null; // Si no se encuentra el socio, devolver null
    }

    // Para almacenar una imagen base64 en la base de datos

  public void saveFotoDNI(Long id, String base64Foto) {
    Optional<SocioEntity> oSocio = oSocioRepository.findById(id);
    if (oSocio.isPresent()) {
        SocioEntity socio = oSocio.get();
        // Convertir la cadena base64 de nuevo a byte[]
        byte[] fotoBytes = Base64.getDecoder().decode(base64Foto);
        socio.setFotoDNI(fotoBytes);  // Almacenamos los bytes en la base de datos
        oSocioRepository.save(socio);
    }
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
        if (oSocioEntity.getTelefono() != null) {
            oSocioEntityFromDatabase.setTelefono(oSocioEntity.getTelefono());
        }
        if (oSocioEntity.getFotoDNI() != null) {
            oSocioEntityFromDatabase.setFotoDNI(oSocioEntity.getFotoDNI());
        }
        if (oSocioEntity.getCodigopostal() != null) {
            oSocioEntityFromDatabase.setCodigopostal(oSocioEntity.getCodigopostal());   
        }
        if (oSocioEntity.getDireccionfiscal() != null) {
            oSocioEntityFromDatabase.setDireccionfiscal(oSocioEntity.getDireccionfiscal());
            
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

    public SocioEntity findById(Long id) {
        return oSocioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Socio no encontrado"));
    }

   
    
    public TipoSocioEntity findTipoSocioById(Long id) {
        return oTipoSocioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("TipoSocio no encontrado"));
    }

    public Page<InmuebleEntity> getInmueblesbySocio(Long id,Pageable oPageable) {
        return oInmuebleRepository.findBySocio_Id(id,oPageable);
    }


    
    

}
