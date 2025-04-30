package com.example.mau_service.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ApiClient {

    @Value("${api.database-service.url}")
    private String apiBaseUrl;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public ApiClient(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    // Phương thức lấy dữ liệu với kiểu trả về đơn giản
    public <T> T get(String endpoint, Class<T> responseType) {
        String url = apiBaseUrl + endpoint;
        return restTemplate.getForObject(url, responseType);
    }

    // Phương thức lấy dữ liệu với kiểu trả về đơn giản và query params
    public <T> T get(String endpoint, Class<T> responseType, Map<String, Object> queryParams) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiBaseUrl + endpoint);

        if (queryParams != null) {
            queryParams.forEach((key, value) -> {
                builder.queryParam(key, value);
            });
        }

        String url = builder.toUriString();
        return restTemplate.getForObject(url, responseType);
    }

    // Phương thức lấy dữ liệu với kiểu generic phức tạp
    public <T> ResponseEntity<T> getForEntity(String endpoint, ParameterizedTypeReference<T> responseType) {
        String url = apiBaseUrl + endpoint;
        return restTemplate.exchange(url, HttpMethod.GET, null, responseType);
    }

    // Phương thức lấy dữ liệu với kiểu generic phức tạp và query params
    public <T> ResponseEntity<T> getForEntity(String endpoint, ParameterizedTypeReference<T> responseType, Map<String, Object> queryParams) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiBaseUrl + endpoint);

        if (queryParams != null) {
            queryParams.forEach((key, value) -> {
                builder.queryParam(key, value);
            });
        }

        String url = builder.toUriString();
        return restTemplate.exchange(url, HttpMethod.GET, null, responseType);
    }

    // Phương thức tạo dữ liệu mới
    public <T, R> R post(String endpoint, T body, Class<R> responseType) {
        String url = apiBaseUrl + endpoint;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<T> requestEntity = new HttpEntity<>(body, headers);
        return restTemplate.postForObject(url, requestEntity, responseType);
    }

    // Phương thức tạo dữ liệu mới với query params
    public <T, R> R post(String endpoint, T body, Class<R> responseType, Map<String, Object> queryParams) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiBaseUrl + endpoint);

        if (queryParams != null) {
            queryParams.forEach((key, value) -> {
                builder.queryParam(key, value);
            });
        }

        String url = builder.toUriString();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<T> requestEntity = new HttpEntity<>(body, headers);
        return restTemplate.postForObject(url, requestEntity, responseType);
    }

    // Phương thức cập nhật dữ liệu
    public <T> void put(String endpoint, T body) {
        String url = apiBaseUrl + endpoint;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<T> requestEntity = new HttpEntity<>(body, headers);
        restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Void.class);
    }

    // Phương thức cập nhật dữ liệu với query params
    public <T> void put(String endpoint, T body, Map<String, Object> queryParams) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiBaseUrl + endpoint);

        if (queryParams != null) {
            queryParams.forEach((key, value) -> {
                builder.queryParam(key, value);
            });
        }

        String url = builder.toUriString();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<T> requestEntity = new HttpEntity<>(body, headers);
        restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Void.class);
    }

    // Phương thức xóa dữ liệu
    public void delete(String endpoint) {
        String url = apiBaseUrl + endpoint;
        restTemplate.delete(url);
    }

    // Phương thức xóa dữ liệu với query params
    public void delete(String endpoint, Map<String, Object> queryParams) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiBaseUrl + endpoint);

        if (queryParams != null) {
            queryParams.forEach((key, value) -> {
                builder.queryParam(key, value);
            });
        }

        String url = builder.toUriString();
        restTemplate.delete(url);
    }

    // Parse PageResponse (phân trang) từ API
    public <T> Map<String, Object> parsePaginatedResponse(ResponseEntity<Map<String, Object>> response, String dataKey, Class<T> elementType) {
        Map<String, Object> responseBody = response.getBody();
        if (responseBody == null) {
            return new HashMap<>();
        }

        try {
            List<T> items = objectMapper.convertValue(responseBody.get(dataKey), new TypeReference<List<T>>() {});

            Map<String, Object> result = new HashMap<>();
            result.put(dataKey, items);
            result.put("trangHienTai", responseBody.get("trangHienTai"));
            result.put("tongSoMuc", responseBody.get("tongSoMuc"));
            result.put("tongSoTrang", responseBody.get("tongSoTrang"));

            return result;
        } catch (Exception e) {
            return new HashMap<>();
        }
    }
}