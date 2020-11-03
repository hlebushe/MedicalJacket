package org.isf.serviceimpl;

import org.isf.dao.DifferentCentre;
import org.isf.repository.DifferentCentreRepository;
import org.isf.service.DifferentCentreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class DifferentCentreServiceImpl implements DifferentCentreService {

    @Autowired
    private DifferentCentreRepository repository;

    @Override
    public void save(DifferentCentre differentCentre) {
        differentCentre.setRegiDate(new Date());
        this.repository.save(differentCentre);
    }

    @Override
    public void update(DifferentCentre differentCentre) {
        this.repository.save(differentCentre);
    }

    @Override
    public void findById(Long id) {
        this.repository.findById(id);
    }

    @Override
    public DifferentCentre findByEmail(String email) {
        return this.repository.findByEmail(email);
    }

    @Override
    public Iterable<DifferentCentre> findAll() {
        return this.repository.findAll();
    }

    @Override
    public void deleteByEmail(String email) {
        this.repository.deleteByEmail(email);
    }

    @Override
    public boolean existsByEmail(String email) {
        return this.repository.existsByEmail(email);
    }
}
