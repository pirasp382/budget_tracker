package org.example.mapper;

import org.example.dto.Category;
import org.example.entity.CategoryUser;
import org.example.entity.User;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CategoryMapper {
    
    public static Category mapCategory(final CategoryUser categoryUser){
        return Category.builder()
                        .id(categoryUser.getId())
                        .title(categoryUser.getTitle())
                        .build();
    }

    public static CategoryUser mapCategoryUser(final Category category, final User user){
        return CategoryUser.builder()
                        .title(category.getTitle())
                        .user(user)
                        .build();
    }
}
