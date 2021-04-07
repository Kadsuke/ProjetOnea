package com.onea.sidot.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Commune.
 */
@Table("commune")
public class Commune implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @NotNull(message = "must not be null")
    @Column("libelle")
    private String libelle;

    @Transient
    @JsonIgnoreProperties(value = { "secteurs", "commune" }, allowSetters = true)
    private Set<Localite> localites = new HashSet<>();

    @JsonIgnoreProperties(value = { "communes", "region" }, allowSetters = true)
    @Transient
    private Province province;

    @Column("province_id")
    private Long provinceId;

    @JsonIgnoreProperties(value = { "communes" }, allowSetters = true)
    @Transient
    private TypeCommune typeCommune;

    @Column("type_commune_id")
    private Long typeCommuneId;

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
        this.provinceId = province != null ? province.getId() : null;
        return this;
    }

    public void setProvince(Province province) {
        this.province = province;
        this.provinceId = province != null ? province.getId() : null;
    }

    public Long getProvinceId() {
        return this.provinceId;
    }

    public void setProvinceId(Long province) {
        this.provinceId = province;
    }

    public TypeCommune getTypeCommune() {
        return this.typeCommune;
    }

    public Commune typeCommune(TypeCommune typeCommune) {
        this.setTypeCommune(typeCommune);
        this.typeCommuneId = typeCommune != null ? typeCommune.getId() : null;
        return this;
    }

    public void setTypeCommune(TypeCommune typeCommune) {
        this.typeCommune = typeCommune;
        this.typeCommuneId = typeCommune != null ? typeCommune.getId() : null;
    }

    public Long getTypeCommuneId() {
        return this.typeCommuneId;
    }

    public void setTypeCommuneId(Long typeCommune) {
        this.typeCommuneId = typeCommune;
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
