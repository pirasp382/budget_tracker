package org.example.resources;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

import org.example.dto.GraphData;
import org.example.dto.Message;
import org.example.dto.TransactionDTO;
import org.example.dto.UserOutput;
import org.example.entity.Account;
import org.example.entity.Transaction;
import org.example.entity.User;
import org.example.enums.Time;
import org.example.enums.Type;
import org.example.mapper.TransactionMapper;
import org.example.mapper.UserMapper;
import org.example.repository.AccountRepository;
import org.example.repository.TransactionRepository;
import org.example.repository.UserRepository;
import org.example.services.validation.TransactionValidation;
import org.example.util.TokenManager;
import org.example.util.Util;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;

public class TransactionResourcesImplementation implements TransactionResources {

  @Inject TokenManager tokenManager;

  public Response getTransactions(
      final String token,
      final Long accountId,
      final Long transactionId,
      final String sortValue,
      final boolean direction) {
    if (transactionId != null) {
      return getTransactionByTransactionId(token, transactionId);
    } else {
      return getAllTransactions(token, sortValue, direction, accountId);
    }
  }

  public Response getAllTransactions(
      final String token,
      final String sortParam,
      final boolean directionParam,
      final Long accountId) {
    final List<Message> errorlist = tokenManager.validateToken(token);
    if (!errorlist.isEmpty()) {
      return Response.status(Response.Status.UNAUTHORIZED).entity(errorlist).build();
    }
    final String sortValue = (sortParam == null || sortParam.equals("null")) ? "date" : sortParam;
    if (!Util.tableContainsColumn(sortValue, false)) {
      errorlist.add(Message.builder().title("Column does not exists").build());
      return Response.status(Response.Status.UNAUTHORIZED).entity(errorlist).build();
    }
    final User user = UserRepository.getUserByUserid(tokenManager.getUserId(token));
    Account account = null;
    if (accountId != null) {
      account = AccountRepository.getAccountByIdAndUserId(accountId, user);
      if (account == null) {
        errorlist.add(
            Message.builder().title("Account does not exist or does not belong to user").build());
        return Response.status(Response.Status.FORBIDDEN).entity(errorlist).build();
      }
    }
    final List<Transaction> transactionList =
        TransactionRepository.getAllTransactionsByUser(user, sortValue, directionParam, account);
    final List<TransactionDTO> transactionDTOs =
        transactionList.stream().map(TransactionMapper::mapToTransactionDTO).toList();
    return Response.ok(transactionDTOs).build();
  }

  public Response getTransactionByTransactionId(final String token, final Long id) {
    final List<Message> errorlist = tokenManager.validateToken(token);
    if (!errorlist.isEmpty()) {
      final UserOutput user = UserOutput.builder().errorlist(errorlist).build();
      return Response.status(Response.Status.UNAUTHORIZED).entity(user).build();
    }
    final User user = UserRepository.getUserByUserid(tokenManager.getUserId(token));
    final Transaction transaction = TransactionRepository.getTransactionById(user, id);
    if (transaction == null) {
      errorlist.add(Message.builder().title("Transaction does not exist").build());
      return Response.status(Response.Status.UNAUTHORIZED).entity(errorlist).build();
    }
    final TransactionDTO transactionDTO = TransactionMapper.mapToTransactionDTO(transaction);
    return Response.ok(transactionDTO).build();
  }

  @Transactional
  public Response addTransaction(
      final String token, final TransactionDTO transactionDTO, final Long accountId) {
    final List<Message> errorlist = tokenManager.validateToken(token);
    if (!errorlist.isEmpty()) {
      final UserOutput user = UserOutput.builder().errorlist(errorlist).build();
      return Response.status(Response.Status.UNAUTHORIZED).entity(user).build();
    }
    final User user = UserRepository.getUserByUserid(tokenManager.getUserId(token));
    final Account account = AccountRepository.getAccountByIdAndUserId(accountId, user);
    final List<Message> errorList = TransactionValidation.valdiate(transactionDTO, account);
    if (!errorList.isEmpty()) {
      final UserOutput userOutput = UserOutput.builder().errorlist(errorList).build();
      return Response.status(Response.Status.UNAUTHORIZED).entity(userOutput).build();
    }
    final Transaction transaction = TransactionMapper.mapToTransaction(transactionDTO, account);
    transaction.persist();
    AccountRepository.updateAccountAddAmount(account, transaction.getAmount());
    final UserOutput userOutput = UserMapper.mapToUserOutput(user);
    return Response.ok(userOutput).build();
  }

  @Transactional
  public Response deleteTransaction(final String token, final long id) {
    final List<Message> errorlist = tokenManager.validateToken(token);
    if (!errorlist.isEmpty()) {
      final UserOutput user = UserOutput.builder().errorlist(errorlist).build();
      return Response.status(Response.Status.UNAUTHORIZED).entity(user).build();
    }
    final User user = UserRepository.getUserByUserid(tokenManager.getUserId(token));
    final Transaction transaction = TransactionRepository.getTransactionById(user, id);
    if (transaction == null) {
      errorlist.add(Message.builder().title("Transaction does not exists").build());
      final UserOutput userOutput = UserOutput.builder().errorlist(errorlist).build();
      return Response.status(Response.Status.UNAUTHORIZED).entity(userOutput).build();
    }
    final Account account = transaction.getAccount();
    AccountRepository.updateAccountSubstractAmount(account, transaction.getAmount());
    transaction.delete();
    final UserOutput userOutput = UserMapper.mapToUserOutput(user);
    return Response.ok(userOutput).build();
  }

