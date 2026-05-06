import java.io.*;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;

public class CleanupStrayEmojis {
    public static void main(String[] args) throws Exception {
        Path dirPath = Paths.get("src/main/resources/templates");
        Files.walk(dirPath)
            .filter(Files::isRegularFile)
            .filter(p -> p.toString().endsWith(".html"))
            .forEach(p -> {
                try {
                    String content = new String(Files.readAllBytes(p), StandardCharsets.UTF_8);
                    
                    String newContent = content.replace("⏳", "<i data-lucide=\"hourglass\" class=\"icon-sm\"></i>")
                                               .replace("\uFE0F", ""); // Strip all variation selector-16

                    if (!content.equals(newContent)) {
                        Files.write(p, newContent.getBytes(StandardCharsets.UTF_8));
                        System.out.println("Cleaned: " + p);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
    }
}
