package cn.asany.security.core.util;

import static org.junit.jupiter.api.Assertions.*;

import cn.asany.security.core.domain.PasswordPolicy;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class PasswordGeneratorTest {

  private PasswordGenerator passwordGenerator = new PasswordGenerator();

  @Test
  void generatePassword() {
    PasswordPolicy policy = new PasswordPolicy();
    policy.setMinLength(8);
    policy.setMaxLength(16);
    policy.setRequiresLowerCase(false);
    policy.setRequiresUpperCase(false);
    policy.setRequiresDigit(false);
    policy.setRequiresSymbol(false);
    policy.setMinUniqueCharacters(2);
    policy.setAllowUsernameInPassword(false);

    String username = "exampleUser";
    String generatedPassword = passwordGenerator.generatePassword(policy, username);
    System.out.println("Generated Password: " + generatedPassword);
  }

  @Test
  void containsSubListWithOrder() {
    List<Character> password =
        "123456".chars().mapToObj(c -> (char) c).collect(Collectors.toList());
    List<Character> username = "456".chars().mapToObj(c -> (char) c).collect(Collectors.toList());
    boolean include = PasswordGenerator.containsSubListWithOrder(password, username);
    log.info("password: {}, username: {}, include: {}", password, username, include);
  }
}
