package org.example.resources;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.example.dto.AccountDTO;

import java.util.List;

@Path("/account")
public interface AccountResource {

  @GET
  Response getAllAccounts(
      @HeaderParam("Authorization") final String token,
      @QueryParam("sortParam") final String sortParam,
      @QueryParam("direction") final boolean directionParam,
      @QueryParam("accountId") final Long accountId);

  @POST
  Response addAccount(
      @HeaderParam("Authorization") final String token, final AccountDTO accountDTO);

  @Path("/{id}")
  @PUT
  Response editAccount(
      @HeaderParam("Authorization") final String token,
      @PathParam("id") final Long id,
      final AccountDTO accountDTO);

  @Path("/{id}")
  @DELETE
  Response deleteAccount(
      @HeaderParam("Authorization") final String token, @PathParam("id") final Long id);

  @DELETE
  Response deleteAccounts(
      @HeaderParam("Authorization") final String token, final List<Long> accountIds);
}
