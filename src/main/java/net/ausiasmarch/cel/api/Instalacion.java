package net.ausiasmarch.cel.api;

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

import jakarta.persistence.EntityNotFoundException;
import net.ausiasmarch.cel.entity.InmuebleEntity;
import net.ausiasmarch.cel.entity.InstalacionEntity;
import net.ausiasmarch.cel.service.InstalacionService;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
@RequestMapping("/instalacion")
public class Instalacion {

    @Autowired
    InstalacionService oInstalacionService;

    @GetMapping("")
    public ResponseEntity<Page<InstalacionEntity>> getPage(
            Pageable oPageable,
            @RequestParam Optional<String> filter) {
        return new ResponseEntity<Page<InstalacionEntity>>(oInstalacionService.getPage(oPageable, filter),
                HttpStatus.OK);
    }

    @PutMapping("/new")
    public ResponseEntity<InstalacionEntity> create(@RequestBody InstalacionEntity oInstalacionEntity) {

        return new ResponseEntity<InstalacionEntity>(oInstalacionService.create(oInstalacionEntity), HttpStatus.OK);
    }

    @PutMapping("/random/{cantidad}")
    public ResponseEntity<Long> create(@PathVariable Long cantidad) {
        return new ResponseEntity<Long>(oInstalacionService.randomCreate(cantidad), HttpStatus.OK);
    }

    @GetMapping("/disponibles")
public ResponseEntity<Page<InstalacionEntity>> getInstalacionesDisponibles(
        Pageable oPageable,
        @RequestParam Optional<String> filter) {
    return new ResponseEntity<>(oInstalacionService.getDisponiblesPage(oPageable, filter), HttpStatus.OK);
}

    
    @GetMapping("/{id}")
    public ResponseEntity<InstalacionEntity> getInstalacion(@PathVariable Long id) {
        return new ResponseEntity<InstalacionEntity>(oInstalacionService.get(id), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<InstalacionEntity> update(@RequestBody InstalacionEntity oInstalacionEntity) {
        return new ResponseEntity<InstalacionEntity>(oInstalacionService.update(oInstalacionEntity), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, @RequestParam(required = false) Boolean force) {
        try {
            // Si force no viene en la URL, lo dejamos como false
            boolean forceDelete = force != null && force;
            Long deletedId = oInstalacionService.delete(id, forceDelete);
            return new ResponseEntity<>(deletedId, HttpStatus.OK);
        } catch (IllegalStateException e) {
            // Devuelve 409 Conflict si hay conexiones y no hay force
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (EntityNotFoundException e) {
            // Si no se encuentra la instalación
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    

    @GetMapping("/xinmueble/{id_inmueble}")
    public ResponseEntity<Page<InstalacionEntity>> getPageXtTpoapunte(
            Pageable oPageable,
            @RequestParam Optional<String> filter,
            @PathVariable Optional<Long> id_inmueble) {
        {
            return new ResponseEntity<Page<InstalacionEntity>>(
                    oInstalacionService.getPageXInmueble(oPageable, filter, id_inmueble), HttpStatus.OK);
        }
    }

    @GetMapping("/check-cau")
public ResponseEntity<Boolean> checkCauExists(@RequestParam("cau") String cau) {
    boolean existe = oInstalacionService.existsByCau(cau);
    return ResponseEntity.ok(existe);
}


}
