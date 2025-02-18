package org.example.mapper;

import lombok.experimental.UtilityClass;
import org.example.dto.TransactionDTO;
import org.example.entity.Account;
import org.example.entity.Transaction;
import org.example.enums.Type;

@UtilityClass
public class TransactionMapper {

  public Transaction mapToTransaction(final TransactionDTO transactionDTO, final Account account){
    return Transaction.builder()
            .description(transactionDTO.getDescription())
            .amount(transactionDTO.getAmount())
            .type(Type.valueOf(transactionDTO.getType()))
            .account(account)
            .build();
  }

  public TransactionDTO mapToTransactionDTO(final Transaction transaction) {
    return TransactionDTO.builder()
        .id(transaction.getId())
        .description(transaction.getDescription())
        .amount(transaction.getAmount())
        .accontId(transaction.getAccount().getId())
        .type(transaction.getType().name())
        .build();
  }
}
