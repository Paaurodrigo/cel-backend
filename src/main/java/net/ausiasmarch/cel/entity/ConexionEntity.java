package net.ausiasmarch.cel.entity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import net.ausiasmarch.cel.api.Inmueble;


@Entity
@Table(name = "conexion")


public class ConexionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 3, max = 255)
    private double potencia;
    @NotNull    
    private String fecha;
    @NotNull
    @Max(value = 128)
    private double porcentaje;

 @ManyToOne(fetch = jakarta.persistence.FetchType.EAGER)
    @JoinColumn(name = "inmueble")
    private InmuebleEntity inmueble;

    @ManyToOne(fetch = jakarta.persistence.FetchType.EAGER)
    @JoinColumn(name = "instalacion")
    private InstalacionEntity instalacion;


    public ConexionEntity() {
    }

    public ConexionEntity(double potencia , String fecha, double porcentaje
    ) {
        this.potencia = potencia;
        this.fecha = fecha;
        this.porcentaje = porcentaje;
        
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getPotencia() {
        return potencia;
    }

    public void setPotencia(double potencia) {
        this.potencia = potencia;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public double getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(double porcentaje) {
        this.porcentaje = porcentaje;
    }

    public InmuebleEntity getInmueble() {
        return inmueble;
    }

    public void setInmueble(InmuebleEntity inmueble) {
        this.inmueble = inmueble;
    }

    public InstalacionEntity getInstalacion() {
        return instalacion;
    }

    public void setInstalacion(InstalacionEntity instalacion) {
        this.instalacion = instalacion;
    }


}
