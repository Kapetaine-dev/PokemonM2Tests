package com.ipi.pokemon.repositories;

import com.ipi.pokemon.entities.DresseurEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DresseurRepository extends JpaRepository<DresseurEntity, Long> {
}
