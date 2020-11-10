package org.isf.service;

import org.isf.service.model.PatientInfoResponse;
import org.springframework.stereotype.Component;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

@Component
public interface DicomClient {

    @GET("patients/{aadhaarId}")
    Call<PatientInfoResponse> getPatient(@Path("aadhaarId") String aadhaarId);
}
