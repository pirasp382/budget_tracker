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
public class RegistrationInput {

  @Schema(
      description = "The username of the new user",
      implementation = String.class,
      examples = {"max.mustermann"},
      required = true)
  private String username;

  @Schema(
      description = "The password of the new user",
      implementation = String.class,
      examples = {"Passwort123!"},
      required = true)
  private String password;

  @Schema(
      description = "The confirmed password of the new user",
      implementation = String.class,
      examples = {"Passwort123!"},
      required = true)
  private String confirmedPassword;

  @Schema(
      description = "The e-mail address of the new user",
      implementation = String.class,
      examples = {"max.mustermann@example.com"},
      required = true)
  private String email;
}
