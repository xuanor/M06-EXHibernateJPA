package com.project;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ciutadans")
public class Ciutada implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ciutada_id")
    private long ciutadaId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ciutat_id", nullable = false)
    private Ciutat ciutat;

    public Ciutada() {}

    public Ciutada(String name) {
        this.name = name;
    }

    public long getCiutadaId() {
        return ciutadaId;
    }

    public void setCiutadaId(long ciutadaId) {
        this.ciutadaId = ciutadaId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Ciutat getCiutat() {
        return ciutat;
    }

    public void setCiutat(Ciutat ciutat) {
        this.ciutat = ciutat;
    }

    @Override
    public String toString() {
        return this.getCiutadaId() + ": " + this.getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ciutada ciutada = (Ciutada) o;
        return ciutadaId == ciutada.ciutadaId;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(ciutadaId);
    }
}
