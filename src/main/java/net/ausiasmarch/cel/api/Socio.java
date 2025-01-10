package net.ausiasmarch.cel.api;
import java.io.IOException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
            @RequestParam("fotoDni") MultipartFile fotoDni) throws IOException {

        // Convertir el archivo a un arreglo de bytes
        byte[] fotoDniBytes = fotoDni.getBytes();

        // Crear la entidad SocioEntity
        SocioEntity oSocioEntity = new SocioEntity();
        oSocioEntity.setNombre(nombre);
        oSocioEntity.setApellido1(apellido1);
        oSocioEntity.setApellido2(apellido2);
        oSocioEntity.setEmail(email);
        oSocioEntity.setTelefono(telefono);
        oSocioEntity.setDNI(dni);
        oSocioEntity.setFotoDNI(fotoDniBytes);  // Asignamos el archivo convertido

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

    @PostMapping("/new-with-photo")
    public ResponseEntity<SocioEntity> createWithFoto(
            @RequestParam("DNI") String DNI,
            @RequestParam("nombre") String nombre,
            @RequestParam("apellido1") String apellido1,
            @RequestParam("apellido2") String apellido2,
            @RequestParam("email") String email,
            @RequestParam("telefono") String telefono,
            @RequestParam("file") MultipartFile file) {
        try {
            // Convertir el archivo MultipartFile a un arreglo de bytes
            byte[] fotoBytes = file.getBytes();

            // Crear el objeto SocioEntity con la foto incluida
            SocioEntity oSocioEntity = new SocioEntity(DNI, nombre, apellido1, apellido2, email, telefono, fotoBytes);

            // Llamar al servicio para guardar el socio
            SocioEntity savedSocio = oSocioService.create(oSocioEntity);

            return new ResponseEntity<>(savedSocio, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);  // Si ocurre un error al procesar el archivo
        }
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
