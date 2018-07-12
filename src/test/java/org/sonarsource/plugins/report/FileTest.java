package org.sonarsource.plugins.report;

import java.io.File;

public class FileTest {

    private static void readFile(File file, int level) {
        if (file == null) {
            return;
        }
        System.out.println(String.format("%"+level+"s",  file.getAbsolutePath()));
        if (file.isDirectory()) {
            File[] subFiles = file.listFiles();
            if (subFiles.length == 0) {
                return;
            }
            for (File subFile : subFiles) {
                readFile(subFile, ++level);
            }
        }
    }

    public static void main(String[] args) {
        String path = "D:\\IDEA projects\\sonar-report-plugin\\src";
        readFile(new File(path), 1);
    }
}