  @Transactional
  public Response deleteTransactions(final String token, final List<Long> transactionIds) {
    final List<Message> errorlist = tokenManager.validateToken(token);
    if (!errorlist.isEmpty()) {
      final UserOutput user = UserOutput.builder().errorlist(errorlist).build();
      return Response.status(Response.Status.UNAUTHORIZED).entity(user).build();
    }
    final User user = UserRepository.getUserByUserid(tokenManager.getUserId(token));
    for (final Long id : transactionIds) {
      if (TransactionRepository.transactionExists(user, id)) {
        final Transaction transaction = TransactionRepository.getTransactionById(user, id);
        final Account account = transaction.getAccount();
        AccountRepository.updateAccountSubstractAmount(account, transaction.getAmount());
        transaction.delete();
      }
    }
    return Response.ok("transactions deleted").build();
  }

  @Transactional
  public Response editTransaction(
      final String token, final TransactionDTO transactionDTO, final Long id) {
    final List<Message> errorlist = tokenManager.validateToken(token);
    if (!errorlist.isEmpty()) {
      return Response.status(Response.Status.UNAUTHORIZED)
          .entity(UserOutput.builder().errorlist(errorlist).build())
          .build();
    }

    if (transactionDTO == null) {
      errorlist.add(Message.builder().title("Transaction is null").build());
      return Response.ok(errorlist).build();
    }

    final User user = UserRepository.getUserByUserid(tokenManager.getUserId(token));
    final Transaction transaction = TransactionRepository.getTransactionById(user, id);
    if (transaction == null) {
      errorlist.add(Message.builder().title("Transaction does not exist").build());
      return Response.status(Response.Status.UNAUTHORIZED)
          .entity(UserOutput.builder().errorlist(errorlist).build())
          .build();
    }

    final BigDecimal oldAmount = transaction.getAmount();
    final Type oldType = transaction.getType();
    final Account oldAccount = transaction.getAccount();

    boolean amountChanged =
        transactionDTO.getAmount() != null && transactionDTO.getAmount().compareTo(oldAmount) != 0;
    boolean typeChanged =
        transactionDTO.getType() != null && !transactionDTO.getType().equals(oldType);
    boolean accountChanged =
        transactionDTO.getAccountTitle() != null
            && !transactionDTO.getAccountTitle().equals(oldAccount.getId());

    Account newAccount = oldAccount;

    if (accountChanged) {
      newAccount =
          AccountRepository.getAccountByUserAndTitle(transactionDTO.getAccountTitle(), user);
      if (newAccount == null) {
        errorlist.add(Message.builder().title("New Account does not exist").build());
        return Response.status(Response.Status.BAD_REQUEST)
            .entity(UserOutput.builder().errorlist(errorlist).build())
            .build();
      }
    }

    if (amountChanged || typeChanged || accountChanged) {
      AccountRepository.updateAccountSubstractAmount(oldAccount, oldAmount);

      if (transactionDTO.getType() != null) {
        transaction.setType(Type.valueOf(transactionDTO.getType()));
      }

      if (transactionDTO.getAmount() != null) {
        BigDecimal newAmount = transactionDTO.getAmount();
        if (transaction.getType() == Type.EXPENSES && newAmount.compareTo(BigDecimal.ZERO) > 0) {
          newAmount = newAmount.negate();
        } else if (transaction.getType() == Type.INCOME
            && newAmount.compareTo(BigDecimal.ZERO) < 0) {
          newAmount = newAmount.abs();
        }
        transaction.setAmount(newAmount);
      }

      if (accountChanged) {
        transaction.setAccount(newAccount);
      }

      AccountRepository.updateAccountAddAmount(transaction.getAccount(), transaction.getAmount());
    }

    if (transactionDTO.getCategory() != null) {
      transaction.setCategory(transactionDTO.getCategory());
    }

    if (transactionDTO.getDescription() != null) {
      transaction.setDescription(transactionDTO.getDescription());
    }

    transaction.persist();

    return Response.ok(transaction).build();
  }

  public Response getGraphDataTransaction(
      final String token, final Long accountId, final Time time) {
    final List<Message> errorlist = tokenManager.validateToken(token);
    if (!errorlist.isEmpty()) {
      return Response.status(Response.Status.UNAUTHORIZED)
          .entity(UserOutput.builder().errorlist(errorlist).build())
          .build();
    }
    final User user = UserRepository.getUserByUserid(tokenManager.getUserId(token));
    Account account = null;
    if (accountId != null) {
      account = AccountRepository.getAccountByIdAndUserId(accountId, user);
      if (account == null) {
        errorlist.add(
            Message.builder().title("Account does not exist or does not belong to user").build());
        return Response.status(Response.Status.FORBIDDEN).entity(errorlist).build();
      }
    }
    final var income =
        TransactionRepository.getAllTransactionsOfType(user, Type.INCOME, account, time);
    final var expenses =
        TransactionRepository.getAllTransactionsOfType(user, Type.EXPENSES, account, time);
    final var categoryData = TransactionRepository.getAllTransactionSumsByCategory(user, account);
    BigDecimal balance = AccountRepository.getBalanceOfAccounts(user);
    List<Transaction> transactionList =
        TransactionRepository.getAllTransactionsByUser(user, account);

    if (transactionList.isEmpty()) {
      return Response.ok(Collections.emptyList()).build();
    }

    LinkedHashMap<LocalDate, BigDecimal> history = new LinkedHashMap<>();

    history.put(transactionList.getFirst().getDate(), balance);

    for (Transaction transaction : transactionList) {
      balance = balance.subtract(transaction.getAmount());
      history.put(transaction.getDate(), balance);
    }

    final GraphData data =
        GraphData.builder()
            .income(income)
            .expenses(expenses)
            .categoryData(categoryData)
            .history(history.reversed())
            .build();
    return Response.ok(data).build();
  }
}
