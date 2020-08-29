package com.itrev.WebCloud.archiver;
import com.itrev.WebCloud.files.FileMemory;
import org.springframework.core.io.InputStreamResource;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.zip.*;
public class Archiver {
    private static Path tempDir =Paths.get(Paths.get("").toAbsolutePath()+"\\temp");
    private static Path archive= Paths.get(tempDir +"\\archive.zip");
    private static String makeArchive(List<String> names) throws Exception{
        prepareTempDir();
        String outname=archive.toString();
        ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(outname));
            for (String n : names) {
                byte[] buff = FileMemory.readFile(n).getFile();
                ZipEntry entry = new ZipEntry(n);
                zout.putNextEntry(entry);
                zout.write(buff);
                zout.closeEntry();
            }
        zout.close();
        return outname;
    }
    public static InputStreamResource getArchive(List<String> names)throws Exception{
        FileInputStream fin = new FileInputStream(makeArchive(names));
        byte[] archive = fin.readAllBytes();
        fin.close();
        clearTempDir();
        return new InputStreamResource(new ByteArrayInputStream(archive));
    }
    private static void prepareTempDir() throws IOException{
        if(!Files.exists(tempDir)) Files.createDirectory(tempDir);
        Files.deleteIfExists(archive);
        Files.createFile(archive);
    }
    public static void clearTempDir() throws IOException {
        Files.deleteIfExists(archive);
        Files.deleteIfExists(tempDir);
    }

}
