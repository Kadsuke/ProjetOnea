package com.onea.sidot.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.onea.sidot.domain.Commune} entity.
 */
public class CommuneDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String libelle;

    private ProvinceDTO province;

    private TypeCommuneDTO typeCommune;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public ProvinceDTO getProvince() {
        return province;
    }

    public void setProvince(ProvinceDTO province) {
        this.province = province;
    }

    public TypeCommuneDTO getTypeCommune() {
        return typeCommune;
    }

    public void setTypeCommune(TypeCommuneDTO typeCommune) {
        this.typeCommune = typeCommune;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CommuneDTO)) {
            return false;
        }

        CommuneDTO communeDTO = (CommuneDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, communeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommuneDTO{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", province=" + getProvince() +
            ", typeCommune=" + getTypeCommune() +
            "}";
    }
}
