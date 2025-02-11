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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import net.ausiasmarch.cel.entity.ConexionEntity;
import net.ausiasmarch.cel.repository.InmuebleRepository;
import net.ausiasmarch.cel.service.ConexionService;
import net.ausiasmarch.cel.service.InmuebleService;


@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
@RequestMapping("/conexion")
public class Conexion {
  @Autowired
  ConexionService oConexionService;

  @GetMapping("")
  public ResponseEntity<Page<ConexionEntity>> getPage(
          Pageable oPageable,
          @RequestParam  Optional<String> filter) {
      return new ResponseEntity<Page<ConexionEntity>>(oConexionService.getPage(oPageable, filter), HttpStatus.OK);
  }

  @PostMapping("/new")
    public ResponseEntity<ConexionEntity> create(@RequestBody ConexionEntity oConexionEntity) {
        return new ResponseEntity<ConexionEntity>(oConexionService.create(oConexionEntity), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConexionEntity> get(@PathVariable Long id) {
        return new ResponseEntity<ConexionEntity>(oConexionService.get(id), HttpStatus.OK);
    }

     @DeleteMapping("/{id}")
    public ResponseEntity<Long> delete(@PathVariable Long id) {
        return new ResponseEntity<Long>(oConexionService.delete(id), HttpStatus.OK);
    }




}
