package org.example.resources;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.example.dto.AccountDTO;
import org.example.dto.LoginInput;
import org.example.dto.RegistrationInput;
import org.example.dto.TransactionDTO;

@Path("")
public interface BudgetTrackerResources {

  @Path("/registration")
  @POST
  Response registration(final RegistrationInput registrationInput);

  @Path("/login")
  @POST
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

  @Path("/hello")
  @GET
  Response hello();
}
