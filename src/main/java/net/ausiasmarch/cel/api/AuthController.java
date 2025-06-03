package net.ausiasmarch.cel.api;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import jakarta.persistence.EntityNotFoundException;
import net.ausiasmarch.cel.bean.LogindataBean;
import net.ausiasmarch.cel.entity.SocioEntity;
import net.ausiasmarch.cel.repository.SocioRepository;
import net.ausiasmarch.cel.service.AuthService;
import net.ausiasmarch.cel.service.EmailService;

@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthService oAuthService;

    @Autowired
   SocioRepository oSocioRepository;

    @Autowired
    EmailService emailService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LogindataBean oLogindataBean) {
        if (oAuthService.checkLogin(oLogindataBean)) {
            return ResponseEntity.ok("\"" + oAuthService.getToken(oLogindataBean.getEmail()) + "\"");
        } else {
            return ResponseEntity.status(401).body("\"" + "Error de autenticación" + "\"");
        }
    }


    @PostMapping("/recuperar-password")
    public ResponseEntity<Map<String, String>> recuperarPassword(@RequestBody String emailRaw){
    
    
        String email = emailRaw.replace("\"", ""); // quita comillas JSON
        System.out.println("Email recibido: " + email);
    
        SocioEntity oSocio = oSocioRepository.findByEmail(email)
            .orElseThrow(() -> new EntityNotFoundException("No existe ningún socio con ese email"));
    
        String enlace = "https://www.solarcel.online/reset-password/" + oSocio.getId();
        emailService.sendPasswordChangeLink(oSocio.getEmail(), enlace);
    
        return ResponseEntity.ok(Map.of("message", "Correo enviado"));

    }


    @PostMapping("/restablecer-password")
    public ResponseEntity<?> restablecerPassword(@RequestBody Map<String, String> payload) {
        String dni = payload.get("dni");
        String newPassword = payload.get("password");
        Long id = Long.parseLong(payload.get("id"));
    
        SocioEntity socio = oSocioRepository.findByDNI(dni)
            .orElseThrow(() -> new EntityNotFoundException("DNI no encontrado"));
    
        if (!socio.getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("DNI no coincide con el email");
        }
    
        socio.setPassword(newPassword); // O el hash que tú uses
        oSocioRepository.save(socio);
    
        return ResponseEntity.ok(Map.of("message", "Contraseña actualizada")
        );
    }
    

    
}
