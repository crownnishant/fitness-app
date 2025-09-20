package com.fitness.activityservice.dto;

import com.fitness.activityservice.entities.ActivityType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class ActivityRequest {

    private String userId;
    private ActivityType type;
    private Integer duration; // in minutes
    private Integer caloriesBurned;
    private LocalDateTime startTime;
    private Map<String , Object> additionalMetrics; // For storing extra info like distance, steps, etc.
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
