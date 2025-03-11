package org.example.resources;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import org.example.dto.AccountDTO;
import org.example.dto.Message;
import org.example.dto.UserOutput;
import org.example.entity.Account;
import org.example.entity.User;
import org.example.mapper.AccountMapper;
import org.example.mapper.UserMapper;
import org.example.repository.AccountRepository;
import org.example.repository.UserRepository;
import org.example.services.validation.AccountValidation;
import org.example.util.TokenManager;
import org.example.util.Util;

import java.util.List;

public class AccountResourceImplementation implements AccountResource {

  @Inject TokenManager tokenManager;

  public Response getAllAccounts(
      final String token,
      final String sortParam,
      final boolean directionParam,
      final Long accountId) {
    final List<Message> errorlist = tokenManager.validateToken(token);
    if (!errorlist.isEmpty()) {
      final UserOutput user = UserOutput.builder().errorlist(errorlist).build();
      return Response.status(Response.Status.UNAUTHORIZED).entity(user).build();
    }
    final String sortValue = (sortParam != null) ? sortParam : "title";
    if (!Util.tableContainsColumn(sortValue, true)) {
      errorlist.add(Message.builder().title("Column does not exists").build());
      return Response.status(Response.Status.UNAUTHORIZED).entity(errorlist).build();
    }
    final User user = UserRepository.getUserByUserid(tokenManager.getUserId(token));
    Account account = null;
    if (accountId != null) {
      account = AccountRepository.getAccountByIdAndUserId(accountId, user);
      if (account == null) {
        errorlist.add(Message.builder().title("Account does not exist or does not belong to user").build());
        return Response.status(Response.Status.FORBIDDEN).entity(errorlist).build();
      }
    }
    final List<Account> accountList =
        AccountRepository.getAccountsByUserId(user, sortValue, directionParam, account);
    final List<AccountDTO> output =
        accountList.stream().map(AccountMapper::mapToAccountDTO).toList();
    return Response.ok(output).build();
  }

  @Transactional
  public Response addAccount(final String token, final AccountDTO accountDTO) {
    final List<Message> errorlist = tokenManager.validateToken(token);
    if (!errorlist.isEmpty()) {
      final UserOutput user = UserOutput.builder().errorlist(errorlist).build();
      return Response.status(Response.Status.UNAUTHORIZED).entity(user).build();
    }
    final List<Message> errorList = AccountValidation.validate(accountDTO);
    if (!errorList.isEmpty()) {
      return Response.status(Response.Status.UNAUTHORIZED).entity(errorList).build();
    }
    final User user = UserRepository.getUserByUserid(tokenManager.getUserId(token));
    final Account account = AccountMapper.mapToAccount(accountDTO, user);
    account.persist();
    final UserOutput userOutput = UserMapper.mapToUserOutput(user);
    return Response.ok(userOutput).build();
  }

  @Transactional
  public Response editAccount(final String token, final Long id, final AccountDTO accountDTO) {
    final List<Message> errorlist = tokenManager.validateToken(token);
    if (!errorlist.isEmpty()) {
      return Response.status(Response.Status.UNAUTHORIZED).entity(errorlist).build();
    }
    final User user = UserRepository.getUserByUserid(tokenManager.getUserId(token));
    final Account account = AccountRepository.getAccountByIdAndUserId(id, user);
    if (account == null) {
      errorlist.add(Message.builder().title("Account does not exist").build());
      return Response.status(Response.Status.UNAUTHORIZED).entity(errorlist).build();
    }
    if (accountDTO.getTitle() != null) {

      account.setTitle(accountDTO.getTitle());
    }
    if (accountDTO.getBalance() != null) {
      account.setBalance(accountDTO.getBalance());
    }
    if (accountDTO.getDescription() != null) {
      account.setDescription(accountDTO.getDescription());
    }
    account.persist();
    final UserOutput userOutput = UserMapper.mapToUserOutput(user);
    return Response.ok(userOutput).build();
  }

  @Transactional
  public Response deleteAccount(final String token, final Long id) {
    final List<Message> errorlist = tokenManager.validateToken(token);
    if (!errorlist.isEmpty()) {
      return Response.status(Response.Status.UNAUTHORIZED).entity(errorlist).build();
    }
    final User user = UserRepository.getUserByUserid(tokenManager.getUserId(token));
    if (!AccountRepository.accountExists(user, id)) {
      errorlist.add(Message.builder().title("Account does not exist").build());
      return Response.status(Response.Status.UNAUTHORIZED).entity(errorlist).build();
    }
    final Account account = AccountRepository.getAccountByIdAndUserId(id, user);
    account.delete();
    final UserOutput userOutput = UserMapper.mapToUserOutput(user);
    return Response.ok(userOutput).build();
  }

  @Transactional
  public Response deleteAccounts(final String token, final List<Long> accountIds) {
    final List<Message> errorlist = tokenManager.validateToken(token);
    if (!errorlist.isEmpty()) {
      return Response.status(Response.Status.UNAUTHORIZED).entity(errorlist).build();
    }
    final User user = UserRepository.getUserByUserid(tokenManager.getUserId(token));
    for (final Long accountId : accountIds) {
      if (AccountRepository.accountExists(user, accountId)) {
        final Account account = AccountRepository.getAccountByIdAndUserId(accountId, user);
        account.delete();
      }
    }
    final UserOutput userOutput = UserMapper.mapToUserOutput(user);
    return Response.ok(userOutput).build();
  }
}
