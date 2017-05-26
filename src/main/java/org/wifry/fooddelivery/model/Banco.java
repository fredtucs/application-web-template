package org.wifry.fooddelivery.model;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "T_BANCOS", uniqueConstraints = @UniqueConstraint(columnNames = {"estado", "nombre"}))
@SQLDelete(sql = "UPDATE T_BANCOS SET estado = -1 WHERE idBanco = ?", callable = true)
@Where(clause = "estado <> -1")
public class Banco extends BaseEntity {

    private static final long serialVersionUID = 1872214846572673893L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_BANCO")
    @SequenceGenerator(name = "SQ_BANCO", sequenceName = "SQ_BANCO_ID", allocationSize = 1, initialValue = 1)
    private Long idBanco;

    @Column(length = 300, nullable = false)
    private String nombre;

    public Banco() {
    }

    public Banco(String nombre, Estado estado) {
        super(estado);
        this.nombre = nombre;
    }

    public Long getIdBanco() {
        return idBanco;
    }

    public void setIdBanco(Long idBanco) {
        this.idBanco = idBanco;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Banco)) return false;
        Banco banco = (Banco) o;
        return Objects.equals(idBanco, banco.idBanco) &&
                Objects.equals(nombre, banco.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idBanco, nombre);
    }
}