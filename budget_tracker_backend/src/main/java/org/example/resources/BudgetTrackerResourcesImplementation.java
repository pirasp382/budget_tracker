package org.example.resources;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import org.example.dto.*;
import org.example.entity.Account;
import org.example.entity.Transaction;
import org.example.entity.User;
import org.example.enums.Type;
import org.example.mapper.AccountMapper;
import org.example.mapper.TransactionMapper;
import org.example.mapper.UserMapper;
import org.example.repository.AccountRepository;
import org.example.repository.TransactionRepository;
import org.example.repository.UserRepository;
import org.example.services.validation.AccountValidation;
import org.example.services.validation.LoginValidation;
import org.example.services.validation.RegistrationValidation;
import org.example.services.validation.TransactionValidation;
import org.example.util.CategoryStorage;
import org.example.util.TokenManager;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

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
        AccountRepository.getAccountsByUserId(user).stream()
            .map(AccountMapper::mapToAccountDTO)
            .toList();
    final UserOutput output = UserMapper.mapToUserOutput(user, accountList);
    return Response.ok(output).build();
  }

  public Response getAllAccounts(final String token) {
    final List<Message> errorlist = tokenManager.validateToken(token);
    if (!errorlist.isEmpty()) {
      final UserOutput user = UserOutput.builder().errorlist(errorlist).build();
      return Response.status(Response.Status.UNAUTHORIZED).entity(user).build();
    }
    final User user = UserRepository.getUserByUserid(tokenManager.getUserId(token));
    final List<Account> accountList = AccountRepository.getAccountsByUserId(user);
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
      Logger.getAnonymousLogger().info(accountDTO.getTitle());
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

  public Response getTransactions(
      final String token, final Long accountId, final Long transactionId) {
    if (accountId != null) {
      return getAllTransactionByAccount(token, accountId);
    } else if (transactionId != null) {
      return getTransactionByTransactionId(token, transactionId);
    } else {
      return getAllTransactions(token);
    }
  }

  public Response getAllTransactions(final String token) {
    final List<Message> errorlist = tokenManager.validateToken(token);
    if (!errorlist.isEmpty()) {
      return Response.status(Response.Status.UNAUTHORIZED).entity(errorlist).build();
    }
    final User user = UserRepository.getUserByUserid(tokenManager.getUserId(token));
    final List<Transaction> transactionList = TransactionRepository.getAllTransactionsByUser(user);
    final List<TransactionDTO> transactionDTOs =
        transactionList.stream().map(TransactionMapper::mapToTransactionDTO).toList();
    return Response.ok(transactionDTOs).build();
  }

  public Response getAllTransactionByAccount(final String token, final Long accountId) {
    final List<Message> errorlist = tokenManager.validateToken(token);
    if (!errorlist.isEmpty()) {
      return Response.status(Response.Status.UNAUTHORIZED).entity(errorlist).build();
    }
    final User user = UserRepository.getUserByUserid(tokenManager.getUserId(token));
    final Account account = AccountRepository.getAccountByIdAndUserId(accountId, user);
    if (account == null) {
      errorlist.add(Message.builder().title("Account does not exist").build());
      return Response.status(Response.Status.UNAUTHORIZED).entity(errorlist).build();
    }
    final List<Transaction> transactionList =
        TransactionRepository.getAllTransactionsByUserAndAccount(user, account);
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
      final UserOutput userOutput = UserOutput.builder().errorlist(errorlist).build();
      return Response.status(Response.Status.UNAUTHORIZED).entity(userOutput).build();
    }
    final Transaction transaction = TransactionMapper.mapToTransaction(transactionDTO, account);
    transaction.persist();
    if (transaction.getType() == Type.INCOME) {
      AccountRepository.updateAccountAddAmount(account, transaction.getAmount());
    } else {
      AccountRepository.updateAccountSubstractAmount(account, transaction.getAmount());
    }
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
    if (transaction.getType() == Type.INCOME) {
      AccountRepository.updateAccountSubstractAmount(account, transaction.getAmount());
    } else {
      AccountRepository.updateAccountAddAmount(account, transaction.getAmount());
    }
    transaction.delete();
    final UserOutput userOutput = UserMapper.mapToUserOutput(user);
    return Response.ok(userOutput).build();
  }

  @Transactional
  public Response editTransaction(
      final String token, final TransactionDTO transactionDTO, final Long id) {
    final List<Message> errorlist = tokenManager.validateToken(token);
    if (!errorlist.isEmpty()) {
      final UserOutput user = UserOutput.builder().errorlist(errorlist).build();
      return Response.status(Response.Status.UNAUTHORIZED).entity(user).build();
    }
    if (transactionDTO == null) {
      errorlist.add(Message.builder().title("Transaction is null").build());
      return Response.ok(errorlist).build();
    }
    final User user = UserRepository.getUserByUserid(tokenManager.getUserId(token));
    final Transaction transaction = TransactionRepository.getTransactionById(user, id);
    if (transaction == null) {
      errorlist.add(Message.builder().title("Transaction does not exist").build());
      final UserOutput userOutput = UserOutput.builder().errorlist(errorlist).build();
      return Response.status(Response.Status.UNAUTHORIZED).entity(userOutput).build();
    }
    final BigDecimal oldAmount = transaction.getAmount();
    final Type oldType = transaction.getType();
    final boolean amountChanged =
        transactionDTO.getAmount() != null
            && (transactionDTO.getAmount().compareTo(oldAmount) != 0);
    final boolean typeChanged =
        transactionDTO.getType() != null && !transactionDTO.getType().equals(oldType);
    final Account account = transaction.getAccount();
    if (amountChanged || typeChanged) {

      if (oldType == Type.INCOME) {
        AccountRepository.updateAccountSubstractAmount(account, oldAmount);
      } else {
        AccountRepository.updateAccountAddAmount(account, oldAmount);
      }
      if (transactionDTO.getAmount() != null) {
        transaction.setAmount(transactionDTO.getAmount());
      }
      if (transactionDTO.getType() != null && typeMatches(transactionDTO.getType())) {
        transaction.setType(Type.valueOf(transactionDTO.getType()));
      }

      final BigDecimal newAmount = transaction.getAmount();
      final Type newType = transaction.getType();

      if (newType == Type.INCOME) {
        AccountRepository.updateAccountAddAmount(account, newAmount);
      } else {
        AccountRepository.updateAccountSubstractAmount(account, newAmount);
      }
    }

    if (transactionDTO.getCategory() != null
        && TransactionValidation.categoryExists(transactionDTO.getCategory(), user.getId())) {
      transaction.setCategory(transactionDTO.getCategory());
    }

    if (transactionDTO.getCategory() != null) {
      transaction.setDescription(transactionDTO.getDescription());
    }

    transaction.persist();
    final TransactionDTO newTransactionDTO = TransactionMapper.mapToTransactionDTO(transaction);
    return Response.ok(newTransactionDTO).build();
  }

  public Response getAllUserCategories(final String token) {
    final List<Message> errorlist = tokenManager.validateToken(token);
    if (!errorlist.isEmpty()) {
      final UserOutput user = UserOutput.builder().errorlist(errorlist).build();
      return Response.status(Response.Status.UNAUTHORIZED).entity(user).build();
    }
    final User user = UserRepository.getUserByUserid(tokenManager.getUserId(token));
    final List<Category> categories = CategoryStorage.loadCategories(user.getId());
    return Response.ok(categories).build();
  }

  public Response addCategory(final String token, final Category category) {
    final List<Message> errorlist = tokenManager.validateToken(token);
    if (!errorlist.isEmpty()) {
      final UserOutput user = UserOutput.builder().errorlist(errorlist).build();
      return Response.status(Response.Status.UNAUTHORIZED).entity(user).build();
    }
    final User user = UserRepository.getUserByUserid(tokenManager.getUserId(token));
    CategoryStorage.saveCategory(category, user.getId());
    final List<Category> categories = CategoryStorage.loadCategories(user.getId());
    return Response.ok(categories).build();
  }

  public Response deleteCategory(final String token, final String title) {
    final List<Message> errorlist = tokenManager.validateToken(token);
    if (!errorlist.isEmpty()) {
      final UserOutput user = UserOutput.builder().errorlist(errorlist).build();
      return Response.status(Response.Status.UNAUTHORIZED).entity(user).build();
    }
    final User user = UserRepository.getUserByUserid(tokenManager.getUserId(token));
    final List<Category> categories = CategoryStorage.deleteCategory(title, user.getId());
    return Response.ok(categories).build();
  }

  public Response updateCategory(
      final String token, final UpdateCategory category, final String title) {
    final List<Message> errorlist = tokenManager.validateToken(token);
    if (!errorlist.isEmpty()) {
      final UserOutput user = UserOutput.builder().errorlist(errorlist).build();
      return Response.status(Response.Status.UNAUTHORIZED).entity(user).build();
    }
    final User user = UserRepository.getUserByUserid(tokenManager.getUserId(token));
    final List<Category> categories = CategoryStorage.updateCategory(category, title, user.getId());
    return Response.ok(categories).build();
  }

  private static boolean typeMatches(final String inputType) {
    return Arrays.stream(Type.values()).anyMatch(type -> type.name().equals(inputType));
  }

  public Response hello() {
    final User user = UserRepository.getUserByUsername("admin");
    final Account account = AccountRepository.getAccountByIdAndUserId(3L, user);
    return Response.ok(account).build();
  }
}
