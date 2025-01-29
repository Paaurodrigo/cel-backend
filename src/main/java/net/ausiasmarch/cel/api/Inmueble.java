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
import net.ausiasmarch.cel.service.InmuebleService;


@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
@RequestMapping("/inmueble")
public class Inmueble {

  @Autowired
  InmuebleService oInmuebleService;


     @GetMapping("")
    public ResponseEntity<Page<InmuebleEntity>> getPage(
            Pageable oPageable,
            @RequestParam  Optional<String> filter) {
        return new ResponseEntity<Page<InmuebleEntity>>(oInmuebleService.getPage(oPageable, filter), HttpStatus.OK);
    }

    /* 
    @GetMapping("xtipoinmueble/{id}")
    public ResponseEntity<Page<InmuebleEntity>> getPageXTipoInmueble(
            Pageable oPageable,
            @RequestParam Optional<String> filter,
            @PathVariable Optional<Long> id) {
        return new ResponseEntity<Page<InmuebleEntity>>(oInmuebleService.getPageXInmueble(oPageable, filter, id),
                HttpStatus.OK);
    }
*/
    @PutMapping("/new")
    public ResponseEntity<InmuebleEntity> create(@RequestBody InmuebleEntity oInmuebleEntity) {
        return new ResponseEntity<InmuebleEntity>(oInmuebleService.create(oInmuebleEntity), HttpStatus.OK);
    }
    @PutMapping("/random/{cantidad}")
    public ResponseEntity<Long> create(@PathVariable Long cantidad) {
        return new ResponseEntity<Long>(oInmuebleService.randomCreate(cantidad), HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<InmuebleEntity> getInmueble(@PathVariable Long id) {
        return new ResponseEntity<InmuebleEntity>(oInmuebleService.get(id), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<InmuebleEntity> update(@RequestBody InmuebleEntity oInmuebleEntity) {
        return new ResponseEntity<InmuebleEntity>(oInmuebleService.update(oInmuebleEntity), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> delete(@PathVariable Long id) {
        return new ResponseEntity<Long>(oInmuebleService.delete(id), HttpStatus.OK);
    }

    

}

