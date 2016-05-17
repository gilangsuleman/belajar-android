package com.example.gilang.latihan.service;

import android.os.Bundle;

import com.example.gilang.latihan.domain.Peserta;
import com.example.gilang.latihan.dto.PesertaResponse;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Created by gilang on 13/05/16.
 */
public class HttpService {

    private static String portServer = "8080";
    private static String ipServer = "192.168.1.41";

    private static String BASE_URI = "http://"+ipServer+":"+portServer+"/api/";

    private RestTemplate restTemplate = new RestTemplate();

    public HttpService(){
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
    }

    public ResponseEntity getAllPeserta(){
        String url = BASE_URI + "peserta";

        return restTemplate.getForEntity(url, PesertaResponse.class);
    }

    public ResponseEntity savePeserta (Peserta peserta){
        String url = BASE_URI + "peserta";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Peserta> entity = new HttpEntity<>(peserta, headers);

        return restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
    }

    public ResponseEntity updatePeserta (String id, Peserta peserta){
        String url = BASE_URI + "peserta/" + id;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Peserta> entity = new HttpEntity<>(peserta, headers);

        return restTemplate.exchange(url, HttpMethod.PUT, entity,String.class);
    }

    public ResponseEntity deletePeserta (String id){
        String url = BASE_URI + "peserta/" + id;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity entity = new HttpEntity(headers);

        return restTemplate.exchange(url, HttpMethod.DELETE, entity, String.class);
    }
}
