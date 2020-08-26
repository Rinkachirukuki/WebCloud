package com.itrev.WebCloud.Archiver;
import com.itrev.WebCloud.Files.FileMemory.FileManager;
import com.itrev.WebCloud.Models.Item;
import org.springframework.core.io.InputStreamResource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.util.List;
import java.util.zip.*;
public class Archiver {
    public static InputStreamResource makeArchive(List<String> names) throws Exception{
        String outname="Archiev.zip";
        File temp=new File(outname);
        if (temp.exists()){
            temp.delete();
            temp.createNewFile();
        }
        ZipOutputStream archive=new ZipOutputStream(new FileOutputStream(outname));
        for (String n: names) {
            byte[] buff= FileManager.ReadFile(n).getFile();
            ZipEntry entry=new ZipEntry(n);
            archive.putNextEntry(entry);
            archive.write(buff);
            archive.closeEntry();
        }
        archive.close();
        InputStreamResource arc=new InputStreamResource(new FileInputStream(outname));

        return arc;
    }
}
