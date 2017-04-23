package com.pitchedapps.theme.tools;

import org.apache.commons.io.FileUtils;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

import javax.imageio.ImageIO;

public class CreateTintable {
    static String dir = Config.getSubTestRes();
    static boolean justGet9Patch = false;

    static HashSet<String> fileSet = new HashSet<>();
    static String[] validDirectories = new String[]{
            "drawable-xxxhdpi", "drawable-xxhdpi", "drawable-xhdpi", "drawable-hdpi", "drawable-mdpi", "drawable-ldpi"
    };
    static int duplicateCount = 0, ninePatchCount = 0;
    static final String
            bitmapFormat = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<bitmap xmlns:android=\"http://schemas.android.com/apk/res/android\"\n\tandroid:src=\"@drawable/%1$s\"\n\tandroid:tint=\"@*common:color/zed_text\" />",
            ninepatchFormat = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<nine-patch\n\txmlns:android=\"http://schemas.android.com/apk/res/android\"\n\tandroid:src=\"@drawable/%1$s\"\n\tandroid:tint=\"@*common:color/zed_heavy_background\"\n\tandroid:dither=\"true\" />";

    public static class Alphabetic implements Comparator<File> {

        @Override
        public int compare(File a, File b) {
            return a.getName().compareTo(b.getName());
        }
    }

    public static void main(String[] args) {
//		dir = new File(HardcodeColors.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParent();
//		dir = dir.replace("%20", " ");
        if (!justGet9Patch) file("new/drawable").mkdirs();
        File[] dirs = file().listFiles();
        if (dirs == null || dirs.length == 0) return;
        String[] dirNames = new String[dirs.length];
        for (int i = 0; i < dirs.length; i++) {
            dirNames[i] = dirs[i].getName();
        }
        Arrays.sort(dirNames, Collections.reverseOrder(String.CASE_INSENSITIVE_ORDER));
        for (String s : dirNames) {
            if (!file(s).isDirectory()) continue;
            for (String ss : validDirectories) {
                if (s.startsWith(ss)) {
                    getDrawables(file(s));
                }
            }
        }
//        for (String s : validDirectories) {
//            if (file(s).exists()) getDrawables(file(s));
//        }
        print(duplicateCount + " Duplicates", ninePatchCount + " Nine Patch Drawables");
    }

    private static void getDrawables(File directory) {
        if (!justGet9Patch) file("new", directory.getName()).mkdirs();
        for (File f : directory.listFiles()) {
            modifyDrawable(f);
        }
    }

    private static File file(String... ss) {
        StringBuilder directory = new StringBuilder().append(dir);
        for (String s : ss) {
            directory.append("/").append(s);
        }
        return new File(directory.toString());
    }

    private static void modifyDrawable(File image) {
        String name = image.getName();
        if (!name.endsWith("png")) return;
        if (fileSet.contains(name)) {
            duplicateCount++;
            return;
        }
        if (name.equals("aaa.png")) return;
        fileSet.add(name);
        if (name.endsWith("9.png")) {
            if (ninePatchCount == 0) {
                file("new/ninepatch/drawable").mkdirs();
                file("new/ninepatch/original").mkdirs();
            }
            ninePatchCount++;
            try {
                FileUtils.copyFile(image, file("new/ninepatch/original", image.getParentFile().getName(), name));
            } catch (IOException e) {
                e.printStackTrace();
                print("Error modifyDrawable", name);
            }

            writeTintedImage(image, file("new/ninepatch", image.getParentFile().getName(), name.replace(".9.png", "_bm.9.png")));
            writeFile(file("new/ninepatch/drawable", name.replace(".9.png", ".xml")), String.format(ninepatchFormat, name.substring(0, name.length() - 6) + "_bm"));
        } else if (!justGet9Patch && name.endsWith(".png")) {
            writeTintedImage(image, file("new", image.getParentFile().getName(), name.replace(".png", "_bm.png")));
            writeFile(file("new/drawable", name.replace(".png", ".xml")), String.format(bitmapFormat, name.substring(0, name.length() - 4) + "_bm"));
        }
    }

    private static void writeTintedImage(File image, File dest) {
        BufferedImage rgb = getTintedImage(image);
        dest.mkdirs();

        try {
            ImageIO.write(rgb, "png", dest);
        } catch (IOException e) {
            e.printStackTrace();
            print("Error modifyDrawable write", image.getName());
        }
    }

    private static BufferedImage getTintedImage(File image) {
        BufferedImage rgb = toRGB(image);
        if (rgb != null) {

            for (int x = 0; x < rgb.getWidth(); x++) {
                for (int y = 0; y < rgb.getHeight(); y++) {
                    Color color = new Color(rgb.getRGB(x, y), true);
                    Color black = new Color(0, 0, 0, color.getAlpha());
                    rgb.setRGB(x, y, black.getRGB());
                    color.getAlpha();
                }
            }
        }
        return rgb;
    }

    public static BufferedImage toRGB(File f) {
        try {
            Image i = ImageIO.read(f);
            BufferedImage rgb = new BufferedImage(i.getWidth(null), i.getHeight(null), BufferedImage.TYPE_INT_ARGB);
            rgb.createGraphics().drawImage(i, 0, 0, null);
            return rgb;
        } catch (IOException e) {
            e.printStackTrace();
            print("Error toRGB", f.getName());
            return null;
        }

    }

    private static void print(Object... oo) {
        for (Object o : oo) {
            System.out.println(String.valueOf(o));
        }
    }

    private static void writeFile(File f, String t) {
        try {
            BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(f));
            os.write(t.getBytes());
            os.flush();
            os.close();
        } catch (IOException e) {
            System.out.println("File Writing Error " + e);
            e.printStackTrace();
        }

    }

}
