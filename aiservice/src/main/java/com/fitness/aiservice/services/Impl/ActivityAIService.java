package com.fitness.aiservice.services.Impl;

import com.fitness.aiservice.entities.Activity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActivityAIService {
    private final GeminiService geminiService;

    public void generateRecommendations(Activity activity){
        String prompt= createPromptForActivity(activity);
        log.info("RESPONSE FROM AI: {}", geminiService.getRecommendations(prompt));
    }
    private String createPromptForActivity(Activity activity) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("You are a fitness assistant. Based on the following activity, ");
        prompt.append("give personalized recommendations for improving performance, ");
        prompt.append("avoiding injury, and suggesting future workouts.\n\n");

        prompt.append("Activity Details:\n");
        prompt.append("User ID: ").append(activity.getUserId()).append("\n");
        prompt.append("Type: ").append(activity.getType()).append("\n");
        prompt.append("Duration: ").append(activity.getDuration()).append(" minutes\n");
        prompt.append("Calories Burned: ").append(activity.getCaloriesBurned()).append("\n");
        prompt.append("Start Time: ").append(activity.getStartTime()).append("\n");

        if (activity.getAdditionalMetrics() != null && !activity.getAdditionalMetrics().isEmpty()) {
            prompt.append("Additional Metrics: ").append(activity.getAdditionalMetrics()).append("\n");
        }

        prompt.append("\nPlease provide:\n");
        prompt.append("1. Feedback on this activity.\n");
        prompt.append("2. Suggestions for improving fitness.\n");
        prompt.append("3. Recommended next workout.\n");

        return prompt.toString();
    }

}
