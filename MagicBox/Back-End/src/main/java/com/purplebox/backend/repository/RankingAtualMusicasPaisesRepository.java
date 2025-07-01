package com.purplebox.backend.repository;

import com.purplebox.backend.model.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RankingAtualMusicasPaisesRepository extends JpaRepository<RankingAtualMusicasPaises, RankingAtualMusicasPaisesId> {
    
}
