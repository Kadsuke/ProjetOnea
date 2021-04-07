package com.sidot.gestioneau.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Site.
 */
@Entity
@Table(name = "site")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Site implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "libelle", nullable = false)
    private String libelle;

    @NotNull
    @Column(name = "responsable", nullable = false)
    private String responsable;

    @NotNull
    @Column(name = "contact", nullable = false)
    private String contact;

    @OneToMany(mappedBy = "site")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "site", "site" }, allowSetters = true)
    private Set<Agent> agents = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "sites", "centreRegroupement" }, allowSetters = true)
    private Centre centre;

    @OneToMany(mappedBy = "site")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
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
        return this;
    }

    public void setCentre(Centre centre) {
        this.centre = centre;
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
