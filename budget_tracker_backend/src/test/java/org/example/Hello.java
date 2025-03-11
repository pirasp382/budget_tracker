package org.example;

import org.example.dto.Category;
import org.example.util.CategoryStorage;

import java.util.List;
public class Hello {
  public static void main(String[] args) {
    final List<Category> liste = CategoryStorage.loadCategories(1L);
    liste.forEach(item -> System.out.println(item.toString()));
  }
}
