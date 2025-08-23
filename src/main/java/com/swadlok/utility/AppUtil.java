package com.swadlok.utility;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.text.Normalizer;
import java.util.Base64;

import static com.swadlok.utility.AppConstant.*;

public class AppUtil {

    // Private constructor to prevent instantiation
    private AppUtil() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }

    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generateRandomString(String characterSet, int length) {
        if (length < 1) {
            throw new IllegalArgumentException("Length must be at least 1.");
        }

        StringBuilder result = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            result.append(characterSet.charAt(RANDOM.nextInt(characterSet.length())));
        }
        return result.toString();
    }

    public static String generateRandomString(String characterSet) {
        return generateRandomString(characterSet, 4);
    }

    // Methods for each case
    public static String generateLowercaseString(int length) {
        return generateRandomString(LOWERCASE_CHARACTERS, length);
    }

    public static String generateUppercaseString(int length) {
        return generateRandomString(UPPERCASE_CHARACTERS, length);
    }

    public static String generateNumericString(int length) {
        return generateRandomString(NUMERIC_CHARACTERS, length);
    }

    public static String generateSpecialCharString(int length) {
        return generateRandomString(SPECIAL_CHARACTERS, length);
    }

    // Optional no-length versions (default 4)
    public static String generateLowercaseString() {
        return generateRandomString(LOWERCASE_CHARACTERS);
    }

    public static String generateUppercaseString() {
        return generateRandomString(UPPERCASE_CHARACTERS);
    }

    public static String generateNumericString() {
        return generateRandomString(NUMERIC_CHARACTERS);
    }

    public static String generateSpecialCharString() {
        return generateRandomString(SPECIAL_CHARACTERS);
    }

    public static String encodeBase64(String plainText) {
        if (plainText == null) return null;
        return Base64.getEncoder()
                .encodeToString(plainText.getBytes(StandardCharsets.UTF_8));
    }

    public static String decodeBase64(String encodedText) {
        if (encodedText == null) return null;
        byte[] decodedBytes = Base64.getDecoder().decode(encodedText);
        return new String(decodedBytes, StandardCharsets.UTF_8);
    }

    public static String maskedEmail(String email) {
        // Find the index of '@' to split the email
        int atIndex = email.indexOf('@');

        // Extract the part before '@' and after '@'
        String localPart = email.substring(0, atIndex);
        String domainPart = email.substring(atIndex);

        // Mask the local part (leave the first character and replace the rest with *)
        String maskedLocalPart = localPart.charAt(0) + "*".repeat(localPart.length() - 1);

        // Mask the domain part (replace everything before the '.' with *)
        String[] domainParts = domainPart.split("\\.");
        String maskedDomainPart = "*".repeat(domainParts[0].length()) + "." + domainParts[1];

        // Combine the masked parts and return
        return maskedLocalPart + "@" + maskedDomainPart;
    }

    public static String normalizeName(String name) {
        // Convert to lowercase
        String normalized = name.toLowerCase();
        // Remove accents/diacritics
        normalized = Normalizer.normalize(normalized, Normalizer.Form.NFD);
        normalized = normalized.replaceAll("\\p{M}", "");
        // Replace spaces and special characters with hyphens
        normalized = normalized.replaceAll("[^a-z0-9]+", "-");
        // Remove leading/trailing hyphens
        normalized = normalized.replaceAll("^-+|-+$", "");
        return normalized;
    }

    public static String getExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        return (dotIndex >= 0) ? filename.substring(dotIndex + 1) : "";
    }

}
