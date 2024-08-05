package org.client.request;


import org.client.dto.MeasurementDTO;
import org.client.sensorResponse.MeasurementsResponse;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class DrawChart {
    public static void main(String[] args) {
        Map<String, Object> jsonData = new HashMap<>();
        jsonData.put("username", "OlehUSER");
        jsonData.put("password", "Ribka228");

        String token = SensorRequest.prepareToken((String) SensorRequest.makePostRequestWithJSONData("http://localhost:8080/auth/login", jsonData).get("jwt-token"));

        List<Double> temperatures = getTemperaturesFromServer(token);
        drawChart(temperatures);
    }

    private static List<Double> getTemperaturesFromServer(String token) {
        final RestTemplate restTemplate = new RestTemplate();
        final String url = "http://localhost:8080/measurements";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", token);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        MeasurementsResponse jsonResponse = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                MeasurementsResponse.class
        ).getBody();

        if (jsonResponse == null || jsonResponse.getMeasurements()==null){
            return Collections.emptyList();
        }
        return jsonResponse.getMeasurements().stream().map(MeasurementDTO::getValue).toList();
    }

    private static void drawChart(List<Double> temperatures) {
        double[] xData = IntStream.range(0, temperatures.size()).asDoubleStream().toArray();
        double[] yData = temperatures.stream().mapToDouble(x->x).toArray();

        XYChart chart = QuickChart.getChart("Temperatures", "X", "Y", "temperature", xData, yData);

        new SwingWrapper(chart).displayChart();


    }
}
