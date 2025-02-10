package net.ausiasmarch.cel.entity;

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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import net.ausiasmarch.cel.api.Socio;

@Entity
@Table(name = "instalacion")
public class InstalacionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 3, max = 255)
    private String nombre;

    @NotNull
    private Integer paneles;

    @NotNull
    private Double potenciapanel;

    @NotNull
    private Double potenciatotal;

    @NotNull
    private Double potenciadisponible;

    @NotNull
    private Integer preciokw;

    @OneToMany(mappedBy = "instalacion", fetch = FetchType.LAZY)
    private java.util.List<ConexionEntity> conexiones;

    public InstalacionEntity() {
        this.conexiones = new java.util.ArrayList<>();
    }

    public InstalacionEntity(String nombre,Integer paneles, Double potenciapanel, Double potenciatotal, Double potenciadisponible, Integer preciokw) {
        this.nombre = nombre;
        this.paneles = paneles;
        this.potenciapanel = potenciapanel;
        this.potenciatotal = potenciatotal;
        this.potenciadisponible = potenciadisponible;
        this.preciokw = preciokw;
        
    }

    public InstalacionEntity(Long id,String nombre, Integer paneles, Double potenciapanel, Double potenciatotal, Double potenciadisponible, Integer preciokw) {
        this.id = id;
        this.nombre = nombre;
        this.paneles = paneles;
        this.potenciapanel = potenciapanel;
        this.potenciatotal = potenciatotal;
        this.potenciadisponible = potenciadisponible;
        this.preciokw = preciokw;
      
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

    public Integer getPaneles() {
        return paneles;
    }

    public void setPaneles(Integer paneles) {
        this.paneles = paneles;
    }

    public Double getPotenciaPanel() {
        return potenciapanel;
    }

    public void setPotenciaPanel(Double potenciapanel) {
        this.potenciapanel = potenciapanel;
    }

    public Double getPotenciaTotal() {
        return potenciatotal;
    }

    public void setPotenciaTotal(Double potenciatotal) {
        this.potenciatotal = potenciatotal;
    }

    public Double getPotenciaDisponible() {
        return potenciadisponible;
    }

    public void setPotenciaDisponible(Double potenciadisponible) {
        this.potenciadisponible = potenciadisponible;
    }

    public Integer getPrecioKw() {
        return preciokw;
    }

    public void setPrecioKw(Integer preciokw) {
        this.preciokw = preciokw;
    }
  
    public int getConexiones() {
        return conexiones.size();
    }

}
