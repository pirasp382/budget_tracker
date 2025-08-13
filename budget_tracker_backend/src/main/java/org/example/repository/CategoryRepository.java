package org.example.repository;

import java.util.List;
import java.util.Map;

import org.example.entity.CategoryUser;
import org.example.entity.User;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CategoryRepository implements PanacheRepository<CategoryUser>{
    
    public List<CategoryUser> getAllUserCategories(final User user){
        return CategoryUser.find("""
                select c from CategoryUser c
                where c.user =: userParam
                """, Map.of("userParam", user))
                .list();
    }

    public void deleteCategory(final Long id, final User user){
        CategoryUser.delete("""
        delete from CategoryUser c
        where c.id =: idParam AND c.user=: userParam    
        """, Map.of("idParam", id, "userParam", user));
    }

    public CategoryUser getCategoryUserById(final Long id, final User user){
        return CategoryUser.find("""
            select c from CategoryUser c
            where c.id =: idParam and c.user =: userParam
        """, Map.of("idParam", id, "userParam", user))
        .firstResult();
    }
}
