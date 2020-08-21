package com.itrev.WebCloud.Files;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.itrev.WebCloud.Models.Item;
import com.sun.xml.bind.v2.runtime.unmarshaller.XsiNilLoader;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.Arrays;
/**
 *
 * @author Matt
 */
public class FileMemory {
    public static class FileManager{
        private static Map<String, Item> fileSystem=new HashMap <String,Item>();
   
        public static void AddFile(Item file){
            if(!fileSystem.containsKey(file.getTitle())) 
                fileSystem.put(file.getTitle(), file);
        }
        public static Item ReadFile(String fileName)throws Exception{
            if(!fileSystem.containsKey(fileName)) throw new Exception("Файл с таким именем не существует!");
            return fileSystem.get(fileName);
        }
        private static void RemoveFile(String fileName)throws Exception{
            if(!fileSystem.containsKey(fileName)) throw new Exception("Файл с таким именем не существует!");
            fileSystem.remove(fileName);
        }
        public static void RenameFile(String fileName, String newFileName)throws Exception{
            if(!fileSystem.containsKey(fileName)) throw new Exception("Файл с таким именем не существует!");
            Item temp = fileSystem.get(fileName);
            String ex=temp.getTitle();
            //String type= ex.split(".")[1];
            fileSystem.remove(ex);
            fileSystem.put(newFileName, temp);
        }
        public static String[] GetFileNames(){
            String[] a=new String[fileSystem.size()];
            a=fileSystem.keySet().toArray(a);
            Arrays.sort(a);
            return a;
        }
        
    }
}
