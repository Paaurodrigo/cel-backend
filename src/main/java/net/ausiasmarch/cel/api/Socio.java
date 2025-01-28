package net.ausiasmarch.cel.api;
import java.io.IOException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import net.ausiasmarch.cel.service.SocioService;
import net.ausiasmarch.cel.entity.SocioEntity;
import net.ausiasmarch.cel.entity.TipoSocioEntity;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
@RequestMapping("/socio")
public class Socio {

    @Autowired
    SocioService oSocioService;



    @GetMapping("")
    public ResponseEntity<Page<SocioEntity>> getPage(
            Pageable oPageable,
            @RequestParam Optional<String> filter) {
        return new ResponseEntity<Page<SocioEntity>>(oSocioService.getPage(oPageable, filter), HttpStatus.OK);
    }

    @PutMapping("/new")
    public ResponseEntity<SocioEntity> createSocio(
            @RequestParam("nombre") String nombre,
            @RequestParam("apellido1") String apellido1,
            @RequestParam("apellido2") String apellido2,
            @RequestParam("email") String email,
            @RequestParam("telefono") String telefono,
            @RequestParam("dni") String dni,
            @RequestParam("direccionfiscal") String direccionfiscal,
            @RequestParam("codigopostal") Integer codigopostal,
            @RequestParam("tiposocio") Long tiposocioId, // Recibimos el ID del tipo de socio
            @RequestParam("fotoDni") MultipartFile fotoDni) throws IOException {
    
        // Convertir el archivo a un arreglo de bytes
        byte[] fotoDniBytes = fotoDni.getBytes();
    
        // Buscar el TiposocioEntity usando el ID recibido
        TipoSocioEntity tiposocio = oSocioService.findTipoSocioById(tiposocioId);
        // Crear la entidad SocioEntity
        SocioEntity oSocioEntity = new SocioEntity();
        oSocioEntity.setNombre(nombre);
        oSocioEntity.setApellido1(apellido1);
        oSocioEntity.setApellido2(apellido2);
        oSocioEntity.setEmail(email);
        oSocioEntity.setTelefono(telefono);
        oSocioEntity.setDNI(dni);
        oSocioEntity.setFotoDNI(fotoDniBytes);  // Asignamos el archivo convertido
        oSocioEntity.setDireccionfiscal(direccionfiscal);
        oSocioEntity.setCodigopostal(codigopostal);
        oSocioEntity.setTiposocio(tiposocio);
    
        // Guardar el socio en la base de datos
        SocioEntity savedSocio = oSocioService.create(oSocioEntity);
    
        return new ResponseEntity<>(savedSocio, HttpStatus.CREATED);
    }
    

    @PutMapping("/random/{cantidad}")
    public ResponseEntity<Long> create(@PathVariable Long cantidad) {
        return new ResponseEntity<Long>(oSocioService.randomCreate(cantidad), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SocioEntity> getSocio(@PathVariable Long id) {
        return new ResponseEntity<SocioEntity>(oSocioService.get(id), HttpStatus.OK);
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> obtenerImagen(@PathVariable Long id) {
        // Buscar el socio directamente, ya que findById lanza la excepción si no lo encuentra
        SocioEntity oSocio = oSocioService.findById(id);
        
        // Retornar la imagen como respuesta HTTP
        return ResponseEntity.ok()
            .contentType(MediaType.IMAGE_PNG)
            .body(oSocio.getFotoDNI());
    }
    @PostMapping("")
    public ResponseEntity<SocioEntity> update(@RequestBody SocioEntity oSocioEntity) {
        return new ResponseEntity<SocioEntity>(oSocioService.update(oSocioEntity), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> delete(@PathVariable Long id) {
        return new ResponseEntity<Long>(oSocioService.delete(id), HttpStatus.OK);
    }
}
