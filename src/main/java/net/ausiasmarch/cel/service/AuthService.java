package net.ausiasmarch.cel.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import net.ausiasmarch.cel.bean.LogindataBean;
import net.ausiasmarch.cel.entity.SocioEntity;
import net.ausiasmarch.cel.exception.UnauthorizedAccessException;
import net.ausiasmarch.cel.repository.SocioRepository;


@Service
public class AuthService {

    @Autowired
    JWTService JWTHelper;

    @Autowired
    SocioRepository oSocioRepository;

    @Autowired
    HttpServletRequest oHttpServletRequest;

    public boolean checkLogin(LogindataBean oLogindataBean) {
        if (oSocioRepository.findByEmailAndPassword(oLogindataBean.getEmail(), oLogindataBean.getPassword())
                .isPresent()) {
            return true;
        } else {
            return false;
        }
    }
    
//OJO PREGUNTA EXAMEN TE DEVUELVE EMAIL Y NOMBRE
    private Map<String, String> getClaims(String email) {
        Map<String, String> claims = new HashMap<>();
        claims.put("email", email);
        claims.put("nombre", oSocioRepository.findByEmail(email).get().getNombre());
        claims.put("apellido1", oSocioRepository.findByEmail(email).get().getApellido1());
        claims.put("role", oSocioRepository.findByEmail(email).get().getTiposocio().getDescripcion());
        claims.put("id", oSocioRepository.findByEmail(email).get().getId().toString());
        return claims;
    };

    public String getToken(String email) {
        return JWTHelper.generateToken(getClaims(email));
    }

    public SocioEntity getSocioFromToken() {
        if (oHttpServletRequest.getAttribute("email") == null) {
            throw new UnauthorizedAccessException("No hay Socio en la sesión");
        } else {
            String email = oHttpServletRequest.getAttribute("email").toString();
            return oSocioRepository.findByEmail(email).get();
        }                
    }

    public boolean isSessionActive() {
        return oHttpServletRequest.getAttribute("email") != null;
    }

    public boolean isAdmin() {
        return this.getSocioFromToken().getTiposocio().getId() == 1L;
    }

    public boolean isMiembro() {
        return this.getSocioFromToken().getTiposocio().getId() == 2L;
    }

  

    public boolean isAdminOrMiembro() {
        return this.isAdmin() || this.isMiembro();
    }

    public boolean isMiembroWithItsOwnData(Long id) {
        SocioEntity oSocioEntity = this.getSocioFromToken();
        return this.isMiembro() && oSocioEntity.getId() == id;
    }



}
