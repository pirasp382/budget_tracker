package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.example.enums.Type;

import java.math.BigDecimal;

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
  private String description;

  @Schema(
      description = "transaction amount",
      implementation = BigDecimal.class,
      examples = {"123.45"},
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

  private long accontId;
}
