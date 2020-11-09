package org.isf.service.response;

import lombok.Data;

import java.util.List;

@Data
public class Diagnosis {
    public String name;
    public List<String> symptoms;

    public void addSymptom(String symptom) {
        symptoms.add(symptom);
    }
}