package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginInput {

  @Schema(
      description = "The username of the user",
      implementation = String.class,
      examples = {"max.mustermann"},
      required = true)
  private String username;

  @Schema(
      description = "The password of the user",
      implementation = String.class,
      examples = {"max.mustermann"},
      required = true)
  private String password;
}
