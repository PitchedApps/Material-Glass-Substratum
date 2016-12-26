package com.pitchedapps.theme.tools;

import java.util.TreeMap;
import java.util.TreeSet;

public class Test {
    static String stockDir = "C:\\Users\\User7681\\PA\\VTS\\ROM\\cm12-framework-res\\Data\\res\\values";
    static String dir = "C:\\Users\\User7681\\PA\\PZ\\theme\\src\\main\\assets\\overlays\\common\\res\\values";
    static String itemPattern = "(<item name=\"([^\\/]+?)\">[^<]+?<\\/item>)";
    static String attrPattern = "<attr name=\"([^\\/]+?)\"[^\\/]+?\\/>";

    public static void main(String[] args) {

        //initialize variables
        TreeMap<String, String> newMap = new TreeMap<>();
        TreeSet<String> set = new TreeSet<>();
        TreeSet<String> removed = new TreeSet<>();
        TreeMap<String, String> map = new TreeMap<>();

        ///get currently themed colors
        Utils.getMatches(itemPattern, Utils.readFile(dir, "test.xml"), m -> {
            if (!Utils.isAppcompatColor(m.group(2))) {
                map.put(m.group(2), m.group(1));
            }
        });
        //get new stock file
        Utils.getMatches(attrPattern, Utils.readFile(stockDir, "attrs.xml"), m -> {
//            if (!Utils.isAppcompatColor(m.group(2))) {
            set.add(m.group(1));
//            }
        });
        //get removed
        StringBuilder ss = new StringBuilder();
        for (String s : map.keySet()) {
            ss.append("\n");
            if (set.contains(s)) {
                ss.append("<item name=\"").append(s).append("\">?android:").append(s).append("</item>");
            } else {
                ss.append(map.get(s));
            }
        }
        Utils.writeFile(dir, "new.xml", ss);
//        Utils.printSet("Missing", missing);
//        Utils.printSet("Removed", removed);

    }

}
