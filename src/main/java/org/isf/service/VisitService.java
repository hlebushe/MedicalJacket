package org.isf.service;

import org.isf.dao.Visit;
import org.isf.repository.VisitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VisitService {

    @Autowired
    VisitRepository visitsIoOperationRepository;

    public Visit saveVisit(Visit visit) {
        return visitsIoOperationRepository.save(visit);
    }
}
