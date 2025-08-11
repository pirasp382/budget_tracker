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

  @Path("/userData")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Operation(
    summary = "get Userdata for Dashboard",
    description = "get the Userdata(username, income, balance, outcome) for the dashboard page"
  )
  @APIResponse(
    responseCode = "200",
    description = "OK",
    content = 
        @Content(schema = @Schema(type = SchemaType.OBJECT, implementation =  UserOutput.class))
  )
  @APIResponse(
    responseCode = "401",
    description = "UNAUTHORIZED",
    content = 
        @Content(schema = @Schema(type = SchemaType.OBJECT, implementation =  UserOutput.class))
  )
  Response getUserData(@HeaderParam("Authorization") final String token);

  @Path("/hello")
  @Produces(MediaType.APPLICATION_JSON)
  @GET
  Response hello();
}
