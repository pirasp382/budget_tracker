package org.example.mapper;

import lombok.experimental.UtilityClass;
import org.example.dto.TransactionDTO;
import org.example.entity.Account;
import org.example.entity.Transaction;
import org.example.enums.Type;

import java.math.BigDecimal;

@UtilityClass
public class TransactionMapper {

  public Transaction mapToTransaction(final TransactionDTO transactionDTO, final Account account) {
    return Transaction.builder()
        .description(transactionDTO.getDescription())
        .amount(transactionDTO.getAmount())
        .type(Type.valueOf(transactionDTO.getType()))
        .date(transactionDTO.getDate())
        .account(account)
        .category(transactionDTO.getCategory())
        .build();
  }

  public TransactionDTO mapToTransactionDTO(final Transaction transaction) {
    return TransactionDTO.builder()
        .id(transaction.getId())
        .description(transaction.getDescription())
        .amount(transaction.getAmount())
        .accountTitle(transaction.getAccount().getTitle())
        .date(transaction.getDate())
        .type(transaction.getType().name())
        .category(transaction.getCategory())
        .build();
  }
}
