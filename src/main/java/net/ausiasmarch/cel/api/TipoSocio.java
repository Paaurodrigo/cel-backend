package net.ausiasmarch.cel.api;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.ausiasmarch.cel.entity.TipoSocioEntity;
import net.ausiasmarch.cel.service.TipoSocioService;


@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
@RestController
@RequestMapping("/tipoSocio")
public class TipoSocio {
    
 @Autowired
    TipoSocioService oTipoSocioService;

     @GetMapping("")
    public ResponseEntity<Page<TipoSocioEntity>> getPage(
            Pageable oPageable,
            @RequestParam  Optional<String> filter) {
        return new ResponseEntity<Page<TipoSocioEntity>>(oTipoSocioService.getPage(oPageable, filter), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoSocioEntity> getTipoSocioById(@PathVariable(value = "id") Long id) {
        return ResponseEntity.ok(oTipoSocioService.get(id));
    }


}
