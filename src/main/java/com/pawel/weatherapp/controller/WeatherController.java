package com.pawel.weatherapp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.pawel.weatherapp.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;
import java.util.Map;

@RestController
public class WeatherController {
    @Autowired
    private WeatherService service;

    @GetMapping("/index")
    public String index() {
        return "Hello World";
    }

    @GetMapping("/weatherdetails/{location}")
    public Map<String, JsonNode> getWeatherDetails(@PathVariable String location) throws URISyntaxException, JsonProcessingException {
        service.setLocation(location);
        return service.getWeatherDetails();
    }

    @GetMapping("/forecastdetails/{location}/{days}")
    public Map<String, JsonNode> getForecastDetails(@PathVariable String location, @PathVariable int days) throws URISyntaxException, JsonProcessingException{
        service.setLocation(location);
        return service.getForecastDetails(days);

    }
}
