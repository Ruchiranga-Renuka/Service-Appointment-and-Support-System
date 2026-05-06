import java.io.*;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.*;

public class ReplaceEmojis {
    public static void main(String[] args) throws Exception {
        Map<String, String> iconMap = new HashMap<>();// Mapping
        iconMap.put("📅", "<i data-lucide=\"calendar\" class=\"icon-sm\"></i>");
        iconMap.put("🔄", "<i data-lucide=\"refresh-cw\" class=\"icon-sm\"></i>");
        iconMap.put("✅", "<i data-lucide=\"check-circle\" class=\"icon-sm\"></i>");
        iconMap.put("👤", "<i data-lucide=\"user\" class=\"icon-sm\"></i>");
        iconMap.put("⏰", "<i data-lucide=\"clock\" class=\"icon-sm\"></i>");
        iconMap.put("📍", "<i data-lucide=\"map-pin\" class=\"icon-sm\"></i>");
        iconMap.put("🔒", "<i data-lucide=\"lock\" class=\"icon-sm\"></i>");
        iconMap.put("👁️", "<i data-lucide=\"eye\" class=\"icon-sm\"></i>"); 
        iconMap.put("👁", "<i data-lucide=\"eye\" class=\"icon-sm\"></i>"); 
        iconMap.put("🔑", "<i data-lucide=\"key\" class=\"icon-sm\"></i>");
        iconMap.put("💾", "<i data-lucide=\"save\" class=\"icon-sm\"></i>");
        iconMap.put("🔨", "<i data-lucide=\"hammer\" class=\"icon-sm\"></i>");
        iconMap.put("✏️", "<i data-lucide=\"pencil\" class=\"icon-sm\"></i>");
        iconMap.put("✏", "<i data-lucide=\"pencil\" class=\"icon-sm\"></i>");
        iconMap.put("🏖️", "<i data-lucide=\"umbrella\" class=\"icon-sm\"></i>");
        iconMap.put("🏖", "<i data-lucide=\"umbrella\" class=\"icon-sm\"></i>");
        iconMap.put("👷", "<i data-lucide=\"hard-hat\" class=\"icon-sm\"></i>");
        iconMap.put("🎫", "<i data-lucide=\"ticket\" class=\"icon-sm\"></i>");
        iconMap.put("📬", "<i data-lucide=\"mail-open\" class=\"icon-sm\"></i>");
        iconMap.put("⚙️", "<i data-lucide=\"settings\" class=\"icon-sm\"></i>");
        iconMap.put("⚙", "<i data-lucide=\"settings\" class=\"icon-sm\"></i>");
        iconMap.put("🚨", "<i data-lucide=\"alert-triangle\" class=\"icon-sm\"></i>");
        iconMap.put("💬", "<i data-lucide=\"message-square\" class=\"icon-sm\"></i>");
        iconMap.put("📩", "<i data-lucide=\"mail\" class=\"icon-sm\"></i>");
        iconMap.put("📤", "<i data-lucide=\"send\" class=\"icon-sm\"></i>");
        iconMap.put("🚫", "<i data-lucide=\"ban\" class=\"icon-sm\"></i>");
        iconMap.put("🔍", "<i data-lucide=\"search\" class=\"icon-sm\"></i>");
        iconMap.put("📋", "<i data-lucide=\"clipboard-list\" class=\"icon-sm\"></i>");
        iconMap.put("❌", "<i data-lucide=\"x\" class=\"icon-sm\"></i>");
        iconMap.put("📊", "<i data-lucide=\"bar-chart-2\" class=\"icon-sm\"></i>");
        iconMap.put("👥", "<i data-lucide=\"users\" class=\"icon-sm\"></i>");
        iconMap.put("🛡️", "<i data-lucide=\"shield\" class=\"icon-sm\"></i>");
        iconMap.put("📝", "<i data-lucide=\"file-text\" class=\"icon-sm\"></i>");
        iconMap.put("💡", "<i data-lucide=\"lightbulb\" class=\"icon-sm\"></i>");
        iconMap.put("⭐", "<i data-lucide=\"star\" class=\"icon-sm\"></i>");
        iconMap.put("🏠", "<i data-lucide=\"home\" class=\"icon-sm\"></i>");
        iconMap.put("🏢", "<i data-lucide=\"building\" class=\"icon-sm\"></i>");
        iconMap.put("✉️", "<i data-lucide=\"mail\" class=\"icon-sm\"></i>");
        iconMap.put("✉", "<i data-lucide=\"mail\" class=\"icon-sm\"></i>");
        iconMap.put("📱", "<i data-lucide=\"smartphone\" class=\"icon-sm\"></i>");
        iconMap.put("👋", "<i data-lucide=\"hand\" class=\"icon-sm\"></i>");
        iconMap.put("👑", "<i data-lucide=\"crown\" class=\"icon-sm\"></i>");
        iconMap.put("💰", "<i data-lucide=\"dollar-sign\" class=\"icon-sm\"></i>");
        iconMap.put("💳", "<i data-lucide=\"credit-card\" class=\"icon-sm\"></i>");
        iconMap.put("💸", "<i data-lucide=\"coins\" class=\"icon-sm\"></i>");
        iconMap.put("📞", "<i data-lucide=\"phone\" class=\"icon-sm\"></i>");
        iconMap.put("➕", "<i data-lucide=\"plus\" class=\"icon-sm\"></i>");
        iconMap.put("➖", "<i data-lucide=\"minus\" class=\"icon-sm\"></i>");
        
        // Added edge cases
        iconMap.put("🛠️", "<i data-lucide=\"tool\" class=\"icon-sm\"></i>");
        iconMap.put("🚪", "<i data-lucide=\"door-open\" class=\"icon-sm\"></i>");
        iconMap.put("🔧", "<i data-lucide=\"wrench\" class=\"icon-sm\"></i>");
        iconMap.put("✈️", "<i data-lucide=\"plane\" class=\"icon-sm\"></i>");
        iconMap.put("✈", "<i data-lucide=\"plane\" class=\"icon-sm\"></i>");
        iconMap.put("⚡", "<i data-lucide=\"zap\" class=\"icon-sm\"></i>");
        iconMap.put("🚿", "<i data-lucide=\"shower-head\" class=\"icon-sm\"></i>");
        iconMap.put("🧹", "<i data-lucide=\"sparkles\" class=\"icon-sm\"></i>");
        iconMap.put("❄️", "<i data-lucide=\"snowflake\" class=\"icon-sm\"></i>");
        iconMap.put("❄", "<i data-lucide=\"snowflake\" class=\"icon-sm\"></i>");
        iconMap.put("💻", "<i data-lucide=\"laptop\" class=\"icon-sm\"></i>");
        iconMap.put("🎨", "<i data-lucide=\"palette\" class=\"icon-sm\"></i>");
        iconMap.put("🪲", "<i data-lucide=\"bug\" class=\"icon-sm\"></i>");
        iconMap.put("🪚", "<i data-lucide=\"hammer\" class=\"icon-sm\"></i>");
        iconMap.put("🚀", "<i data-lucide=\"rocket\" class=\"icon-sm\"></i>");
        iconMap.put("★", "<i data-lucide=\"star\" class=\"icon-sm\" style=\"fill: currentColor\"></i>");
        iconMap.put("☆", "<i data-lucide=\"star\" class=\"icon-sm\"></i>");
        iconMap.put("▶", "<i data-lucide=\"play\" class=\"icon-sm\" style=\"fill: currentColor\"></i>");
        iconMap.put("🛠", "<i data-lucide=\"tool\" class=\"icon-sm\"></i>");
        iconMap.put("⬅", "<i data-lucide=\"arrow-left\" class=\"icon-sm\"></i>");
        iconMap.put("➡", "<i data-lucide=\"arrow-right\" class=\"icon-sm\"></i>");

        // The injection script for Lucide
        String scriptHtml = "\n<script src=\"https://unpkg.com/lucide@latest\"></script>\n<script>\n  document.addEventListener(\"DOMContentLoaded\", function() {\n    lucide.createIcons();\n  });\n</script>\n";

        Path dirPath = Paths.get("src/main/resources/templates");

        Files.walk(dirPath)
            .filter(Files::isRegularFile)
            .filter(p -> p.toString().endsWith(".html"))
            .forEach(p -> {
                try {
                    String content = new String(Files.readAllBytes(p), StandardCharsets.UTF_8);
                    boolean modified = false;

                    for (Map.Entry<String, String> entry : iconMap.entrySet()) {
                        if (content.contains(entry.getKey())) {
                            content = content.replace(entry.getKey(), entry.getValue());
                            modified = true;
                        }
                    }

                    if (modified) {
                        Files.write(p, content.getBytes(StandardCharsets.UTF_8));
                        System.out.println("Updated: " + p);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
    }
}
