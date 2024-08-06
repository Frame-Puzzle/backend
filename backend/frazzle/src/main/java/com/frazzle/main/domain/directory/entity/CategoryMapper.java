package com.frazzle.main.domain.directory.entity;

import java.util.HashMap;
import java.util.Map;

public class CategoryMapper {
    
    private static final Map<String, String> categoryMap = new HashMap<>();
    
    static {
        categoryMap.put("friend", "친구");
        categoryMap.put("lover", "연인");
        categoryMap.put("pet", "반려동물");
        categoryMap.put("family", "가족");
    }
    
    public static String getCategoryInKorean(String categoryInEnglish) {
        return categoryMap.getOrDefault(categoryInEnglish, "Unknown Category");
    }
}
