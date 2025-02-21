package org.example.resources;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.example.dto.*;

import java.util.List;

@Path("")
public interface BudgetTrackerResources {

  @Path("/registration")
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Operation(
      summary = "register user",
      description =
          "Registers a new user in the application. The user is checked and, if validation is successful,"
              + " the user is saved in the database. A token and user information are returned in response.")
  @APIResponse(
      responseCode = "200",
      description = "OK",
      content =
          @Content(schema = @Schema(type = SchemaType.OBJECT, implementation = UserOutput.class)))
  @APIResponse(
      responseCode = "401",
      description = "Invalid or missing input data",
      content = @Content(schema = @Schema(type = SchemaType.ARRAY, implementation = List.class)))
  Response registration(final RegistrationInput registrationInput);

  @Path("/login")
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Operation(
      summary = "login user",
      description = " Logs in a user and returns the login data and the access token")
  @APIResponse(
      responseCode = "200",
      description = "OK",
      content =
          @Content(schema = @Schema(type = SchemaType.OBJECT, implementation = UserOutput.class)))
  @APIResponse(
      responseCode = "401",
      description = "Invalid or missing input data",
      content = @Content(schema = @Schema(type = SchemaType.ARRAY, implementation = List.class)))
  Response login(final LoginInput loginInput);

  @Path("/account")
  @GET
  Response getAllAccounts(@HeaderParam("Authorization") final String token);

  @Path("/account")
  @POST
  Response addAccount(
      @HeaderParam("Authorization") final String token, final AccountDTO accountDTO);

  @Path("/account/{id}")
  @PUT
  Response editAccount(
      @HeaderParam("Authorization") final String token,
      @PathParam("id") final Long id,
      final AccountDTO accountDTO);

  @Path("/account/{id}")
  @DELETE
  Response deleteAccount(
      @HeaderParam("Authorization") final String token, @PathParam("id") final Long id);

  @Path("/transaction")
  @GET
  Response getTransactions(
      @HeaderParam("Authorization") final String token,
      @QueryParam("accountId") Long accountId,
      @QueryParam("transactionId") Long transactionId);

  @Path("/transaction")
  @POST
  Response addTransaction(
      @HeaderParam("Authorization") final String token,
      final TransactionDTO transactionDTO,
      @QueryParam("accountId") final Long accountId);

  @Path("/transaction/{id}")
  @DELETE
  Response deleteTransaction(
      @HeaderParam("Authorization") final String token, @PathParam("id") final long id);

  @Path("/transaction/{id}")
  @PUT
  Response editTransaction(
      @HeaderParam("Authorization") final String token,
      final TransactionDTO transactionDTO,
      @PathParam("id") final Long id);

  @Path("/category")
  @GET
  Response getAllUserCategories(@HeaderParam("Authorization") final String token);

  @Path("/category")
  @POST
  Response addCategory(@HeaderParam("Authorization") final String token, final Category category);

  @Path("/category")
  @DELETE
  Response deleteCategory(
      @HeaderParam("Authorization") final String token, @QueryParam("title") final String title);

  @Path("/category")
  @PUT
  Response updateCategory(@HeaderParam("Authorization") final String token, final UpdateCategory category, @QueryParam("title") final String title);

  @Path("/hello")
  @GET
  Response hello();
}
