package com.itrev.WebCloud.Files;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.itrev.WebCloud.Models.Item;
import java.util.Map;
import java.util.HashMap;
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
        private static void RenameFile(String fileName)throws Exception{
            if(!fileSystem.containsKey(fileName)) throw new Exception("Файл с таким именем не существует!");
            Item temp = fileSystem.get(fileName);
            temp.setTitle(fileName);
            fileSystem.put(fileName, temp);
        }
        
    }
}
