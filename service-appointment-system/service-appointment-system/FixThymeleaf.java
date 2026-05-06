import java.io.*;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.util.regex.*;

public class FixThymeleaf {
    public static void main(String[] args) throws Exception {
        Path dirPath = Paths.get("src/main/resources/templates");
        Files.walk(dirPath)
            .filter(Files::isRegularFile)
            .filter(p -> p.toString().endsWith(".html"))
            .forEach(p -> {
                try {
                    String content = new String(Files.readAllBytes(p), StandardCharsets.UTF_8);
                    String temp = content;

                    // 1. Fix broken placeholders
                    temp = temp.replaceAll("placeholder=\"<i data-lucide=\"[^\"]+\" class=\"icon-sm\"></i>\\s*", "placeholder=\"");

                    // 2. Fix broken string literals containing the SVG icons inside single quotes.
                    // This handles ANY icon enclosed inside single quotes. We replace " with &quot;
                    // Because there might be multiple icons inside the SAME single quotes (e.g. '<i ..></i> <i ..></i>'), 
                    // we can just loop the regex until no more match.
                    boolean running = true;
                    while (running) {
                        Matcher m = Pattern.compile("'([^']*)<i data-lucide=\"([^\"]+)\" class=\"icon-sm\"></i>([^']*)'").matcher(temp);
                        StringBuffer sb = new StringBuffer();
                        boolean found = false;
                        while(m.find()) {
                            found = true;
                            m.appendReplacement(sb, "'" + Matcher.quoteReplacement(m.group(1)) + "<i data-lucide=&quot;" + m.group(2) + "&quot; class=&quot;icon-sm&quot;></i>" + Matcher.quoteReplacement(m.group(3)) + "'");
                        }
                        m.appendTail(sb);
                        temp = sb.toString();
                        running = found;
                    }

                    // 3. Replace th:text with th:utext anywhere the value contains data-lucide
                    // The regex [^"]* matches across newlines natively in Java if we don't use .
                    // Wait, we can just literally look for th:text=" and if the content up to the matching " contains data-lucide, we replace it.
                    // A simple regex: th:text="([^"]*data-lucide(?:=&quot;|=')[^"]*)" 
                    // Since it has already been converted to &quot; in step 2, we look for data-lucide=&quot; or just data-lucide
                    temp = temp.replaceAll("th:text=\"([^\"]*data-lucide[^\"]*)\"", "th:utext=\"$1\"");
                    
                    // Also handle th:text='...' single quoted attributes just in case
                    temp = temp.replaceAll("th:text='([^']*data-lucide[^']*)'", "th:utext='$1'");

                    // 4. In case there is an icon directly in an attribute without single quotes but we missed it?
                    // E.g. th:onclick="'openModal(\\'updateJob_' + ${job.id} + '\\')'" ... wait, emoji there was Outside the attribute!

                    // 5. Some placeholders might be something like placeholder="..." with <i inside. Checked in step 1.

                    if (!content.equals(temp)) {
                        Files.write(p, temp.getBytes(StandardCharsets.UTF_8));
                        System.out.println("Fixed Thymeleaf syntax in: " + p);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
    }
}
