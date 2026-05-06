import java.io.*;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.util.regex.*;

public class RevertAndFixThymeleaf {
    public static void main(String[] args) throws Exception {
        Path dirPath = Paths.get("src/main/resources/templates");
        Files.walk(dirPath)
            .filter(Files::isRegularFile)
            .filter(p -> p.toString().endsWith(".html"))
            .forEach(p -> {
                try {
                    String original = new String(Files.readAllBytes(p), StandardCharsets.UTF_8);
                    String content = original;

                    // 1. Revert broken syntax introduced by FixThymeleaf.java everywhere
                    content = content.replace("<i data-lucide=&quot;", "<i data-lucide=\"");
                    content = content.replace("&quot; class=&quot;icon-sm&quot;></i>", "\" class=\"icon-sm\"></i>");

                    // Ensure placeholders are cleaned (if they were broken by ReplaceEmojis.java)
                    content = content.replaceAll("placeholder=\"<i data-lucide=\"[^\"]+\" class=\"icon-sm\"></i>\\s*", "placeholder=\"");

                    // 2. Safely apply quoting ONLY to SVG icons strictly bounded by single quotes on the same line
                    String[] lines = content.split("\r?\n", -1); // preserve trailing newlines
                    for (int i = 0; i < lines.length; i++) {
                        String line = lines[i];
                        // Regex matches: 
                        // group 1: starting single quote + zero or more non-quote chars
                        // group 2: the lucide icon name
                        // group 3: zero or more non-quote chars + ending single quote
                        String pattern = "('[^']*?)<i data-lucide=\"([a-z\\-]+)\" class=\"icon-sm\"></i>([^']*?')";
                        Matcher m = Pattern.compile(pattern).matcher(line);
                        StringBuffer sb = new StringBuffer();
                        while (m.find()) {
                            m.appendReplacement(sb, Matcher.quoteReplacement(m.group(1)) + 
                                                    "<i data-lucide=&quot;" + m.group(2) + "&quot; class=&quot;icon-sm&quot;></i>" + 
                                                    Matcher.quoteReplacement(m.group(3)));
                        }
                        m.appendTail(sb);
                        lines[i] = sb.toString();
                    }
                    content = String.join("\n", lines); // Use \n since git/Thymeleaf handles it nicely

                    // 3. Upgrade th:text to th:utext if it contains our injected &quot; 
                    // This matches multi-line attributes thanks to replacing globally
                    content = content.replaceAll("th:text=\"([^\"]*?data-lucide=&quot;[^\"]*?)\"", "th:utext=\"$1\"");
                    content = content.replaceAll("th:text='([^']*?data-lucide=&quot;[^']*?)'", "th:utext='$1'");

                    if (!original.equals(content)) {
                        Files.write(p, content.getBytes(StandardCharsets.UTF_8));
                        System.out.println("Restored and fixed: " + p);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
    }
}
