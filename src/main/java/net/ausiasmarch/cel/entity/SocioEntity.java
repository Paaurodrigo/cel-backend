package net.ausiasmarch.cel.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "socio")
public class SocioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 3, max = 255)
    private String DNI;

    @NotNull
    @Size(min = 3, max = 255)
    private String nombre;

    @NotNull
    @Size(min = 3, max = 255)
    private String apellido1;

    @NotNull
    @Size(min = 0, max = 255)
    private String apellido2;

    @Email
    private String email;

    @NotNull
    @Size(min = 0, max = 15)
    private String telefono;

    @Lob
    @Column(name = "fotoDNI")
    private byte[] fotoDNI;

    @NotNull
    @Size(min = 0, max = 255)
    private String direccionfiscal;

    @NotNull
    private Integer codigopostal;

    @ManyToOne(fetch = jakarta.persistence.FetchType.EAGER)
    @JoinColumn(name = "tiposocio")
    private TipoSocioEntity tiposocio;

    @OneToMany(mappedBy = "socio",fetch = FetchType.LAZY)
    private java.util.List<InmuebleEntity> inmuebles;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    public SocioEntity() {
        this.inmuebles = new java.util.ArrayList<>();
    }

    public SocioEntity(String DNI, String nombre, String apellido1, String apellido2, String email, String telefono,
            byte[] fotoDNI, String direccionfiscal, Integer codigopostal, TipoSocioEntity tiposocio) {
        this.DNI = DNI;
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.email = email;
        this.telefono = telefono;
        this.fotoDNI = fotoDNI;
        this.direccionfiscal = direccionfiscal;
        this.codigopostal = codigopostal;
        this.tiposocio = tiposocio;
    }

    public SocioEntity(Long id, String DNI, String nombre, String apellido1, String apellido2, String email,
            String telefono, byte[] fotoDNI, String direccionfiscal, Integer codigopostal, TipoSocioEntity tiposocio) {
        this.id = id;
        this.DNI = DNI;
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.email = email;
        this.telefono = telefono;
        this.fotoDNI = fotoDNI;
        this.direccionfiscal = direccionfiscal;
        this.codigopostal = codigopostal;
        this.tiposocio = tiposocio;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDNI() {
        return DNI;
    }

    public void setDNI(String DNI) {
        this.DNI = DNI;
    }

    public String getApellido1() {
        return apellido1;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public byte[] getFotoDNI() {
        return fotoDNI;
    }

    public void setFotoDNI(byte[] fotoDNI) {
        this.fotoDNI = fotoDNI;
    }

    public String getDireccionfiscal() {
        return direccionfiscal;
    }

    public void setDireccionfiscal(String direccionfiscal) {
        this.direccionfiscal = direccionfiscal;
    }

    public Integer getCodigopostal() {
        return codigopostal;
    }

    public void setCodigopostal(Integer codigopostal) {
        this.codigopostal = codigopostal;
    }

    public TipoSocioEntity getTiposocio() {
        return tiposocio;
    }   

    public void setTiposocio(TipoSocioEntity tiposocio) {
        this.tiposocio = tiposocio;
    }

    public int getInmuebles() {
        return inmuebles.size();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
