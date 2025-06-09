

package com.oo2.grupo3.models.entities;

import java.util.List;

import com.oo2.grupo3.models.enums.TipoPersona;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@Getter
@Setter
@Builder
@DiscriminatorValue("CLIENTE")
@Table(name = "cliente")
public class Cliente extends Persona {

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Turno> turnosSolicitados;

    public Cliente() {
        super();
        this.setTipoPersona(TipoPersona.CLIENTE);
    }
}