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
 * A TypeCommune.
 */
@Entity
@Table(name = "type_commune")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TypeCommune implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "libelle", nullable = false)
    private String libelle;

    @OneToMany(mappedBy = "typeCommune")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "localites", "province", "typeCommune" }, allowSetters = true)
    private Set<Commune> communes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TypeCommune id(Long id) {
        this.id = id;
        return this;
    }

    public String getLibelle() {
        return this.libelle;
    }

    public TypeCommune libelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Set<Commune> getCommunes() {
        return this.communes;
    }

    public TypeCommune communes(Set<Commune> communes) {
        this.setCommunes(communes);
        return this;
    }

    public TypeCommune addCommune(Commune commune) {
        this.communes.add(commune);
        commune.setTypeCommune(this);
        return this;
    }

    public TypeCommune removeCommune(Commune commune) {
        this.communes.remove(commune);
        commune.setTypeCommune(null);
        return this;
    }

    public void setCommunes(Set<Commune> communes) {
        if (this.communes != null) {
            this.communes.forEach(i -> i.setTypeCommune(null));
        }
        if (communes != null) {
            communes.forEach(i -> i.setTypeCommune(this));
        }
        this.communes = communes;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TypeCommune)) {
            return false;
        }
        return id != null && id.equals(((TypeCommune) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TypeCommune{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            "}";
    }
}
