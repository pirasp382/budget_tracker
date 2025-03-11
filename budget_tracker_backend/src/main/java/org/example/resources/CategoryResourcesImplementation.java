package org.example.resources;

import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.example.dto.Category;
import org.example.dto.Message;
import org.example.dto.UpdateCategory;
import org.example.dto.UserOutput;
import org.example.entity.User;
import org.example.repository.UserRepository;
import org.example.util.CategoryStorage;
import org.example.util.TokenManager;

import java.util.List;

public class CategoryResourcesImplementation implements CategoryResources {

  @Inject TokenManager tokenManager;

  public Response getAllUserCategories(final String token) {
    final List<Message> errorlist = tokenManager.validateToken(token);
    if (!errorlist.isEmpty()) {
      final UserOutput user = UserOutput.builder().errorlist(errorlist).build();
      return Response.status(Response.Status.UNAUTHORIZED).entity(user).build();
    }
    final User user = UserRepository.getUserByUserid(tokenManager.getUserId(token));
    final List<Category> categories = CategoryStorage.loadCategories(user.getId());
    return Response.ok(categories).build();
  }

  public Response addCategory(final String token, final Category category) {
    final List<Message> errorlist = tokenManager.validateToken(token);
    if (!errorlist.isEmpty()) {
      final UserOutput user = UserOutput.builder().errorlist(errorlist).build();
      return Response.status(Response.Status.UNAUTHORIZED).entity(user).build();
    }
    final User user = UserRepository.getUserByUserid(tokenManager.getUserId(token));
    CategoryStorage.saveCategory(category, user.getId());
    final List<Category> categories = CategoryStorage.loadCategories(user.getId());
    return Response.ok(categories).build();
  }

  public Response deleteCategory(final String token, final String title) {
    final List<Message> errorlist = tokenManager.validateToken(token);
    if (!errorlist.isEmpty()) {
      final UserOutput user = UserOutput.builder().errorlist(errorlist).build();
      return Response.status(Response.Status.UNAUTHORIZED).entity(user).build();
    }
    final User user = UserRepository.getUserByUserid(tokenManager.getUserId(token));
    final List<Category> categories = CategoryStorage.deleteCategory(title, user.getId());
    return Response.ok(categories).build();
  }

  public Response updateCategory(
      final String token, final UpdateCategory category, final String title) {
    final List<Message> errorlist = tokenManager.validateToken(token);
    if (!errorlist.isEmpty()) {
      final UserOutput user = UserOutput.builder().errorlist(errorlist).build();
      return Response.status(Response.Status.UNAUTHORIZED).entity(user).build();
    }
    final User user = UserRepository.getUserByUserid(tokenManager.getUserId(token));
    final List<Category> categories = CategoryStorage.updateCategory(category, title, user.getId());
    return Response.ok(categories).build();
  }
}
