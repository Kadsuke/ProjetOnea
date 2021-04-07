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
 * A Site.
 */
@Table("site")
public class Site implements Serializable {

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
    @JsonIgnoreProperties(value = { "site", "site" }, allowSetters = true)
    private Set<Agent> agents = new HashSet<>();

    @JsonIgnoreProperties(value = { "sites", "centreRegroupement" }, allowSetters = true)
    @Transient
    private Centre centre;

    @Column("centre_id")
    private Long centreId;

    @Transient
    @JsonIgnoreProperties(value = { "site", "site" }, allowSetters = true)
    private Set<Agent> agents = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Site id(Long id) {
        this.id = id;
        return this;
    }

    public String getLibelle() {
        return this.libelle;
    }

    public Site libelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getResponsable() {
        return this.responsable;
    }

    public Site responsable(String responsable) {
        this.responsable = responsable;
        return this;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public String getContact() {
        return this.contact;
    }

    public Site contact(String contact) {
        this.contact = contact;
        return this;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Set<Agent> getAgents() {
        return this.agents;
    }

    public Site agents(Set<Agent> agents) {
        this.setAgents(agents);
        return this;
    }

    public Site addAgent(Agent agent) {
        this.agents.add(agent);
        agent.setSite(this);
        return this;
    }

    public Site removeAgent(Agent agent) {
        this.agents.remove(agent);
        agent.setSite(null);
        return this;
    }

    public void setAgents(Set<Agent> agents) {
        if (this.agents != null) {
            this.agents.forEach(i -> i.setSite(null));
        }
        if (agents != null) {
            agents.forEach(i -> i.setSite(this));
        }
        this.agents = agents;
    }

    public Centre getCentre() {
        return this.centre;
    }

    public Site centre(Centre centre) {
        this.setCentre(centre);
        this.centreId = centre != null ? centre.getId() : null;
        return this;
    }

    public void setCentre(Centre centre) {
        this.centre = centre;
        this.centreId = centre != null ? centre.getId() : null;
    }

    public Long getCentreId() {
        return this.centreId;
    }

    public void setCentreId(Long centre) {
        this.centreId = centre;
    }

    public Set<Agent> getAgents() {
        return this.agents;
    }

    public Site agents(Set<Agent> agents) {
        this.setAgents(agents);
        return this;
    }

    public Site addAgent(Agent agent) {
        this.agents.add(agent);
        agent.setSite(this);
        return this;
    }

    public Site removeAgent(Agent agent) {
        this.agents.remove(agent);
        agent.setSite(null);
        return this;
    }

    public void setAgents(Set<Agent> agents) {
        if (this.agents != null) {
            this.agents.forEach(i -> i.setSite(null));
        }
        if (agents != null) {
            agents.forEach(i -> i.setSite(this));
        }
        this.agents = agents;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Site)) {
            return false;
        }
        return id != null && id.equals(((Site) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Site{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", responsable='" + getResponsable() + "'" +
            ", contact='" + getContact() + "'" +
            "}";
    }
}
