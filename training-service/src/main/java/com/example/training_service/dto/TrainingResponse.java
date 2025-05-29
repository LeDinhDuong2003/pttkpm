// training-service/src/main/java/com/example/training_service/dto/TrainingResponse.java
package com.example.training_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainingResponse {
    private Boolean success;
    private String message;
    private Integer queuePosition;
    private String trainingId;
    private Long estimatedTime; // thời gian dự kiến (phút)

    // Constructor cho phản hồi thành công
    public TrainingResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    // Constructor đầy đủ
    public TrainingResponse(Boolean success, String message, Integer queuePosition, String trainingId) {
        this.success = success;
        this.message = message;
        this.queuePosition = queuePosition;
        this.trainingId = trainingId;
    }
}