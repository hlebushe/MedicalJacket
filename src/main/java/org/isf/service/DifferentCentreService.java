package org.isf.service;

import org.isf.dao.DifferentCentre;

import java.util.UUID;


public interface DifferentCentreService {
    void save(DifferentCentre pathologyCentre);
    void update(DifferentCentre pathologyCentre);
    void findById(Long id);
    DifferentCentre findByEmail(String email);
    Iterable<DifferentCentre> findAll();
    void deleteByEmail(String email);
    boolean existsByEmail(String email);

}
