import java.util.*;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordStrengthAnalyzer {

    private static final String HISTORY_FILE = "password_history.txt";
    private static final Set<String> DICTIONARY_WORDS = new HashSet<>(Arrays.asList(
            "password", "admin", "welcome", "login", "user", "qwerty", "abc123", "letmein"
    ));

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("===== Smart Password Strength Analyzer =====");
        System.out.print("Enter your password: ");
        String password = sc.nextLine();
        sc.close();

        try {
            if (isPasswordUsedBefore(password)) {
                System.out.println("  Warning: This password was used before. Choose a new one.");
            }

            int score = analyzePassword(password);
            System.out.println("\nFinal Strength Score: " + score + "/10");

            if (score >= 8) {
                System.out.println("Verdict:  Strong Password");
            } else if (score >= 5) {
                System.out.println("Verdict:  Medium Password");
            } else {
                System.out.println("Verdict:  Weak Password");
            }

            savePassword(password);

        } catch (IOException e) {
            System.out.println("Error accessing password history file.");
        }
    }

    public static int analyzePassword(String password) {
        int score = 0;

        // Length check
        if (password.length() >= 12) {
            score += 3;
        } else if (password.length() >= 8) {
            score += 2;
        } else {
            score += 1;
            System.out.println("Suggestion: Make the password at least 8 characters.");
        }

        // Uppercase, lowercase, number, special char checks
        boolean hasUpper = false, hasLower = false, hasDigit = false, hasSpecial = false;
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isLowerCase(c)) hasLower = true;
            else if (Character.isDigit(c)) hasDigit = true;
            else hasSpecial = true;
        }

        if (hasUpper) score++;
        else System.out.println("Suggestion: Add uppercase letters.");

        if (hasLower) score++;
        else System.out.println("Suggestion: Add lowercase letters.");

        if (hasDigit) score++;
        else System.out.println("Suggestion: Add numbers.");

        if (hasSpecial) score++;
        else System.out.println("Suggestion: Add special characters (!,@,#,$, etc.).");

        // Dictionary word check
        String lowerPass = password.toLowerCase();
        for (String word : DICTIONARY_WORDS) {
            if (lowerPass.contains(word)) {
                System.out.println("Warning: Avoid common word \"" + word + "\" in your password.");
                score -= 2; // penalty
                break;
            }
        }

        // Repeating pattern check
        if (hasRepeatingPattern(password)) {
            System.out.println("Warning: Your password has repeating characters or patterns.");
            score -= 1;
        }

        // Score bounds
        if (score < 0) score = 0;
        if (score > 10) score = 10;

        return score;
    }

    private static boolean hasRepeatingPattern(String password) {
        // Check for 3+ repeating same char
        for (int i = 0; i < password.length() - 2; i++) {
            if (password.charAt(i) == password.charAt(i + 1) && password.charAt(i) == password.charAt(i + 2)) {
                return true;
            }
        }

        // Check for simple sequences like abc, 123
        String lower = password.toLowerCase();
        String sequences = "abcdefghijklmnopqrstuvwxyz0123456789";
        for (int i = 0; i < sequences.length() - 2; i++) {
            String seq = sequences.substring(i, i + 3);
            if (lower.contains(seq)) {
                return true;
            }
        }

        return false;
    }

    // ====== Password History Feature ======
    private static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error: SHA-256 algorithm not found.");
        }
    }

    private static boolean isPasswordUsedBefore(String password) throws IOException {
        String hashed = hashPassword(password);
        File file = new File(HISTORY_FILE);
        if (!file.exists()) return false;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.equals(hashed)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static void savePassword(String password) throws IOException {
        String hashed = hashPassword(password);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(HISTORY_FILE, true))) {
            bw.write(hashed);
            bw.newLine();
        }
    }
}
