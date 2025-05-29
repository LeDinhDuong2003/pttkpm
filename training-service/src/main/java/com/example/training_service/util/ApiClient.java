// training-service/src/main/java/com/example/training_service/util/ApiClient.java
package com.example.training_service.util;

import com.example.training_service.dto.TrainingRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ApiClient {

    @Value("${api.database-service.url}")
    private String apiDatabaseUrl;

    @Value("${api.training-processor.url}")
    private String apiTrainingProcessorUrl;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public ApiClient(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    // Gọi API database service
    public <T> T getFromDatabase(String endpoint, Class<T> responseType) {
        String url = apiDatabaseUrl + endpoint;
        return restTemplate.getForObject(url, responseType);
    }

    public <T> T getFromDatabase(String endpoint, Class<T> responseType, Map<String, Object> queryParams) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiDatabaseUrl + endpoint);

        if (queryParams != null) {
            queryParams.forEach(builder::queryParam);
        }

        String url = builder.toUriString();
        return restTemplate.getForObject(url, responseType);
    }

    public <T> ResponseEntity<T> getEntityFromDatabase(String endpoint, ParameterizedTypeReference<T> responseType) {
        String url = apiDatabaseUrl + endpoint;
        return restTemplate.exchange(url, HttpMethod.GET, null, responseType);
    }

    public <T> ResponseEntity<T> getEntityFromDatabase(String endpoint, ParameterizedTypeReference<T> responseType, Map<String, Object> queryParams) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiDatabaseUrl + endpoint);

        if (queryParams != null) {
            queryParams.forEach(builder::queryParam);
        }

        String url = builder.toUriString();
        return restTemplate.exchange(url, HttpMethod.GET, null, responseType);
    }

    public <T, R> R postToDatabase(String endpoint, T body, Class<R> responseType) {
        String url = apiDatabaseUrl + endpoint;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<T> requestEntity = new HttpEntity<>(body, headers);
        return restTemplate.postForObject(url, requestEntity, responseType);
    }

    public <T, R> R postToDatabase(String endpoint, T body, Class<R> responseType, Map<String, Object> queryParams) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiDatabaseUrl + endpoint);

        if (queryParams != null) {
            queryParams.forEach(builder::queryParam);
        }

        String url = builder.toUriString();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<T> requestEntity = new HttpEntity<>(body, headers);
        return restTemplate.postForObject(url, requestEntity, responseType);
    }

    public <T> void putToDatabase(String endpoint, T body) {
        String url = apiDatabaseUrl + endpoint;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<T> requestEntity = new HttpEntity<>(body, headers);
        restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Void.class);
    }

    public <T> void putToDatabase(String endpoint, T body, Map<String, Object> queryParams) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiDatabaseUrl + endpoint);

        if (queryParams != null) {
            queryParams.forEach(builder::queryParam);
        }

        String url = builder.toUriString();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<T> requestEntity = new HttpEntity<>(body, headers);
        restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Void.class);
    }

    // CẬP NHẬT: Method mới để gửi training request với mô hình và danh sách mẫu
    public ResponseEntity<Map<String, Object>> requestTraining(TrainingRequest trainingRequest) {
        String url = apiTrainingProcessorUrl + "/api/train";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<TrainingRequest> requestEntity = new HttpEntity<>(trainingRequest, headers);
        return restTemplate.exchange(url, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    // Giữ lại method cũ để tương thích ngược (deprecated)
    @Deprecated
    public ResponseEntity<Map<String, Object>> requestTraining(Long moHinhId) {
        String url = apiTrainingProcessorUrl + "/train/" + moHinhId;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        return restTemplate.exchange(url, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    // Parse phản hồi phân trang từ API
    public <T> Map<String, Object> parsePaginatedResponse(ResponseEntity<Map<String, Object>> response, String dataKey, Class<T> elementType) {
        Map<String, Object> responseBody = response.getBody();
        if (responseBody == null) {
            return new HashMap<>();
        }

        try {
            List<Object> rawItems = (List<Object>) responseBody.get(dataKey);
            List<T> items = new ArrayList<>();

            if (rawItems != null) {
                for (Object item : rawItems) {
                    T convertedItem = objectMapper.convertValue(item, elementType);
                    items.add(convertedItem);
                }
            }

            Map<String, Object> result = new HashMap<>();
            result.put(dataKey, items);
            result.put("trangHienTai", responseBody.get("trangHienTai"));
            result.put("tongSoMuc", responseBody.get("tongSoMuc"));
            result.put("tongSoTrang", responseBody.get("tongSoTrang"));

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    public void delete(String endpoint) {
        String url = apiDatabaseUrl + endpoint;
        restTemplate.delete(url);
    }
}