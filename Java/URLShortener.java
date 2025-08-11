import java.awt.Desktop;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * URLShortener - Offline URL shortener (single-file)
 *
 * Features:
 * - Generates short codes using base62 encoding of an incrementing ID
 * - Persists mappings in url_mappings.csv (code,longUrl,id,createdAt)
 * - Lookup by code or by long URL
 * - List, delete, export mappings
 * - Open long URL in default browser (if supported)
 *
 * How to run:
 * javac URLShortener.java
 * java URLShortener
 */
public class URLShortener {
    private static final String DATA_FILE = "url_mappings.csv";
    private static final String CSV_HEADER = "code,longUrl,id,createdAt";
    private static final String BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // code -> (longUrl, id, createdAt)
    private final Map<String, UrlRecord> map = new LinkedHashMap<>();
    private long nextId = 1;

    public static void main(String[] args) {
        URLShortener app = new URLShortener();
        app.loadFromFile();
        app.runCLI();
    }

    private void runCLI() {
        Scanner sc = new Scanner(System.in);
        boolean running = true;
        System.out.println("=== Offline URL Shortener ===");
        while (running) {
            System.out.println("\nMenu:");
            System.out.println("1. Shorten URL");
            System.out.println("2. Lookup by short code");
            System.out.println("3. Lookup by long URL");
            System.out.println("4. List all mappings");
            System.out.println("5. Delete mapping");
            System.out.println("6. Export mappings to CSV");
            System.out.println("7. Open long URL in browser");
            System.out.println("8. Exit");
            System.out.print("Enter choice: ");
            String choice = sc.nextLine().trim();

            try {
                switch (choice) {
                    case "1":
                        shortenUrlUI(sc);
                        break;
                    case "2":
                        lookupByCodeUI(sc);
                        break;
                    case "3":
                        lookupByLongUrlUI(sc);
                        break;
                    case "4":
                        listAll();
                        break;
                    case "5":
                        deleteUI(sc);
                        break;
                    case "6":
                        exportUI(sc);
                        break;
                    case "7":
                        openUrlUI(sc);
                        break;
                    case "8":
                        running = false;
                        System.out.println("Exiting. Bye!");
                        break;
                    default:
                        System.out.println("Invalid option. Try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        sc.close();
    }

    // ===== UI Handlers =====
    private void shortenUrlUI(Scanner sc) {
        System.out.print("Enter the long URL: ");
        String longUrl = sc.nextLine().trim();
        if (!isValidURL(longUrl)) {
            System.out.println("Invalid URL format. Include protocol (http/https).");
            return;
        }
        // if already exists return code
        String existing = findCodeByLongUrl(longUrl);
        if (existing != null) {
            System.out.println("This URL is already shortened: " + existing);
            return;
        }
        String code = generateNextCode();
        UrlRecord rec = new UrlRecord(longUrl, nextId - 1, LocalDateTime.now());
        map.put(code, rec);
        saveToFile();
        System.out.println("Shortened successfully.");
        System.out.println("Code: " + code);
    }

    private void lookupByCodeUI(Scanner sc) {
        System.out.print("Enter the short code: ");
        String code = sc.nextLine().trim();
        UrlRecord rec = map.get(code);
        if (rec == null) {
            System.out.println("No mapping found for code: " + code);
        } else {
            System.out.println("Long URL: " + rec.longUrl);
            System.out.println("Created: " + rec.createdAt.format(DTF));
            System.out.println("ID: " + rec.id);
        }
    }

    private void lookupByLongUrlUI(Scanner sc) {
        System.out.print("Enter the long URL to find: ");
        String longUrl = sc.nextLine().trim();
        String code = findCodeByLongUrl(longUrl);
        if (code == null) {
            System.out.println("No short code found for this URL.");
        } else {
            System.out.println("Short code: " + code);
        }
    }

    private void listAll() {
        if (map.isEmpty()) {
            System.out.println("No mappings stored yet.");
            return;
        }
        System.out.printf("%-8s  %-40s  %-6s  %s%n", "CODE", "LONG URL", "ID", "CREATED AT");
        System.out.println("----------------------------------------------------------------------------------------");
        for (Map.Entry<String, UrlRecord> e : map.entrySet()) {
            String code = e.getKey();
            UrlRecord r = e.getValue();
            String url = r.longUrl;
            if (url.length() > 40) url = url.substring(0, 37) + "...";
            System.out.printf("%-8s  %-40s  %-6d  %s%n", code, url, r.id, r.createdAt.format(DTF));
        }
    }

    private void deleteUI(Scanner sc) {
        System.out.print("Enter the short code to delete: ");
        String code = sc.nextLine().trim();
        if (map.remove(code) != null) {
            saveToFile();
            System.out.println("Deleted mapping for code: " + code);
        } else {
            System.out.println("No mapping found for code: " + code);
        }
    }

    private void exportUI(Scanner sc) {
        System.out.print("Enter export filename (e.g. export.csv): ");
        String filename = sc.nextLine().trim();
        if (filename.isEmpty()) filename = "export.csv";
        try {
            exportToCsv(filename);
            System.out.println("Exported to " + filename);
        } catch (IOException e) {
            System.out.println("Failed to export: " + e.getMessage());
        }
    }

    private void openUrlUI(Scanner sc) {
        System.out.print("Enter the short code to open: ");
        String code = sc.nextLine().trim();
        UrlRecord rec = map.get(code);
        if (rec == null) {
            System.out.println("No mapping found for code: " + code);
            return;
        }
        try {
            openInBrowser(rec.longUrl);
            System.out.println("Attempted to open URL in default browser.");
        } catch (Exception e) {
            System.out.println("Unable to open browser: " + e.getMessage());
        }
    }

    // ===== Core Logic =====
    private String generateNextCode() {
        long id = nextId++;
        String code = encodeBase62(id);
        // safety check (shouldn't happen)
        while (map.containsKey(code)) {
            id = nextId++;
            code = encodeBase62(id);
        }
        return code;
    }

    private String findCodeByLongUrl(String longUrl) {
        for (Map.Entry<String, UrlRecord> e : map.entrySet()) {
            if (e.getValue().longUrl.equals(longUrl)) return e.getKey();
        }
        return null;
    }

    private static String encodeBase62(long num) {
        if (num == 0) return "0";
        StringBuilder sb = new StringBuilder();
        while (num > 0) {
            int rem = (int)(num % 62);
            sb.append(BASE62.charAt(rem));
            num /= 62;
        }
        return sb.reverse().toString();
    }

    private boolean isValidURL(String url) {
        try {
            URI u = new URI(url);
            String scheme = u.getScheme();
            return scheme != null && (scheme.equalsIgnoreCase("http") || scheme.equalsIgnoreCase("https"));
        } catch (URISyntaxException e) {
            return false;
        }
    }

    private void openInBrowser(String url) throws Exception {
        if (!Desktop.isDesktopSupported()) throw new UnsupportedOperationException("Desktop not supported");
        Desktop desktop = Desktop.getDesktop();
        desktop.browse(new URI(url));
    }

    // ===== Persistence (CSV) =====
    private void loadFromFile() {
        Path p = Paths.get(DATA_FILE);
        if (!Files.exists(p)) {
            System.out.println("No data file found — starting fresh.");
            return;
        }
        try (BufferedReader br = Files.newBufferedReader(p)) {
            String header = br.readLine(); // skip header
            if (header == null) return;
            String line;
            long maxId = 0;
            while ((line = br.readLine()) != null) {
                // CSV format: code,longUrl,id,createdAt
                // longUrl may contain commas - we protect by using simple escaping:
                // We assume longUrl doesn't contain newline and quotes; for robust use, use a proper CSV library.
                String[] parts = splitCsvLine(line);
                if (parts.length < 4) continue;
                String code = parts[0];
                String longUrl = parts[1];
                long id = Long.parseLong(parts[2]);
                LocalDateTime createdAt = LocalDateTime.parse(parts[3], DTF);
                map.put(code, new UrlRecord(longUrl, id, createdAt));
                if (id > maxId) maxId = id;
            }
            nextId = maxId + 1;
            System.out.println("Loaded " + map.size() + " mappings. Next id = " + nextId);
        } catch (IOException e) {
            System.out.println("Failed to load data: " + e.getMessage());
        }
    }

    private void saveToFile() {
        Path p = Paths.get(DATA_FILE);
        try (BufferedWriter bw = Files.newBufferedWriter(p)) {
            bw.write(CSV_HEADER);
            bw.newLine();
            for (Map.Entry<String, UrlRecord> e : map.entrySet()) {
                String code = e.getKey();
                UrlRecord r = e.getValue();
                // write as CSV with simple escaping for commas by surrounding longUrl with quotes if needed
                String longUrl = r.longUrl;
                if (longUrl.contains(",") || longUrl.contains("\"")) {
                    longUrl = longUrl.replace("\"", "\"\""); // escape quotes
                    longUrl = "\"" + longUrl + "\"";
                }
                bw.write(String.join(",", code, longUrl, String.valueOf(r.id), r.createdAt.format(DTF)));
                bw.newLine();
            }
        } catch (IOException ex) {
            System.out.println("Failed to save data: " + ex.getMessage());
        }
    }

    private void exportToCsv(String filename) throws IOException {
        Path p = Paths.get(filename);
        try (BufferedWriter bw = Files.newBufferedWriter(p)) {
            bw.write(CSV_HEADER);
            bw.newLine();
            for (Map.Entry<String, UrlRecord> e : map.entrySet()) {
                String code = e.getKey();
                UrlRecord r = e.getValue();
                String longUrl = r.longUrl;
                if (longUrl.contains(",") || longUrl.contains("\"")) {
                    longUrl = longUrl.replace("\"", "\"\"");
                    longUrl = "\"" + longUrl + "\"";
                }
                bw.write(String.join(",", code, longUrl, String.valueOf(r.id), r.createdAt.format(DTF)));
                bw.newLine();
            }
        }
    }

    // Simple CSV splitter that handles quoted fields (longUrl may be quoted)
    private static String[] splitCsvLine(String line) {
        List<String> parts = new ArrayList<>();
        StringBuilder cur = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                // If next char is also quote -> escaped quote
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    cur.append('"');
                    i++; // skip next quote
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                parts.add(cur.toString());
                cur.setLength(0);
            } else {
                cur.append(c);
            }
        }
        parts.add(cur.toString());
        return parts.toArray(new String[0]);
    }

    // ===== Helper Classes =====
    private static class UrlRecord {
        final String longUrl;
        final long id;
        final LocalDateTime createdAt;

        UrlRecord(String longUrl, long id, LocalDateTime createdAt) {
            this.longUrl = longUrl;
            this.id = id;
            this.createdAt = createdAt;
        }
    }
}




// OUTPUT:
// code,longUrl,id,createdAt
// 1,https://www.pexels.com/search/beautiful/,1,2025-08-11 19:05:36
