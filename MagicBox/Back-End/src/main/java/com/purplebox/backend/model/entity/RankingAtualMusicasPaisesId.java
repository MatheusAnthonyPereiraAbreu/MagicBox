package com.purplebox.backend.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
class RankingAtualMusicasPaisesId implements java.io.Serializable {
    private Integer musicaId;
    private Integer paisId;
}
