package com.pitchedapps.theme.tools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetAppcompatColors {
    static TreeSet<String> valueSet = new TreeSet<>();
    static String dir = "C:\\Users\\User7681\\PA\\VTS\\User\\PGML-v1.0\\Data\\res\\values";
    static String fileName = "colors.xml";
    static StringBuilder fullText = new StringBuilder();
    static String colorPattern = "<color name=\"([^\\/]+?)\">[^<]+?<\\/color>";

    public static void main(String[] args) {
//		dir = new File(HardcodeColors.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParent();
//		dir = dir.replace("%20", " ");

        readFile();
        getContent();
        printResult();
    }

    public static void getContent() {
        Pattern r = Pattern.compile(colorPattern, Pattern.DOTALL);
        Matcher m = r.matcher(fullText);
        if (m.find()) {
            do {
                valueSet.add(m.group(1));
            } while (m.find());
        } else {
            System.out.println("NO MATCH");
        }
    }


    public static void readFile() {
        String line = "";
        if (!dir.endsWith("\\") && !fileName.startsWith("\\")) dir = dir + "\\";
        try (BufferedReader br = new BufferedReader(new FileReader(dir + fileName))) {
            while ((line = br.readLine()) != null) {
                // process the line.
                fullText.append(line);
            }
        } catch (IOException e) {
            System.out.println("ERROR :" + e + "\n" + line);
        }
    }

    public static void printResult() {
        print("static String[] appcompatColorArray = new String[] {");
        for (String s : valueSet) {
            print("\t\"" + s + "\", ");
        }
        print("\t\"remove me\"\n};");
        print("static HashSet<String> appcompatColorSet = new HashSet<>(Arrays.asList(appcompatColorArray));");
//		print("static ")
//        System.out.println(valueSet);
    }

    private static void print(String s) {
        System.out.println(s);
    }
}
