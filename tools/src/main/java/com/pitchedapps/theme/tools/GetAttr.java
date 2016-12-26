package com.pitchedapps.theme.tools;

import java.util.HashMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.pitchedapps.theme.tools.Utils.print;

public class GetAttr {
    static TreeSet<String> valueSet = new TreeSet<>();
    static TreeSet<String> missing = new TreeSet<>();
    static HashMap<String, String> attrMap = new HashMap<>();
    static String attrDir = "C:\\Users\\User7681\\PA\\VTS\\ROM\\duContacts\\Data\\res\\values";
    static String dir = StyleSelfReference.dir;
    static String fileName = "styles.xml";
    static String attrPattern = "(<attr name=\"([^\\/]+?)\"[^\\/]+?\\/>)";
    static String attrItemPattern = "<item name=\"([^\\/]+?)\">[^<]+?<\\/item>";

    public static void main(String[] args) {
        getAttr();
        getItem();
        printResult();
    }

    public static void getAttr() {
        Pattern r = Pattern.compile(attrPattern, Pattern.DOTALL);
        Matcher m = r.matcher(Utils.readFile(attrDir, "attrs.xml"));
        if (m.find()) {
            do {
                attrMap.put(m.group(2), m.group(1));
            } while (m.find());
        } else {
            System.out.println("NO MATCH");
        }
    }

    public static void getItem() {
        Pattern r = Pattern.compile(attrItemPattern, Pattern.DOTALL);
        Matcher m = r.matcher(Utils.readFile(dir, fileName));
        if (m.find()) {
            do {
                if (m.group(1).startsWith("android:")) continue;
                valueSet.add(m.group(1));
            } while (m.find());
        } else {
            System.out.println("NO MATCH");
        }
    }

    public static void printResult() {
        for (String s : valueSet) {
            if (!attrMap.containsKey(s)) {
                missing.add(s);
                continue;
            }
            print(attrMap.get(s));
        }
        if (missing.isEmpty()) return;
        print("\nMISSING\n");
        for (String s : missing) {
            print(s);
        }
    }

}
