package com.fitness.aiservice.services.Impl;

import com.fitness.aiservice.entities.Activity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityMessageListener {

    private final ActivityAIService activityAIService;

    @KafkaListener(topics = "${kafka.topic.name}", groupId = "activity-processor-group")
    public void processActivity(Activity activity) {
        log.info("Received activity for AI processing: {}", activity.getUserId());
        try {
            activityAIService.generateRecommendations(activity);
        } catch (WebClientResponseException e) {
            log.error("Gemini API failed with status {} for user {}: {}",
                    e.getRawStatusCode(), activity.getUserId(), e.getResponseBodyAsString());
            // Decide: swallow, retry manually, or send to DLQ
        } catch (Exception e) {
            log.error("Unexpected error processing activity {}: {}", activity.getUserId(), e.getMessage(), e);
        }
}}
