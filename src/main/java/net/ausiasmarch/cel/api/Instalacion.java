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
            @RequestParam  Optional<String> filter) {
        return new ResponseEntity<Page<InstalacionEntity>>(oInstalacionService.getPage(oPageable, filter), HttpStatus.OK);
    }
    @PutMapping("/new")
    public ResponseEntity<InstalacionEntity> create(@RequestBody InstalacionEntity oInstalacionEntity) {
        return new ResponseEntity<InstalacionEntity>(oInstalacionService.create(oInstalacionEntity), HttpStatus.OK);
    }
    @PutMapping("/random/{cantidad}")
    public ResponseEntity<Long> create(@PathVariable Long cantidad) {
        return new ResponseEntity<Long>(oInstalacionService.randomCreate(cantidad), HttpStatus.OK);
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
    public ResponseEntity<Long> delete(@PathVariable Long id) {
        return new ResponseEntity<Long>(oInstalacionService.delete(id), HttpStatus.OK);
    }

    

}
