package org.sonarsource.plugins.report;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class FileTest {

    private static void readFile(File file, int level) {
        if (file == null) {
            return;
        }
        System.out.println(String.format("%" + level + "s", file.getAbsolutePath()));
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

    @Test
    public void createFile() throws IOException {
        String path = "C:\\Users\\yangchengang2.CNSVWSH00\\Desktop\\tmp1\\tmp02";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        file = new File(path + "/file.txt");
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        System.out.println(file.getName());
    }

    public static void main(String[] args) {
//        String path = "D:\\IDEA projects\\sonar-report-plugin\\src";
//        readFile(new File(path), 1);

        System.out.println(System.getProperty("user.dir"));
    }
}
