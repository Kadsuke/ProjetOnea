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
 * A Commune.
 */
@Entity
@Table(name = "commune")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Commune implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "libelle", nullable = false)
    private String libelle;

    @OneToMany(mappedBy = "commune")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "secteurs", "commune" }, allowSetters = true)
    private Set<Localite> localites = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "communes", "region" }, allowSetters = true)
    private Province province;

    @ManyToOne
    @JsonIgnoreProperties(value = { "communes" }, allowSetters = true)
    private TypeCommune typeCommune;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Commune id(Long id) {
        this.id = id;
        return this;
    }

    public String getLibelle() {
        return this.libelle;
    }

    public Commune libelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Set<Localite> getLocalites() {
        return this.localites;
    }

    public Commune localites(Set<Localite> localites) {
        this.setLocalites(localites);
        return this;
    }

    public Commune addLocalite(Localite localite) {
        this.localites.add(localite);
        localite.setCommune(this);
        return this;
    }

    public Commune removeLocalite(Localite localite) {
        this.localites.remove(localite);
        localite.setCommune(null);
        return this;
    }

    public void setLocalites(Set<Localite> localites) {
        if (this.localites != null) {
            this.localites.forEach(i -> i.setCommune(null));
        }
        if (localites != null) {
            localites.forEach(i -> i.setCommune(this));
        }
        this.localites = localites;
    }

    public Province getProvince() {
        return this.province;
    }

    public Commune province(Province province) {
        this.setProvince(province);
        return this;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    public TypeCommune getTypeCommune() {
        return this.typeCommune;
    }

    public Commune typeCommune(TypeCommune typeCommune) {
        this.setTypeCommune(typeCommune);
        return this;
    }

    public void setTypeCommune(TypeCommune typeCommune) {
        this.typeCommune = typeCommune;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Commune)) {
            return false;
        }
        return id != null && id.equals(((Commune) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Commune{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            "}";
    }
}
