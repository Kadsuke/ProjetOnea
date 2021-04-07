package com.onea.sidot.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Agent.
 */
@Table("agent")
public class Agent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @NotNull(message = "must not be null")
    @Column("nom")
    private String nom;

    @NotNull(message = "must not be null")
    @Column("numero")
    private String numero;

    @NotNull(message = "must not be null")
    @Column("role")
    private String role;

    @JsonIgnoreProperties(value = { "agents", "centre", "agents" }, allowSetters = true)
    @Transient
    private Site site;

    @Column("site_id")
    private Long siteId;

    @JsonIgnoreProperties(value = { "agents", "centre", "agents" }, allowSetters = true)
    @Transient
    private Site site;

    @Column("site_id")
    private Long siteId;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Agent id(Long id) {
        this.id = id;
        return this;
    }

    public String getNom() {
        return this.nom;
    }

    public Agent nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getNumero() {
        return this.numero;
    }

    public Agent numero(String numero) {
        this.numero = numero;
        return this;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getRole() {
        return this.role;
    }

    public Agent role(String role) {
        this.role = role;
        return this;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Site getSite() {
        return this.site;
    }

    public Agent site(Site site) {
        this.setSite(site);
        this.siteId = site != null ? site.getId() : null;
        return this;
    }

    public void setSite(Site site) {
        this.site = site;
        this.siteId = site != null ? site.getId() : null;
    }

    public Long getSiteId() {
        return this.siteId;
    }

    public void setSiteId(Long site) {
        this.siteId = site;
    }

    public Site getSite() {
        return this.site;
    }

    public Agent site(Site site) {
        this.setSite(site);
        this.siteId = site != null ? site.getId() : null;
        return this;
    }

    public void setSite(Site site) {
        this.site = site;
        this.siteId = site != null ? site.getId() : null;
    }

    public Long getSiteId() {
        return this.siteId;
    }

    public void setSiteId(Long site) {
        this.siteId = site;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Agent)) {
            return false;
        }
        return id != null && id.equals(((Agent) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Agent{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", numero='" + getNumero() + "'" +
            ", role='" + getRole() + "'" +
            "}";
    }
}
