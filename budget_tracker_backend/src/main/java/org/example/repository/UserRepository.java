package org.example.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import lombok.experimental.UtilityClass;
import org.example.entity.User;

import java.util.List;
import java.util.Map;

@UtilityClass
public class UserRepository implements PanacheRepository<User> {

  public static User getUserByUsername(final String username) {
    return User.find(
            "select u from User u where u.username =: userParam", Map.of("userParam", username))
        .firstResult();
  }

  public static User getUserByUserid(final Long id) {
    return User.find("select u from User u where u.id =: userParam", Map.of("userParam", id))
        .firstResult();
  }

  public static User getUserByEmail(final String email) {
    return User.find("select u from User u where u.email =: userParam", Map.of("userParam", email))
        .firstResult();
  }

  public static String getPasswordByUsername(final String username) {
    return User.find(
            "select u.password from User u where u.username =: userParam",
            Map.of("userParam", username))
        .project(String.class)
        .firstResult();
  }

  public static List<User> getAllUser() {
    return User.listAll();
  }
}
