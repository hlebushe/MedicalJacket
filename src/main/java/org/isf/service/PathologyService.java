package org.isf.service;

import org.isf.dao.Pathology;
import org.isf.dao.Patient;
import org.isf.repository.PathologyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PathologyService {

    @Autowired
    private PathologyRepository pathologyRepository;

    public Pathology savePathology(Pathology pathology) {
        return pathologyRepository.save(pathology);
    }

    public List<Pathology> getPathologies(Patient patient) {
        return pathologyRepository.getAllByPatient(patient);
    }

    public Pathology getPathology(UUID id) {
        return pathologyRepository.findById(id).get();
    }
}
