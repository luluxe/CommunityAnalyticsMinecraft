package net.communityanalytics.spigot.commands.util;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class Util {
    public static void FileReplacer(String path_name, String line_to_replace,String replace_with){
        try {
            File file = new File(path_name);
            List<String> file_content = new ArrayList<>(Files.readAllLines(file.toPath(), StandardCharsets.UTF_8));
            for (int i = 0; i < file_content.size(); i++) {
                if (file_content.get(i).startsWith(line_to_replace)) {
                    file_content.set(i, replace_with);
                    break;
                }
            }
            Files.write(file.toPath(), file_content, StandardCharsets.UTF_8);
        }
        catch (Exception e) {
            System.out.println("§f(§b§lCommunityAnalytics§f) §cThere was an error while writing the file");
        }
    }
}
