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

import net.ausiasmarch.cel.service.SocioService;
import net.ausiasmarch.cel.entity.SocioEntity;
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/socio")
public class Socio {

    @Autowired
    SocioService oSocioService;
    @GetMapping("")
    public ResponseEntity<Page<SocioEntity>> getPage(
            Pageable oPageable,
            @RequestParam  Optional<String> filter) {
        return new ResponseEntity<Page<SocioEntity>>(oSocioService.getPage(oPageable, filter), HttpStatus.OK);
    }
    @PutMapping("/new")
    public ResponseEntity<SocioEntity> create(@RequestBody SocioEntity oSocioEntity) {
        return new ResponseEntity<SocioEntity>(oSocioService.create(oSocioEntity), HttpStatus.OK);
    }
    @PutMapping("/random/{cantidad}")
    public ResponseEntity<Long> create(@PathVariable Long cantidad) {
        return new ResponseEntity<Long>(oSocioService.randomCreate(cantidad), HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<SocioEntity> getSocio(@PathVariable Long id) {
        return new ResponseEntity<SocioEntity>(oSocioService.get(id), HttpStatus.OK);
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
