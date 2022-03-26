package demidova;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Main {

    public static void main(String[] args) {
        int filesCount = 0;

        //unzip
        File zip = new File("Games", "savegames.zip");
        File savegamesDir = new File(zip.getPath().substring(0, zip.getPath().length() - 4));

        try (InputStream fis = new FileInputStream(zip);
             ZipInputStream zis = new ZipInputStream(fis)) {

            for (ZipEntry ze = zis.getNextEntry(); ze != null; ze = zis.getNextEntry(), filesCount++) {
                String name = ze.getName().replaceAll("_bak", "");
                File dat = new File(savegamesDir, name);
                OutputStream fos = new FileOutputStream(dat);

                for (int bytes = zis.read(); bytes != -1; bytes = zis.read()) {
                    fos.write(bytes);
                }

                fos.flush();
                zis.closeEntry();
                fos.close();
            }
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }

        //deserialize all files
        List<GameProgress> gp = new ArrayList<>();
        for (File file : savegamesDir.listFiles()) {
            if (file.isFile() && file.getName().contains(".dat")) {
                try (InputStream fis = new FileInputStream(file);
                     ObjectInputStream ois = new ObjectInputStream(fis)) {
                    gp.add((GameProgress) ois.readObject());
                } catch (Exception e) {
                    System.out.println(e.getLocalizedMessage());
                }
            }
        }

        gp.stream().forEach(System.out::println);
    }
}
