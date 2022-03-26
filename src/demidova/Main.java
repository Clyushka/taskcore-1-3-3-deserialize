package demidova;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {

    public static void main(String[] args) {
        List<GameProgress> gp = new ArrayList() {{
            add(new GameProgress(10, 5, 3, 34.5));
            add(new GameProgress(100, 29, 15, 105.9));
            add(new GameProgress(200, 100, 76, 1046.98));
        }};

        File savegamesDir = new File("Games\\savegames");

        //serialize
        for (int i = 0; i < gp.size(); i++) {
            File saveDat = new File(savegamesDir, "save" + i + ".dat");

            try {
                saveDat.createNewFile();
                FileOutputStream fos = new FileOutputStream(saveDat);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(gp.get(i));
                oos.flush();
                fos.close();
                oos.close();
            } catch (IOException e) {
                System.out.println(e.getLocalizedMessage());
                System.exit(0);
            }
        }

        //ZIP
        File zip = new File(savegamesDir.getParent(), "savegames.zip");
        try (OutputStream fos = new FileOutputStream(zip);
             ZipOutputStream zos = new ZipOutputStream(fos)) {

            for (int i = 0; i < gp.size(); i++) {
                InputStream fis = new FileInputStream(new File(savegamesDir, "save" + i + ".dat"));
                ZipEntry zeGP = new ZipEntry("save" + i + "_bak.dat");
                zos.putNextEntry(zeGP);
                byte[] gpBytes = new byte[fis.available()];
                fis.read(gpBytes);
                zos.write(gpBytes);
                zos.closeEntry();
                fis.close();
            }
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }

        //delete non-ZIP
        for (int i = 0; i < gp.size(); i++) {
            new File(savegamesDir, "save" + i + ".dat").delete();
        }
    }
}
