package org.isf.repository;

import org.isf.dao.DifferentCentre;
import org.isf.enums.CentreType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface DifferentCentreRepository extends JpaRepository<DifferentCentre, Long> {
    DifferentCentre findByEmail(String email);
    void deleteByEmail(String email);
    boolean existsByEmail(String email);
    List<DifferentCentre> findAllByCentreType(CentreType centerType);
}
