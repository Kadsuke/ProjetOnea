package com.onea.referentiel.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Localite.
 */
@Entity
@Table(name = "localite")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Localite implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "libelle", nullable = false)
    private String libelle;

    @OneToMany(mappedBy = "localite")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "sections", "localite" }, allowSetters = true)
    private Set<Secteur> secteurs = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "localites", "province", "typeCommune" }, allowSetters = true)
    private Commune commune;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Localite id(Long id) {
        this.id = id;
        return this;
    }

    public String getLibelle() {
        return this.libelle;
    }

    public Localite libelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Set<Secteur> getSecteurs() {
        return this.secteurs;
    }

    public Localite secteurs(Set<Secteur> secteurs) {
        this.setSecteurs(secteurs);
        return this;
    }

    public Localite addSecteur(Secteur secteur) {
        this.secteurs.add(secteur);
        secteur.setLocalite(this);
        return this;
    }

    public Localite removeSecteur(Secteur secteur) {
        this.secteurs.remove(secteur);
        secteur.setLocalite(null);
        return this;
    }

    public void setSecteurs(Set<Secteur> secteurs) {
        if (this.secteurs != null) {
            this.secteurs.forEach(i -> i.setLocalite(null));
        }
        if (secteurs != null) {
            secteurs.forEach(i -> i.setLocalite(this));
        }
        this.secteurs = secteurs;
    }

    public Commune getCommune() {
        return this.commune;
    }

    public Localite commune(Commune commune) {
        this.setCommune(commune);
        return this;
    }

    public void setCommune(Commune commune) {
        this.commune = commune;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Localite)) {
            return false;
        }
        return id != null && id.equals(((Localite) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Localite{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            "}";
    }
}
