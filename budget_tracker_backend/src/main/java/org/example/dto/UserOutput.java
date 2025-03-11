package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserOutput {

  @Schema(
      description = "The username of the user",
      implementation = String.class,
      examples = {"max.mustermann"},
      required = true)
  private String username;

  @Schema(description = "List of all User Accounts", implementation = List.class)
  private List<AccountDTO> accountDTOList;

  private BigDecimal income;
  private BigDecimal expenses;
  private BigDecimal balance;

  @Schema(
      description = "The encrypted token of the user",
      implementation = String.class,
      examples = {"max.mustermann"},
      required = true)
  private String token;

  @Builder.Default private List<Message> errorlist = List.of();
}
