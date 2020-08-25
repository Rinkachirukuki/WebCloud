package com.itrev.WebCloud.Archiver;
import com.itrev.WebCloud.Files.FileMemory.FileManager;
import com.itrev.WebCloud.Models.Item;
import org.springframework.core.io.InputStreamResource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.*;
public class Archiver {
    public static InputStreamResource makeArchive(String[] names) throws Exception{
        ZipOutputStream archive=new ZipOutputStream(new FileOutputStream("/output.zip"));
        for (String n: names) {
            byte[] temp= FileManager.ReadFile(n).getFile();
            ZipEntry entry=new ZipEntry(n);
            archive.putNextEntry(entry);
            archive.write(temp);
            archive.closeEntry();
        }
        archive.close();
        InputStreamResource nova=new InputStreamResource(new FileInputStream("/output.zip"));



        return null;
    }
}
