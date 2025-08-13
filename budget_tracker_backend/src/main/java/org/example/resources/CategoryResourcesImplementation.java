package org.example.resources;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import org.example.dto.Category;
import org.example.dto.Message;
import org.example.dto.UpdateCategory;
import org.example.dto.UserOutput;
import org.example.entity.CategoryUser;
import org.example.entity.User;
import org.example.mapper.CategoryMapper;
import org.example.repository.CategoryRepository;
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
    final List<Category> categories = CategoryRepository.getAllUserCategories(user)
                                        .stream()
                                        .map(item->CategoryMapper.mapCategory((CategoryUser)item))
                                        .toList();
    return Response.ok(categories).build();
  }

  @Transactional
  public Response addCategory(final String token, final Category category) {
    final List<Message> errorlist = tokenManager.validateToken(token);
    if (!errorlist.isEmpty()) {
      final UserOutput user = UserOutput.builder().errorlist(errorlist).build();
      return Response.status(Response.Status.UNAUTHORIZED).entity(user).build();
    }
    final User user = UserRepository.getUserByUserid(tokenManager.getUserId(token));
    final CategoryUser categoryUser = CategoryMapper.mapCategoryUser(category, user);
    
    if(!CategoryUser.listAll().stream()
                      .map(item->(CategoryUser)item)
                      .anyMatch(item->item.getTitle().equals(categoryUser.getTitle()))){
                        CategoryUser.persist(categoryUser);
                      }
    final List<Category> categories = CategoryRepository.getAllUserCategories(user)
                                        .stream()
                                        .map(item->CategoryMapper.mapCategory((CategoryUser)item))
                                        .toList();
    return Response.ok(categories).build();
  }

  @Transactional
  public Response deleteCategory(final String token, final Long id) {
    final List<Message> errorlist = tokenManager.validateToken(token);
    if (!errorlist.isEmpty()) {
      final UserOutput user = UserOutput.builder().errorlist(errorlist).build();
      return Response.status(Response.Status.UNAUTHORIZED).entity(user).build();
    }
    final User user = UserRepository.getUserByUserid(tokenManager.getUserId(token));
    CategoryRepository.deleteCategory(id, user);
    final var categories = CategoryUser.listAll().stream()
                                          .map(item->CategoryMapper.mapCategory((CategoryUser)item))
                                          .toList();
    return Response.ok(categories).build();
  }

  @Transactional
  public Response updateCategory(
      final String token, final UpdateCategory category, final Long id) {
    final List<Message> errorlist = tokenManager.validateToken(token);
    if (!errorlist.isEmpty()) {
      final UserOutput user = UserOutput.builder().errorlist(errorlist).build();
      return Response.status(Response.Status.UNAUTHORIZED).entity(user).build();
    }
    final User user = UserRepository.getUserByUserid(tokenManager.getUserId(token));
    final var categoryUser = CategoryRepository.getCategoryUserById(id, user);
    if(categoryUser!=null){
      categoryUser.setTitle(category.getTitle());
    }
    final var categories = CategoryUser.listAll().stream()
                                          .map(item->CategoryMapper.mapCategory((CategoryUser)item))
                                          .toList();
    return Response.ok(categories).build();
  }
}
