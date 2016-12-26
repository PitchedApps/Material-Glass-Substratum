package com.pitchedapps.theme.tools;

import java.util.HashMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.pitchedapps.theme.tools.Utils.print;

public class GetDimen {
    static TreeSet<String> valueSet = new TreeSet<>();
    static TreeSet<String> missing = new TreeSet<>();
    static HashMap<String, String> dimenMap = new HashMap<>();
    static String attrDir = "C:\\Users\\User7681\\PA\\VTS\\ROM\\DialerCM12\\Data\\res\\values";
    static String dir = "C:\\Users\\User7681\\PA\\PZ\\theme\\src\\main\\assets\\overlays\\com.android.dialer\\res\\values-v21";
    static String fileName = "styles.xml";
    static String dimenPattern = "(<dimen name=\"([^\\/]+?)\">[^<]+?<\\/dimen>)";
    static String dimenItemPattern = "@dimen\\/([^\\/]+?)<\\/item>";

    public static void main(String[] args) {
        getDimen();
        getItem();
        printResult();
    }

    public static void getDimen() {
        Pattern r = Pattern.compile(dimenPattern, Pattern.DOTALL);
        Matcher m = r.matcher(Utils.readFile(attrDir, "dimens.xml"));
        if (m.find()) {
            do {
                dimenMap.put(m.group(2), m.group(1));
            } while (m.find());
        } else {
            System.out.println("NO MATCH");
        }
    }

    public static void getItem() {
        Pattern r = Pattern.compile(dimenItemPattern, Pattern.DOTALL);
        Matcher m = r.matcher(Utils.readFile(dir, fileName));
        if (m.find()) {
            do {
                valueSet.add(m.group(1));
            } while (m.find());
        } else {
            System.out.println("NO MATCH");
        }
    }

    public static void printResult() {
        for (String s : valueSet) {
            if (!dimenMap.containsKey(s)) {
                missing.add(s);
                continue;
            }
            print(dimenMap.get(s));
        }
        if (missing.isEmpty()) return;
        print("\nMISSING\n");
        for (String s : missing) {
            print(s);
        }
    }

}
