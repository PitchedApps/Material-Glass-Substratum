package com.pitchedapps.theme.tools.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Allan Wang on 2017-04-22.
 */
public class FileUtils {

    static final String[] values = {
            "values",
            "values-v11",
            "values-v17",
            "values-v21",
            "values-v24"
    };

    public interface Reader {
        void nextLine(String s);
    }

    public static String filename(String... snippets) {
        String dir = "";
        for (String s : snippets) {
            dir += s;
            if (!dir.endsWith("/") || !dir.endsWith("\\")) dir += "/";
        }
        return dir.substring(0, dir.length() - 1);
    }

    public static boolean exists(String... dir) {
        return new File(filename(dir)).exists();
    }

    public static void readValueFiles(String dir, String fileName, Reader reader) {
        for (String value : values) {
            if (!exists(filename(dir, value, fileName))) continue;
            readFile(filename(dir, value), fileName, reader);
        }
    }

    public static void readFile(String dir, String fileName, Reader reader) {
        String line = "";
        String section = filename(dir, fileName);
        if (!exists(section)) return;
        try (BufferedReader br = new BufferedReader(new FileReader(section))) {
            while ((line = br.readLine()) != null) {
                // process the line.
                reader.nextLine(line.trim());
            }
        } catch (IOException e) {
            System.out.println("ERROR :" + e + "\n" + line);
        }
    }

    public static StringBuilder readFile(String... dir) {
        String line = "";
        String section = filename(dir);
        if (!exists(section)) return null;
        StringBuilder data = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(section))) {
            while ((line = br.readLine()) != null) {
                // process the line.
                data.append(line).append("\n");
            }
        } catch (IOException e) {
            System.out.println("ERROR :" + e + "\n" + line);
        }
        return data;
    }

    public static void writeFile(String dir, String fileName, Collection<?>... collections) {
        mkdirs(dir);
        try {
            File newFile = new File(dir, fileName);
            OutputStreamWriter f = new OutputStreamWriter(new FileOutputStream(newFile), "UTF-8");
            f.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<resources>\n\n");
            for (Collection<?> collection : collections)
                for (Object data : collection)
                    if (data.toString() != null)
                        f.append(data.toString()).append("\n");
            f.append("</resources>");
            f.close();
        } catch (Exception e) {
            // This should never happen
            e.printStackTrace();
        }
    }

    public static void copyFile(String source, String sourceFile, String dest, String destFile) {
        writeFile(dest, destFile, readFile(source, sourceFile));
    }

    public static void writeFile(String dir, String fileName, StringBuilder result) {
        if (result == null)
            Utils.p("Empty data in writeFile");
        else
            writeFile(dir, fileName, result.toString());
    }

    public static void writeFile(String dir, String fileName, String content) {
        mkdirs(dir);
        try {
            File newFile = new File(dir, fileName);
            OutputStreamWriter f = new OutputStreamWriter(new FileOutputStream(newFile), "UTF-8");
            f.append(content);
            f.close();
        } catch (Exception e) {
            // This should never happen
            e.printStackTrace();
        }
    }

    public interface MatcherInterface {
        void onMatch(Matcher m);
    }

    public static void getMatches(String pattern, StringBuilder text, MatcherInterface mi) {
        Pattern r = Pattern.compile(pattern, Pattern.DOTALL);
        Matcher m = r.matcher(text);
        if (m.find()) {
            do {
                mi.onMatch(m);
            } while (m.find());
        } else {
            System.out.println("NO MATCH");
        }
    }

    public static void mkdirs(String s) {
        File file = new File(s);
        if (!file.exists())
            file.mkdirs();
    }

}
