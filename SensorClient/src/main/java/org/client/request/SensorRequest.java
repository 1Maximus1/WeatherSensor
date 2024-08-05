package org.client.request;


import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class SensorRequest {
    public static void main(String[] args) {
        String sensorName= "MK239";

        Map<String, Object> jsonData = new HashMap<>();
        jsonData.put("username", "OlehUSER");
        jsonData.put("password", "Ribka228");


        Map<String, Object> jsonResponse = makePostRequestWithJSONData("http://localhost:8080/auth/login", jsonData);
        String token = prepareToken((String) jsonResponse.get("jwt-token"));

        registerSensor(sensorName, token);

        Random random = new Random();

        double minTemperature = 0.0;
        double maxTemperature = 45.;
        for (int i=0; i<500; i++){
            System.out.println(i);
            sendMeasurement(minTemperature+(random.nextDouble()*(maxTemperature-minTemperature)),random.nextBoolean(),sensorName,token);
        }


    }

    private static void registerSensor(String sensorName, String token) {
        final String registerUrl = "http://localhost:8080/sensors/registration";

        Map<String, Object> jsonData = new HashMap<>();
        jsonData.put("name", sensorName);

        makePostRequestWithJSONData(registerUrl,jsonData,token);
    }

    public static void makePostRequestWithJSONData(String url, Map<String, Object> jsonData, String token) {
        final RestTemplate restTemplate = new RestTemplate();
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", token);

        HttpEntity<Object> request = new HttpEntity<>(jsonData, headers);
        try {
            String response = restTemplate.postForObject(url, request, String.class);
            System.out.println(response);
        }catch (HttpClientErrorException e){
            System.out.println(e.getMessage());
        }
    }

    public static Map<String,Object> makePostRequestWithJSONData(String url, Map<String, Object> jsonData) {
        final RestTemplate restTemplate = new RestTemplate();
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Object> request = new HttpEntity<>(jsonData, headers);
        try {
            return restTemplate.postForObject(url,request,Map.class);
        }catch (HttpClientErrorException e){
            throw new HttpClientErrorException(HttpStatusCode.valueOf(500));
        }
    }

    public static void sendMeasurement(double value, boolean raining, String sensorName, String token){
        final String addMeasurementURL = "http://localhost:8080/measurements/add";

        Map<String, Object> jsonData = new HashMap<>();
        jsonData.put("value", value);
        jsonData.put("raining", raining);
        jsonData.put("sensor", Map.of("name", sensorName));

        makePostRequestWithJSONData(addMeasurementURL, jsonData, token);

    }

    public static String prepareToken(String token){
        return "Bearer " + token;
    }

}
