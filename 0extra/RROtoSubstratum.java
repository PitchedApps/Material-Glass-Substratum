package android;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class RROtoSubstratum {
    public static void main(String[] args) {
        File file = new File("C:\\Users\\user1\\PA\\MGL\\substratum\\src\\main\\assets\\overlays");
        File[] list = file.listFiles();
        for (File f : list) {
            String manifestText = readFile(f.getAbsolutePath() + "/AndroidManifest.xml");
            if (manifestText == null) continue;
            int i = manifestText.indexOf("android:targetPackage=\"");
            int ii = manifestText.indexOf("\" android:priority");
            String packageName = manifestText.substring(i+23, ii);
            System.out.println(packageName);
            if(!f.renameTo(new File(file.getAbsolutePath() + "/" + packageName))) {
                System.out.println("fail");
            }
        }
    }

    public static String readFile(String filename) {
        String content = null;
        File file = new File(filename); //for ex foo.txt
        if (!file.exists()) return null;
        FileReader reader = null;
        try {
            reader = new FileReader(file);
            char[] chars = new char[(int) file.length()];
            reader.read(chars);
            content = new String(chars);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        file.delete();
        return content;
    }
}
