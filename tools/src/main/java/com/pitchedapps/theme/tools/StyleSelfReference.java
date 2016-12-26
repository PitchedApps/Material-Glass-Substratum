package com.pitchedapps.theme.tools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Allan Wang on 2016-08-23.
 */
public class StyleSelfReference {

    public static String dir = Config.getSubTest(),
            patternStyle = "<style name=\"([^\\/]+?)\" parent=\"self\">",
            patternItem = "<item name=\"([^\\/]+?)\">([^!]+?)</item>",
    patternItemFormat = "\t\t<item name=\"%1$s\">%2$s</item>";

    public static void main(String[] args) {
        String packageName = getPackageName(dir);
        Pattern itemPattern = Pattern.compile(patternItem);

        StringBuilder s = new StringBuilder();
        String line = "";
        try (BufferedReader br = new BufferedReader(new FileReader(dir + "\\styles.xml"))) {
            while ((line = br.readLine()) != null) {
                if (!line.startsWith("<?xml")) {
                    s.append("\n");
                }
//                if (line.matches(patternStyle)) {
//                    Utils.print("MAT");
                    line = line.replaceAll(patternStyle, "<style name=\"$1\" parent=\"@*" + packageName + ":style/$1\">");
//                } else {
                    Matcher m = itemPattern.matcher(line);
                    if (m.find()) {
                        String attr = m.group(1);
                        String item = m.group(2);
//                        if (!attr.startsWith("android:")) {
//                            attr = "@*" + packageName + ":" + attr;
//                        }
                        if (item.startsWith("@color") || item.startsWith("@dimen") || item.startsWith("@style") || item.startsWith("@drawable") || item.startsWith("@integer")) {
                            item = "@*" + packageName + ":" + item.substring(1);
                        }
                        line = String.format(Locale.CANADA, patternItemFormat, attr, item);
                    }
//                }
                s.append(line);
            }
        } catch (IOException e) {
            System.out.println("ERROR :" + e + "\n" + line);
        }
        Utils.writeFile(dir, "styles_new.xml", s);
    }

    private static String getPackageName(String s) {
        String start = "overlays\\";
        String end = "\\res";
        int first = s.indexOf(start);
        int last = s.indexOf(end);
        if (first == -1 || last == -1) return "test";
        return s.substring(first + start.length(), last);
    }
}
