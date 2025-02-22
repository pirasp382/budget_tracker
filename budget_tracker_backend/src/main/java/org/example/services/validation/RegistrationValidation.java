package org.example.services.validation;

import lombok.experimental.UtilityClass;
import org.example.dto.Message;
import org.example.dto.RegistrationInput;
import org.example.repository.UserRepository;
import org.example.util.Hasher;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class RegistrationValidation {

  public List<Message> validate(final RegistrationInput registrationInput) {
    final List<Message> errorlist = new ArrayList<>();
    if (registrationInput == null) {
      errorlist.add(Message.builder().title("input is null").build());
      return errorlist;
    }
    if (valueIsNull(registrationInput.getUsername())) {
      errorlist.add(Message.builder().title("Username is null").build());
    } else if (!userIsUnique(registrationInput.getUsername())) {
      errorlist.add(Message.builder().title("Username already exists").build());
    } else if (usernameIsTooShort(registrationInput.getUsername())) {
      errorlist.add(Message.builder().title("Username is too short").build());
    }
    if (valueIsNull(registrationInput.getEmail())) {
      errorlist.add(Message.builder().title("Email is null").build());
    } else if (validateEMailAddressFormat(registrationInput.getEmail())) {
      errorlist.add(Message.builder().title("Email-Address is not valid").build());
    } else if (emailIsNotUnique(registrationInput.getEmail())) {
      errorlist.add(Message.builder().title("Email already exists").build());
    }
    if (valueIsNull(registrationInput.getPassword())) {
      errorlist.add(Message.builder().title("Password is null").build());
    }
    if (valueIsNull(registrationInput.getConfirmedPassword())) {
      errorlist.add(Message.builder().title("ConfirmedPassword is null").build());
    }
    if (!valueIsNull(registrationInput.getPassword())
        && !valueIsNull(registrationInput.getConfirmedPassword())
        && passwordDontMatch(
            registrationInput.getPassword(), registrationInput.getConfirmedPassword())) {
      errorlist.add(Message.builder().title("Passwords dont match").build());
    }
    if (!valueIsNull(registrationInput.getPassword())
        && !valueIsNull(registrationInput.getConfirmedPassword())
        && passwordHasWrongFormat(registrationInput.getPassword())) {
      errorlist.add(Message.builder().title("Password has wrong format").build());
    }
    return errorlist;
  }

  private boolean userIsUnique(final String username) {
    return UserRepository.getUserByUsername(username) == null;
  }

  private static boolean usernameIsTooShort(final String username) {
    return username.length() < 5;
  }

  private static boolean emailIsNotUnique(final String email) {
    final String hashedEmail = Hasher.hashValue(email);
    return UserRepository.getUserByHashedEmail(hashedEmail) != null;
  }

  private static boolean validateEMailAddressFormat(final String email) {
    final String pattern =
        "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
    final Pattern emailPattern = Pattern.compile(pattern);
    final Matcher matcher = emailPattern.matcher(email);
    return !matcher.find();
  }

  private static boolean passwordDontMatch(final String password, final String confirmedPassword) {
    return !password.equals(confirmedPassword);
  }

  private static boolean passwordHasWrongFormat(final String password) {
    final Pattern uppercase = Pattern.compile("[A-Z]+");
    final Pattern lowercase = Pattern.compile("[a-z]+");
    final Pattern digit = Pattern.compile("\\d+");
    final Pattern specialCharacter = Pattern.compile("[^\\w\\s]+");
    final Matcher lowercaseMatcher = lowercase.matcher(password);
    final Matcher uppercaseMatcher = uppercase.matcher(password);
    final Matcher digitMatcher = digit.matcher(password);
    final Matcher spechialMatcher = specialCharacter.matcher(password);
    return !(lowercaseMatcher.find()
        && uppercaseMatcher.find()
        && digitMatcher.find()
        && spechialMatcher.find()
        && password.length() >= 6);
  }

  private boolean valueIsNull(final String value) {
    return value == null;
  }
}
