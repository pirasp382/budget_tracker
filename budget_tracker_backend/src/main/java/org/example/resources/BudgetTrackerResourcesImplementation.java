package org.example.resources;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import org.example.dto.*;
import org.example.entity.User;
import org.example.enums.Type;
import org.example.mapper.AccountMapper;
import org.example.mapper.UserMapper;
import org.example.repository.AccountRepository;
import org.example.repository.TransactionRepository;
import org.example.repository.UserRepository;
import org.example.services.validation.LoginValidation;
import org.example.services.validation.RegistrationValidation;
import org.example.util.CategoryStorage;
import org.example.util.TokenManager;

import java.math.BigDecimal;
import java.util.List;

public class BudgetTrackerResourcesImplementation implements BudgetTrackerResources {

  @Inject TokenManager tokenManager;

  @Transactional
  public Response registration(final RegistrationInput registrationInput) {
    final List<Message> errorlist = RegistrationValidation.validate(registrationInput);
    if (!errorlist.isEmpty()) {
      final UserOutput user = UserOutput.builder().errorlist(errorlist).build();
      return Response.status(Response.Status.UNAUTHORIZED).entity(user).build();
    }
    final User user = UserMapper.mapToUser(registrationInput);
    user.persist();
    CategoryStorage.initializecategories(user.getId());
    final UserOutput output = UserMapper.mapToUserOutput(user);
    return Response.ok(output).build();
  }

  public Response login(final LoginInput loginInput) {
    final List<Message> errorlist = LoginValidation.validate(loginInput);
    if (!errorlist.isEmpty()) {
      final UserOutput user = UserOutput.builder().errorlist(errorlist).build();
      return Response.status(Response.Status.UNAUTHORIZED).entity(user).build();
    }
    final User user = UserRepository.getUserByUsername(loginInput.getUsername());
    final List<AccountDTO> accountList =
        AccountRepository.getAccountsByUserId(user, "title", false, null).stream()
            .map(AccountMapper::mapToAccountDTO)
            .toList();
    final UserOutput output = UserMapper.mapToUserOutput(user, accountList);
    return Response.ok(output).build();
  }

  public Response getUserData(final String token) {
    final List<Message> errorlist = tokenManager.validateToken(token);
    if (!errorlist.isEmpty()) {
      final UserOutput user = UserOutput.builder().errorlist(errorlist).build();
      return Response.status(Response.Status.UNAUTHORIZED).entity(user).build();
    }
    final User user = UserRepository.getUserByUserid(tokenManager.getUserId(token));
    final List<AccountDTO> accountDTOList =
        AccountRepository.getAccountsByUserId(user, "title", false, null).stream()
            .map(AccountMapper::mapToAccountDTO)
            .toList();
    final BigDecimal income = TransactionRepository.getTransactionsTypeSum(user, Type.INCOME);
    final BigDecimal expenses = TransactionRepository.getTransactionsTypeSum(user, Type.EXPENSES);
    final BigDecimal balance = AccountRepository.getBalanceOfAccounts(user);
    final UserOutput output =
        UserMapper.mapToUserOutput(user, accountDTOList, income, expenses, balance);
    return Response.ok(output).build();
  }

  public Response hello() {
    return Response.status(Response.Status.ACCEPTED)
        .entity(UserRepository.getUserByUserid(1L))
        .build();
  }
}
