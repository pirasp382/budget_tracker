package org.example.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.experimental.UtilityClass;
import org.example.dto.Category;
import org.example.dto.UpdateCategory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@UtilityClass
public class CategoryStorage {
  private static final String BASE_PATH = "src/main/resources/categories/";
  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  private static Path getFilePath(final Long userId) {
    return Paths.get(BASE_PATH, userId + ".json");
  }

  public static void initializecategories(final Long userId) {
    final Path filePath = getFilePath(userId);
    if (Files.exists(filePath)) {
      return;
    }
    try {
      final List<Category> liste =
          List.of(
              Category.builder().title("rent").build(),
              Category.builder().title("grocery").build(),
              Category.builder().title("others").build());
      Files.createDirectories(filePath.getParent());
      Files.createFile(filePath);
      saveCategories(liste, userId);
    } catch (final IOException exception) {
      return;
    }
  }

  private static void saveCategories(final List<Category> categories, final Long userId) {
    final Path filePath = getFilePath(userId);
    try {
      OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValue(filePath.toFile(), categories);
    } catch (final IOException exception) {
      Logger.getAnonymousLogger().info("error");
    }
  }

  public static List<Category> loadCategories(final Long userId) {
    final Path filePath = getFilePath(userId);
    if (!Files.exists(filePath)) {
      return List.of();
    }
    try {
      return OBJECT_MAPPER.readValue(filePath.toFile(), new TypeReference<List<Category>>() {});
    } catch (final IOException exception) {
      return List.of();
    }
  }

  public static List<Category> deleteCategory(final String title, final Long userId) {
    final List<Category> categories = loadCategories(userId);
    final List<Category> newCategories =
        categories.stream().filter(category -> !category.getTitle().equals(title)).toList();
    saveCategories(newCategories, userId);
    return loadCategories(userId);
  }

  public static List<Category> updateCategory(
      final UpdateCategory category, final String title, final Long userId) {
    final Optional<Category> optionalCategory =
        loadCategories(userId).stream().filter(item -> item.getTitle().equals(title)).findFirst();

    if (optionalCategory.isEmpty()) {
      return loadCategories(userId);
    }
    final Category tempCategory = optionalCategory.get();
    if (category.getTitle() != null) {
      tempCategory.setTitle(category.getTitle());
    }
    if (category.getDescription() != null) {
      tempCategory.setDescription(category.getDescription());
    }
    if (category.getColor() != null) {
      System.out.println(category.getColor());
      tempCategory.setColor(category.getColor());
    }
    if (category.getIcon() != null) {
      tempCategory.setIcon(category.getIcon());
    }
    deleteCategory(title, userId);
    saveCategory(tempCategory, userId);
    return loadCategories(userId);
  }

  public static void saveCategory(final Category category, final Long userId) {
    final List<Category> categories = loadCategories(userId);
    categories.add(category);
    saveCategories(categories, userId);
  }
}
