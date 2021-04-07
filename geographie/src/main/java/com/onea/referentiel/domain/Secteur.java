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
 * A Secteur.
 */
@Entity
@Table(name = "secteur")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Secteur implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "libelle", nullable = false)
    private String libelle;

    @OneToMany(mappedBy = "secteur")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "lots", "secteur" }, allowSetters = true)
    private Set<Section> sections = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "secteurs", "commune" }, allowSetters = true)
    private Localite localite;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Secteur id(Long id) {
        this.id = id;
        return this;
    }

    public String getLibelle() {
        return this.libelle;
    }

    public Secteur libelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Set<Section> getSections() {
        return this.sections;
    }

    public Secteur sections(Set<Section> sections) {
        this.setSections(sections);
        return this;
    }

    public Secteur addSection(Section section) {
        this.sections.add(section);
        section.setSecteur(this);
        return this;
    }

    public Secteur removeSection(Section section) {
        this.sections.remove(section);
        section.setSecteur(null);
        return this;
    }

    public void setSections(Set<Section> sections) {
        if (this.sections != null) {
            this.sections.forEach(i -> i.setSecteur(null));
        }
        if (sections != null) {
            sections.forEach(i -> i.setSecteur(this));
        }
        this.sections = sections;
    }

    public Localite getLocalite() {
        return this.localite;
    }

    public Secteur localite(Localite localite) {
        this.setLocalite(localite);
        return this;
    }

    public void setLocalite(Localite localite) {
        this.localite = localite;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Secteur)) {
            return false;
        }
        return id != null && id.equals(((Secteur) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Secteur{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            "}";
    }
}
