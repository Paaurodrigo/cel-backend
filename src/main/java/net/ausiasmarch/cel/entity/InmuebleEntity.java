package net.ausiasmarch.cel.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


    @Entity
@Table(name = "inmueble")
public class InmuebleEntity {

    @Id
    @Column(nullable = false, unique = true)
    private Long cups;

    @NotNull
    @Size(min = 3, max = 255)
    private String direccion;

    public InmuebleEntity() {
    }
    public InmuebleEntity(Long cups, String direccion) {
        this.cups = cups;
        this.direccion = direccion;
    }

    public Long getCups() {
        return cups;
    }
    public void setCups(Long cups) {
        this.cups = cups;
    }
    public String getDireccion() {
        return direccion;
    }
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

   
}
