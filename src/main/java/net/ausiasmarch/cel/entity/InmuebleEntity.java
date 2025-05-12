package net.ausiasmarch.cel.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.criteria.CriteriaBuilder.In;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


    @Entity
@Table(name = "inmueble")
public class InmuebleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false, unique = true)
    private String cups;

    @NotNull
    @Size(min = 3, max = 255)
    private String direccion;

    @NotNull
    private Integer codigopostal;

    @NotNull
    @Size(min = 3, max = 255)
    private String municipio;

    @NotNull
    @Size(min = 3, max = 255)
    private String refcatas;

    @NotNull
    private Double potencia1;

    @NotNull
    private Double potencia2;

    @NotNull
    private Integer tension;

    @NotNull
    @Size(min = 3, max = 255)
    private String uso;

    @NotNull
    private Integer consumoanual;

    @NotNull
    @Size(min = 3, max = 255)
    private String habitos;


    @Size(min = 3, max = 255)
    private String intencion;

    @Size(min = 3, max = 255)
    private String recomendacion;
    @Size(min = 1, max = 255)
    private String puerta;

 @ManyToOne(fetch = jakarta.persistence.FetchType.EAGER)
    @JoinColumn(name = "id_socio")
    private SocioEntity socio;

      @OneToMany(mappedBy = "inmueble", fetch = FetchType.LAZY)
    private java.util.List<ConexionEntity> conexiones;


@Size(min = 3, max = 255)
    private String comercializadora;


    public InmuebleEntity() {
        this.conexiones = new java.util.ArrayList<>();
    }
    public InmuebleEntity(String cups, String direccion, Integer codigopostal, String municipio, String refcatas, Double potencia1, Double potencia2, Integer tension, String uso, Integer consumoanual, String habitos, String intencion, String recomendacion, SocioEntity socio, String comercializadora, String puerta) {
        this.cups = cups;
        this.direccion = direccion;
        this.codigopostal = codigopostal;
        this.municipio = municipio;
        this.refcatas = refcatas;
        this.potencia1 = potencia1;
        this.potencia2 = potencia2;
        this.tension = tension;
        this.uso = uso;
        this.consumoanual = consumoanual;
        this.habitos = habitos;
        this.intencion = intencion;
        this.recomendacion = recomendacion;
        this.socio = socio;
        this.comercializadora = comercializadora;
        this.puerta = puerta;
    }

    public InmuebleEntity(Long id,String cups, String direccion, Integer codigopostal, String municipio, String refcatas, Double potencia1, Double potencia2, Integer tension, String uso, Integer consumoanual, String habitos, String intencion, String recomendacion, SocioEntity socio, String comercializadora, String puerta) {
        this.id = id;
        this.cups = cups;
        this.direccion = direccion;
        this.codigopostal = codigopostal;
        this.municipio = municipio;
        this.refcatas = refcatas;
        this.potencia1 = potencia1;
        this.potencia2 = potencia2;
        this.tension = tension;
        this.uso = uso;
        this.consumoanual = consumoanual;
        this.habitos = habitos;
        this.intencion = intencion;
        this.recomendacion = recomendacion;
        this.socio = socio;
        this.comercializadora = comercializadora;
        this.puerta = puerta;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getCups() {
        return cups;
    }
    public void setCups(String cups) {
        this.cups = cups;
    }
    public String getDireccion() {
        return direccion;
    }
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    public Integer getCodigoPostal() {
        return codigopostal;
    }
    public void setCodigoPostal(Integer codigopostal) {
        this.codigopostal = codigopostal;
    }
    public String getMunicipio() {
        return municipio;
    }
    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }
    public String getRefCatas() {
        return refcatas;
    }
    public void setRefCatas(String refcatas) {
        this.refcatas = refcatas;
    }
    public Double getPotencia1() {
        return potencia1;
    }
    public void setPotencia1(Double potencia1) {
        this.potencia1 = potencia1;
    }
    public Double getPotencia2() {
        return potencia2;
    }
    public void setPotencia2(Double potencia2) {
        this.potencia2 = potencia2;
    }
    public Integer getTension() {
        return tension;
    }
    public void setTension(Integer tension) {
        this.tension = tension;
    }
    public String getUso() {
        return uso;
    }
    public void setUso(String uso) {
        this.uso = uso;
    }
    public Integer getConsumoAnual() {
        return consumoanual;
    }
    public void setConsumoAnual(Integer consumoanual) {
        this.consumoanual = consumoanual;
    }
    public String getHabitos() {
        return habitos;
    }
    public void setHabitos(String habitos) {
        this.habitos = habitos;
    }
    public String getIntencion() {
        return intencion;
    }
    public void setIntencion(String intencion) {
        this.intencion = intencion;
    }
    public String getRecomendacion() {
        return recomendacion;
    }
    public void setRecomendacion(String recomendacion) {
        this.recomendacion = recomendacion;
    }   

    public SocioEntity getSocio() {
        return socio;
    }

    public void setSocio(SocioEntity socio) {
        this.socio = socio;
    }


    public int getConexiones() {
        return conexiones.size();
    }

    public String getComercializadora() {
        return comercializadora;
    }

    public void setComercializadora(String comercializadora) {
        this.comercializadora = comercializadora;
    }

    public String getPuerta() {
        return puerta;
    }

    public void setPuerta(String puerta) {
        this.puerta = puerta;
    }

   
}
