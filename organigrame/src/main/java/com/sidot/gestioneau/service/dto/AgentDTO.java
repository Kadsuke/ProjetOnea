package com.sidot.gestioneau.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.sidot.gestioneau.domain.Agent} entity.
 */
public class AgentDTO implements Serializable {

    private Long id;

    @NotNull
    private String nom;

    @NotNull
    private String numero;

    @NotNull
    private String role;

    private SiteDTO site;

    private SiteDTO site;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public SiteDTO getSite() {
        return site;
    }

    public void setSite(SiteDTO site) {
        this.site = site;
    }

    public SiteDTO getSite() {
        return site;
    }

    public void setSite(SiteDTO site) {
        this.site = site;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AgentDTO)) {
            return false;
        }

        AgentDTO agentDTO = (AgentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, agentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AgentDTO{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", numero='" + getNumero() + "'" +
            ", role='" + getRole() + "'" +
            ", site=" + getSite() +
            ", site=" + getSite() +
            "}";
    }
}
