package com.fitness.activityservice.services.Impl;

import com.fitness.activityservice.dto.ActivityRequest;
import com.fitness.activityservice.dto.ActivityResponse;
import com.fitness.activityservice.entities.Activity;
import com.fitness.activityservice.services.ActivityService;
import com.fitness.activityservice.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {

    private final ActivityRepository activityRepository;
    private final UserValidationService userValidationService;
    private final KafkaTemplate<String, Activity> kafkaTemplate;

    @Value("${kafka.topic.name}")
    private String topicName;

    @Override
    public ActivityResponse createActivity(ActivityRequest request) {
        // Validate if user exists
        boolean isValidUser = userValidationService.validateUser(request.getUserId());
        if(!isValidUser){
            throw new RuntimeException("User does not exist: "+request.getUserId());
        }

        Activity activity = Activity.builder()
                .userId(request.getUserId())
                .type(request.getType())
                .duration(request.getDuration())
                .caloriesBurned(request.getCaloriesBurned())
                .startTime(request.getStartTime())
                .additionalMetrics(request.getAdditionalMetrics())
                .build();

        Activity savedActivity = activityRepository.save(activity);

        try{
            kafkaTemplate.send(topicName, savedActivity.getUserId(), savedActivity);
        }catch (Exception e){
            // Log the error
            System.err.println("Failed to send activity to Kafka: " + e.getMessage());
        }
        return mapToResponse(savedActivity);
    }

    private ActivityResponse mapToResponse(Activity Activity) {
        ActivityResponse response = new ActivityResponse();
        response.setId(Activity.getId());
        response.setUserId(Activity.getUserId());
        response.setType(Activity.getType());
        response.setDuration(Activity.getDuration());
        response.setCaloriesBurned(Activity.getCaloriesBurned());
        response.setStartTime(Activity.getStartTime());
        response.setAdditionalMetrics(Activity.getAdditionalMetrics());
        response.setCreatedAt(Activity.getCreatedAt());
        response.setUpdatedAt(Activity.getUpdatedAt());
        return response;
    }
}
