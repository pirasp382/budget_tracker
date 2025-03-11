package org.example.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.enums.Type;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Transaction extends PanacheEntityBase {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(name = "description")
  private String description;

  @Column(name = "amount", nullable = false)
  private BigDecimal amount;

  @Column(name = "date", nullable = false)
  private LocalDate date;

  @Column(name = "type", nullable = false)
  private Type type;

  @Column(name = "category")
  private String category;

  @Column(name = "created_date")
  private LocalDateTime createdDate;

  @ManyToOne
  @JoinColumn(name = "account_id", nullable = false)
  @JsonBackReference
  private Account account;

  @PrePersist
  private void onCreate() {
    date = (date == null) ? LocalDate.now() : date;
    createdDate = LocalDateTime.now();
    if (category == null) {
      category = "others";
    }
    if (type == Type.EXPENSES && amount.compareTo(BigDecimal.ZERO) > 0) {
      amount = amount.negate();
    } else if (type == Type.INCOME && amount.compareTo(BigDecimal.ZERO) < 0) {
      amount = amount.abs();
    }
  }

  @PreUpdate
  private void inUpdate() {
    if (type == Type.EXPENSES && amount.compareTo(BigDecimal.ZERO) > 0) {
      amount = amount.negate();
    } else if (type == Type.INCOME && amount.compareTo(BigDecimal.ZERO) < 0) {
      amount = amount.abs();
    }
  }
}
