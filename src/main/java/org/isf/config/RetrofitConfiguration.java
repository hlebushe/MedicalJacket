package org.isf.config;

import okhttp3.OkHttpClient;
import org.isf.service.DicomClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Configuration
@PropertySource("classpath:integration.properties")
public class RetrofitConfiguration {

    @Value("${dicom.base.url}")
    private String dicomBaseUrl;

    @Bean
    public Retrofit dicomRetrofit() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        return new Retrofit.Builder()
                .baseUrl(dicomBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
    }

    @Bean
    public DicomClient dicomClient(Retrofit dicomRetrofit) {
        return dicomRetrofit.create(DicomClient.class);
    }
}
