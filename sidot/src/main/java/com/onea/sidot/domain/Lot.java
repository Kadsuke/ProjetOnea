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
 * A Lot.
 */
@Table("lot")
public class Lot implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @NotNull(message = "must not be null")
    @Column("libelle")
    private String libelle;

    @Transient
    @JsonIgnoreProperties(value = { "lot" }, allowSetters = true)
    private Set<Parcelle> parcelles = new HashSet<>();

    @JsonIgnoreProperties(value = { "lots", "secteur" }, allowSetters = true)
    @Transient
    private Section section;

    @Column("section_id")
    private Long sectionId;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Lot id(Long id) {
        this.id = id;
        return this;
    }

    public String getLibelle() {
        return this.libelle;
    }

    public Lot libelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Set<Parcelle> getParcelles() {
        return this.parcelles;
    }

    public Lot parcelles(Set<Parcelle> parcelles) {
        this.setParcelles(parcelles);
        return this;
    }

    public Lot addParcelle(Parcelle parcelle) {
        this.parcelles.add(parcelle);
        parcelle.setLot(this);
        return this;
    }

    public Lot removeParcelle(Parcelle parcelle) {
        this.parcelles.remove(parcelle);
        parcelle.setLot(null);
        return this;
    }

    public void setParcelles(Set<Parcelle> parcelles) {
        if (this.parcelles != null) {
            this.parcelles.forEach(i -> i.setLot(null));
        }
        if (parcelles != null) {
            parcelles.forEach(i -> i.setLot(this));
        }
        this.parcelles = parcelles;
    }

    public Section getSection() {
        return this.section;
    }

    public Lot section(Section section) {
        this.setSection(section);
        this.sectionId = section != null ? section.getId() : null;
        return this;
    }

    public void setSection(Section section) {
        this.section = section;
        this.sectionId = section != null ? section.getId() : null;
    }

    public Long getSectionId() {
        return this.sectionId;
    }

    public void setSectionId(Long section) {
        this.sectionId = section;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Lot)) {
            return false;
        }
        return id != null && id.equals(((Lot) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Lot{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            "}";
    }
}
