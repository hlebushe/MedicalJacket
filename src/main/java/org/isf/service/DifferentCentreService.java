package org.isf.service;

import org.isf.dao.DifferentCentre;
import org.isf.enums.CentreType;

import java.util.List;
import java.util.UUID;


public interface DifferentCentreService {
    void save(DifferentCentre pathologyCentre);
    void update(DifferentCentre pathologyCentre);
    void findById(Long id);
    DifferentCentre findByEmail(String email);
    Iterable<DifferentCentre> findAll();
    void deleteByEmail(String email);
    boolean existsByEmail(String email);
    List<DifferentCentre> findAllByCentreType(CentreType centerType);

}
