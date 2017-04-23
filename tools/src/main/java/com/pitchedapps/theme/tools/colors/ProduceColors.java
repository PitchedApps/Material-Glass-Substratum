package com.pitchedapps.theme.tools.colors;

import com.pitchedapps.theme.tools.utils.FileUtils;
import com.pitchedapps.theme.tools.utils.Utils;

/**
 * Created by Allan Wang on 2017-04-22.
 * <p>
 * When ready, creates production ready color.xml
 */

public class ProduceColors {

    public static void main(String[] args) {
        StringBuilder content = FileUtils.readFile(PortColors.writeDir, PortColors.COLORS_XML);
        if (content == null) {
            Utils.p("No color.xml ported yet");
            System.exit(0);
        }
        FileUtils.writeFile(FileUtils.filename(PortColors.themeDir, PortColors.DEFAULT_VALUES),
                PortColors.COLORS_XML, content.toString()
                        .replaceAll("(<!--#.*-->)", "") //remove pseudocomments
                        .replaceAll("(\n[\n\t ]+)", "\n\t")); //remove multiple newLines
    }

}
