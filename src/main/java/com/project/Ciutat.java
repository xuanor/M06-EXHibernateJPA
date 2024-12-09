package com.project;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ciutats")
public class Ciutat implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ciutat_id")
    private long ciutatId;

    @Column(name = "nom", nullable = false, length = 100)
    private String nom;

    @Column(name = "pais", nullable = false, length = 50)
    private String pais;

    @Column(name = "poblacio", nullable = false)
    private int poblacio;

    @OneToMany(mappedBy = "ciutat", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Ciutada> ciutadans = new HashSet<>();

    public Ciutat() {}

    public Ciutat(String nom) {
        this.nom = nom;
    }

    public Ciutat(String nom, String pais, int poblacio, Set<Ciutada> ciutadans) {
        this.nom = nom;
        this.pais = pais;
        this.poblacio = poblacio;
        if (ciutadans != null) {
            ciutadans.forEach(this::addCiutada);
        }
    }

    public long getCiutatId() {
        return ciutatId;
    }

    public void setCiutatId(long ciutatId) {
        this.ciutatId = ciutatId;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public int getPoblacio() {
        return poblacio;
    }

    public void setPoblacio(int poblacio) {
        this.poblacio = poblacio;
    }

    public Set<Ciutada> getCiutadans() {
        return ciutadans;
    }

    public void setCiutadans(Set<Ciutada> ciutadans) {
        if (ciutadans != null) {
            ciutadans.forEach(this::addCiutada);
        }
    }

    public void addCiutada(Ciutada ciutada) {
        ciutadans.add(ciutada);
        // Ensure proper bidirectional relationship setup if needed
        ciutada.setCiutat(this);
    }

    public void removeCiutada(Ciutada ciutada) {
        ciutadans.remove(ciutada);
        // Ensure proper bidirectional relationship teardown if needed
        ciutada.setCiutat(null);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (Ciutada ciutada : ciutadans) {
            if (str.length() > 0) {
                str.append(" | ");
            }
            str.append(ciutada.getName());
        }
        return this.getCiutatId() + ": " + this.getNom() + ", Pais: " + this.getPais() +
                ", Poblacio: " + this.getPoblacio() + ", Items: [" + str + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ciutat ciutat = (Ciutat) o;
        return ciutatId == ciutat.ciutatId;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(ciutatId);
    }
}
