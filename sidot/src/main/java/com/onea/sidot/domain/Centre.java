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
 * A Centre.
 */
@Table("centre")
public class Centre implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @NotNull(message = "must not be null")
    @Column("libelle")
    private String libelle;

    @NotNull(message = "must not be null")
    @Column("responsable")
    private String responsable;

    @NotNull(message = "must not be null")
    @Column("contact")
    private String contact;

    @Transient
    @JsonIgnoreProperties(value = { "agents", "centre", "agents" }, allowSetters = true)
    private Set<Site> sites = new HashSet<>();

    @JsonIgnoreProperties(value = { "centres", "directionRegionale" }, allowSetters = true)
    @Transient
    private CentreRegroupement centreRegroupement;

    @Column("centre_regroupement_id")
    private Long centreRegroupementId;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Centre id(Long id) {
        this.id = id;
        return this;
    }

    public String getLibelle() {
        return this.libelle;
    }

    public Centre libelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getResponsable() {
        return this.responsable;
    }

    public Centre responsable(String responsable) {
        this.responsable = responsable;
        return this;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public String getContact() {
        return this.contact;
    }

    public Centre contact(String contact) {
        this.contact = contact;
        return this;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Set<Site> getSites() {
        return this.sites;
    }

    public Centre sites(Set<Site> sites) {
        this.setSites(sites);
        return this;
    }

    public Centre addSite(Site site) {
        this.sites.add(site);
        site.setCentre(this);
        return this;
    }

    public Centre removeSite(Site site) {
        this.sites.remove(site);
        site.setCentre(null);
        return this;
    }

    public void setSites(Set<Site> sites) {
        if (this.sites != null) {
            this.sites.forEach(i -> i.setCentre(null));
        }
        if (sites != null) {
            sites.forEach(i -> i.setCentre(this));
        }
        this.sites = sites;
    }

    public CentreRegroupement getCentreRegroupement() {
        return this.centreRegroupement;
    }

    public Centre centreRegroupement(CentreRegroupement centreRegroupement) {
        this.setCentreRegroupement(centreRegroupement);
        this.centreRegroupementId = centreRegroupement != null ? centreRegroupement.getId() : null;
        return this;
    }

    public void setCentreRegroupement(CentreRegroupement centreRegroupement) {
        this.centreRegroupement = centreRegroupement;
        this.centreRegroupementId = centreRegroupement != null ? centreRegroupement.getId() : null;
    }

    public Long getCentreRegroupementId() {
        return this.centreRegroupementId;
    }

    public void setCentreRegroupementId(Long centreRegroupement) {
        this.centreRegroupementId = centreRegroupement;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Centre)) {
            return false;
        }
        return id != null && id.equals(((Centre) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Centre{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", responsable='" + getResponsable() + "'" +
            ", contact='" + getContact() + "'" +
            "}";
    }
}
