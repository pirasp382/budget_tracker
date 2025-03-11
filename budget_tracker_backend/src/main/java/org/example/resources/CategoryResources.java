package org.example.resources;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.example.dto.Category;
import org.example.dto.UpdateCategory;

@Path("/category")
public interface CategoryResources {

  @GET
  Response getAllUserCategories(@HeaderParam("Authorization") final String token);

  @POST
  Response addCategory(@HeaderParam("Authorization") final String token, final Category category);

  @DELETE
  Response deleteCategory(
      @HeaderParam("Authorization") final String token, @QueryParam("title") final String title);

  @PUT
  Response updateCategory(
      @HeaderParam("Authorization") final String token,
      final UpdateCategory category,
      @QueryParam("title") final String title);
}
