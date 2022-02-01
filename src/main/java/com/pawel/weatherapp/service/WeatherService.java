package com.pawel.weatherapp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pawel.weatherapp.enums.FieldsEnum;
import com.pawel.weatherapp.enums.RequestType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.logging.Logger;

@Service
public class WeatherService {
    private static final Logger logger = Logger.getLogger(WeatherService.class.getName());

    private final String URI_BASE = "http://api.weatherapi.com/v1/";
    private String location;
    private Map<String, JsonNode> weatherDetails = new TreeMap<>();

    @Value("${weatherApiKey}")
    private String apiKey;
    
    public void setLocation(String location) {
        this.location = location;
    }

    public Map<String, JsonNode> getWeatherDetails() throws URISyntaxException, JsonProcessingException {
        HttpResponse<String> response = null;
        ObjectMapper mapper = new ObjectMapper();

        HttpRequest request = createHttpRequest(RequestType.CURRENT);
        HttpClient client = getHttpClient();

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        logger.info("[current] Response status: ===> " + response.statusCode()); //                   --> logging

        JsonNode jsonNode = mapper.readTree(response.body());
        Iterator<String> rootNodesIterator = jsonNode.fieldNames();

        while (rootNodesIterator.hasNext()) {
            Iterator<Map.Entry<String, JsonNode>> fields = jsonNode.get(rootNodesIterator.next()).fields();

            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> entry = (Map.Entry<String, JsonNode>) fields.next();
                for (FieldsEnum enumField : FieldsEnum.values()) {
                   String field = enumField.name().toLowerCase();
                    if (field.equals(entry.getKey())) {
                        weatherDetails.put(enumField.getName(), entry.getValue());
                    }
                }
            }
        }

        return weatherDetails;
    }

    public Map<String, JsonNode> getForecastDetails(int days) throws URISyntaxException, JsonProcessingException {
        HttpResponse<String> response = null;
        ObjectMapper mapper = new ObjectMapper();

        HttpRequest request = createHttpRequest(RequestType.FORECAST, days);
        HttpClient client = getHttpClient();

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        logger.info("[forecast] Response status: ===> " + response.statusCode()); //                   --> logging

        JsonNode jsonNode = mapper.readTree(response.body());
        Iterator<String> rootNodesIterator = jsonNode.fieldNames();

        while (rootNodesIterator.hasNext()) {
            Iterator<Map.Entry<String, JsonNode>> fields = jsonNode.get(rootNodesIterator.next()).fields();

            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> entry = (Map.Entry<String, JsonNode>) fields.next();
                for (FieldsEnum enumField : FieldsEnum.values()) {
                    String field = enumField.name().toLowerCase();
                    if (field.equals(entry.getKey())) {
                        weatherDetails.put(enumField.getName(), entry.getValue());
                    }
                }
            }
        }

        return weatherDetails;
    }

    private HttpRequest createHttpRequest(RequestType type, int... days) {
        HttpRequest request;

        if (type.equals(RequestType.CURRENT)) {
            request = HttpRequest.newBuilder()
                    .GET()
                    .header("accept", "application/json")
                    .uri(URI.create(URI_BASE + "current.json?key=" + apiKey + "&q=" + location))
                    .build();
        } else {
            request = HttpRequest.newBuilder()
                    .GET()
                    .header("accept", "application/json")
                    .uri(URI.create(URI_BASE + "forecast.json?key=" + apiKey + "&q=" + location + "&days=" + days))
                    .build();
        }

        return  request;
    }

    private HttpClient getHttpClient() {
        HttpClient client = HttpClient.newHttpClient();
        return client;
    }
}
