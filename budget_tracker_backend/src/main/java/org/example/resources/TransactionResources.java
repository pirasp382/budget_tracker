package org.example.resources;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.example.dto.TransactionDTO;
import org.example.enums.Time;

import java.util.List;

@Path("/transaction")
public interface TransactionResources {

  @GET
  Response getTransactions(
      @HeaderParam("Authorization") final String token,
      @QueryParam("accountId") final Long accountId,
      @QueryParam("transactionId") final Long transactionId,
      @QueryParam("sortParam") final String sortParam,
      @QueryParam("direction") final boolean directionParam);

  @POST
  Response addTransaction(
      @HeaderParam("Authorization") final String token,
      final TransactionDTO transactionDTO,
      @QueryParam("accountId") final Long accountId);

  @Path("/{id}")
  @DELETE
  Response deleteTransaction(
      @HeaderParam("Authorization") final String token, @PathParam("id") final long id);

  @DELETE
  Response deleteTransactions(
      @HeaderParam("Authorization") final String token, final List<Long> transactionIds);

  @Path("/{id}")
  @PUT
  Response editTransaction(
      @HeaderParam("Authorization") final String token,
      final TransactionDTO transactionDTO,
      @PathParam("id") final Long id);

  @Path("/transactionGraph")
  @GET
  Response getGraphDataTransaction(
      @HeaderParam("Authorization") final String token,
      @QueryParam("accountId") final Long accountId,
      @QueryParam("time") Time time);
}
