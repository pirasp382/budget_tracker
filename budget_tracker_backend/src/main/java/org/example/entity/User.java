package org.example.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.jackson.Jacksonized;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "user")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
@Jacksonized
public class User extends PanacheEntityBase {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Schema(
      description = "Unique ID of the user",
      examples = {"1"},
      required = true)
  private long id;

  @Column(name = "username", nullable = false, unique = true)
  @Schema(
      description = "The username of the user",
      examples = {"max.mustermann"},
      required = true)
  private String username;

  @Column(name = "email", nullable = false)
  @Schema(
      description = "The email address of the user",
      examples = {"max.mustermann@example.com"},
      required = true)
  private String email;

  @Column(name = "email_hashed", nullable = false, unique = true)
  @Schema(
      description = "The hashed email address of the user",
      examples = {"hashedEmail123"},
      required = true)
  private String hashedEmail;

  @Column(name = "password", nullable = false)
  @Schema(
      description = "The password of the user",
      examples = {"Password123!"},
      required = true)
  private String password;

  @Column(name = "created_at", nullable = false)
  @Schema(
      description = "Timestamp of when the user was created",
      examples = {"2023-10-01T12:00:00"},
      required = true)
  private LocalDateTime createdAt;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonManagedReference
  private List<Account> accountList;

  @PrePersist
  private void onCreate(){
    createdAt = LocalDateTime.now();
  }
}
