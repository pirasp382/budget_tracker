package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.example.enums.Type;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionDTO {

  private long id;

  @Schema(
      description = "description of the transaction",
      implementation = String.class,
      examples = {"rent"})
  @Builder.Default
  private String description = "";

  @Schema(
      description = "transaction amount",
      implementation = BigDecimal.class,
      examples = {"123.45", "-123.45"},
      required = true)
  private BigDecimal amount;

  @Schema(
      description = "Type of the transaction",
      implementation = Type.class,
      examples = {"INCOME", "EXPENSES"},
      required = true)
  private String type;

  @Schema(
      description = "Category of the transaction",
      implementation = String.class,
      examples = {"rent", "groceries", "others"})
  private String category;

  @Schema(
      description = "Accountname of the transaction",
      implementation = String.class,
      examples = {"BankAccount"})
  private String accountTitle;

  @Schema(
      description = "timestamp of the transaction",
      implementation = LocalDateTime.class,
      examples = {"2025-02-18T12:09:21"})
  private LocalDate date;
}
