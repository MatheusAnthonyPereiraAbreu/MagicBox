package com.magicbox.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.magicbox.backend.model.entity.*;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Integer> {
    
}