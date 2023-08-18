package cn.asany.security.core.util;

import cn.asany.security.core.domain.PasswordPolicy;
import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;
import lombok.Getter;

public class PasswordGenerator {

  private static final Random RANDOM = new SecureRandom();

  public String generatePassword(PasswordPolicy policy, String username) {
    StringBuilder password = new StringBuilder();
    List<CharacterType> requiredCharacterTypes = new ArrayList<>();

    if (policy.getRequiresLowerCase()) {
      requiredCharacterTypes.add(CharacterType.LowercaseLetter);
    }
    if (policy.getRequiresUpperCase()) {
      requiredCharacterTypes.add(CharacterType.UppercaseLetter);
    }
    if (policy.getRequiresDigit()) {
      requiredCharacterTypes.add(CharacterType.Digit);
    }
    if (policy.getRequiresSymbol()) {
      requiredCharacterTypes.add(CharacterType.SpecialCharacter);
    }

    for (CharacterType characterType : requiredCharacterTypes) {
      password.append(
          characterType.generateRandomCharacters(Math.max(policy.getMinUniqueCharacters(), 1)));
    }

    int minLength = Math.max(policy.getMinLength(), password.length());
    int maxLength = Math.max(policy.getMaxLength(), minLength);

    int passwordLength = minLength + RANDOM.nextInt(maxLength - minLength + 1);

    while (password.length() < passwordLength) {
      CharacterType characterType =
          CharacterType.values()[RANDOM.nextInt(CharacterType.values().length)];
      password.append(characterType.generateRandomCharacters(1));
    }

    List<Character> passwordChars =
        password.chars().mapToObj(c -> (char) c).collect(Collectors.toList());

    Collections.shuffle(passwordChars, RANDOM);

    if (!policy.getAllowUsernameInPassword()) {
      List<Character> usernameChars =
          username.chars().mapToObj(c -> (char) c).collect(Collectors.toList());

      while (containsSubListWithOrder(passwordChars, usernameChars)) {
        Collections.shuffle(passwordChars, RANDOM);
      }
    }

    return passwordChars.stream()
        .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
        .toString();
  }

  protected static boolean containsSubListWithOrder(
      List<Character> list1, List<Character> subList) {
    if (subList.isEmpty()) {
      return true; // An empty sub-list is always considered contained
    }
    int index = 0;
    for (Character item : list1) {
      if (item.equals(subList.get(index))) {
        index++;
        if (index == subList.size()) {
          return true; // All elements of subList found in order
        }
      }
    }
    return false;
  }

  /** 字符类型 */
  @Getter
  enum CharacterType {
    UppercaseLetter("ABCDEFGHIJKLMNOPQRSTUVWXYZ"),
    LowercaseLetter("abcdefghijklmnopqrstuvwxyz"),
    Digit("0123456789"),
    SpecialCharacter("!@#$%^&*()-_=+[]{}|;:',.<>?");

    private final String characters;

    CharacterType(String characters) {
      this.characters = characters;
    }

    public String generateRandomCharacters(int length) {
      StringBuilder result = new StringBuilder();

      for (int i = 0; i < length; i++) {
        int randomIndex = RANDOM.nextInt(characters.length());
        result.append(characters.charAt(randomIndex));
      }

      return result.toString();
    }
  }
}
