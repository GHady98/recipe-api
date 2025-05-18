package com.ghadynazha.recipeapi.util;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Utility class for generating secure, randomized passwords.
 *
 * Ensures that each generated password contains at least one uppercase letter,
 * one lowercase letter, one digit, and one special character.
 *
 * Author: Ghady Nazha
 */
public class PasswordGenerator {

    /**
     * Private constructor to prevent instantiation.
     * This is a utility class that only contains static methods.
     */
    private PasswordGenerator() {}

    /**
     * Generates a secure password with the specified length.
     * The password is guaranteed to include at least:
     * - one uppercase letter
     * - one lowercase letter
     * - one digit
     * - one special character
     *
     * @param length the desired length of the password (minimum 4)
     * @return a randomized, secure password string
     */
    public static String generateSecurePassword(int length) {
        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lower = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String special = "!@#$%^&*.";
        String all = upper + lower + digits + special;

        StringBuilder password = new StringBuilder();

        // Ensure at least one from each category
        password.append(upper.charAt((int)(Math.random() * upper.length())));
        password.append(lower.charAt((int)(Math.random() * lower.length())));
        password.append(digits.charAt((int)(Math.random() * digits.length())));
        password.append(special.charAt((int)(Math.random() * special.length())));

        // Fill the rest with random characters
        for (int i = 4; i < length; i++) {
            password.append(all.charAt((int)(Math.random() * all.length())));
        }

        // Shuffle characters to prevent predictable patterns
        List<Character> chars = password.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.toList());

        Collections.shuffle(chars);
        return chars.stream()
                .map(String::valueOf)
                .collect(Collectors.joining());
    }
}