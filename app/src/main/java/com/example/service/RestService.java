package com.example.service;

import android.os.AsyncTask;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.nio.charset.Charset;

public class RestService extends AsyncTask<URL, String, String> {
    private String requestBody;
    private String urlString;

    public RestService(String requestBody, String urlString) {
        this.requestBody = requestBody;
        this.urlString = urlString;
    }

    public String fetchDataFromApiPOST(String requestBody, String urlString) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
        // set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(requestBody.toString(), headers);
        String result = null;

        // send request and parse result
        ResponseEntity<String> response = restTemplate.exchange(urlString, HttpMethod.POST, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            result = response.getBody();
        }

        return result;
    }

    @Override
    protected String doInBackground(URL... urls) {
        String result = fetchDataFromApiPOST(requestBody, urlString);

        return result;
    }
}