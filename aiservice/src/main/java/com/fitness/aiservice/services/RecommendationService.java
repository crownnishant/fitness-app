package com.fitness.aiservice.services;

import com.fitness.aiservice.entities.Recommendation;

import java.util.List;

public interface RecommendationService {

    List<Recommendation> getUserRecommendation(String userId);

    Recommendation getActivityRecommendation(String activityId);

}
