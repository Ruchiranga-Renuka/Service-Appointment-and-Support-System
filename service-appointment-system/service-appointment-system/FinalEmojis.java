import java.io.*;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
public class FinalEmojis {
    public static void main(String[] args) throws Exception {
        Path dirPath = Paths.get("src/main/resources/templates");
        Files.walk(dirPath)
            .filter(Files::isRegularFile)
            .filter(p -> p.toString().endsWith(".html"))
            .forEach(p -> {
                try {
                    String content = new String(Files.readAllBytes(p), StandardCharsets.UTF_8);
                    String newContent = content.replace("🖨", "<i data-lucide=\"printer\" class=\"icon-sm\"></i>")
                                               .replace("🖨️", "<i data-lucide=\"printer\" class=\"icon-sm\"></i>")
                                               .replace("🙋", "<i data-lucide=\"user\" class=\"icon-sm\"></i>")
                                               .replace("🙋‍♀️", "<i data-lucide=\"user\" class=\"icon-sm\"></i>")
                                               .replace("▶", "<i data-lucide=\"play\" class=\"icon-sm\" style=\"fill: currentColor\"></i>");

                    if (!content.equals(newContent)) {
                        Files.write(p, newContent.getBytes(StandardCharsets.UTF_8));
                        System.out.println("Cleaned final emojis: " + p);
                    }
                } catch (Exception e) {}
            });
    }
}
