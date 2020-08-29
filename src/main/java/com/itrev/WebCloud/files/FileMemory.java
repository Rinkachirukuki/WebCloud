package com.itrev.WebCloud.files;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.itrev.WebCloud.models.Item;

import java.util.*;

/**
 *
 * @author Matt
 */
public class FileMemory {//класс, ответственный за размещение файлов в оперативной памяти
    public static class MemoryException extends Exception{
        public MemoryException() {
        }

        public MemoryException(String message) {
            super(message);
        }
    }
    private static Map<String, Item> fileSystem=new HashMap <>();//файловое хранилище

    public static void addFile(Item file){//добавление файла
        if(!fileSystem.containsKey(file.getTitle()))
            fileSystem.put(file.getTitle(), file);
    }
    public static Item readFile(String filename)throws MemoryException{//чтение файла
        if(!fileSystem.containsKey(filename)) throw new MemoryException("Файл с таким именем не существует!");
        return fileSystem.get(filename);
    }

    public static void removeFile(String filename)throws MemoryException{//удаление файла
        if(!fileSystem.containsKey(filename)) throw new MemoryException("Файл с таким именем не существует!");
        fileSystem.remove(filename);
    }
    public static void renameFile(String filename, String newfilename)throws MemoryException{//переименование файла
        if(!fileSystem.containsKey(filename)) throw new MemoryException("Файл с таким именем не существует!");
        Item temp = fileSystem.get(filename);
        temp.setTitle(newfilename);
        temp.setChangeDate(new java.util.Date());
        fileSystem.remove(filename);
        fileSystem.put(newfilename, temp);
    }
    public static String[] getFileNames(){
        String[] a=new String[fileSystem.size()];
        a=fileSystem.keySet().toArray(a);
        Arrays.sort(a);
        return a;
    }
    public static List<String[]> getItemsInfo(){
        List<String[]> list = new ArrayList<>();
        for (Map.Entry<String, Item> entry : fileSystem.entrySet()) {
            list.add(entry.getValue().toStringArray());
        }
        return list;
    }
    public static List<String[]> getItemsInfo(String info, Date fromd, Date tod, String datatype){
        List<String[]> list = new ArrayList<>();
        for (Map.Entry<String, Item> entry : fileSystem.entrySet()) {
            Item  h = entry.getValue();
            if (h.getTitle().contains(info) &&
                    h.getChangeDate().after(fromd) &&
                    h.getChangeDate().before(tod) &&
                    h.getTitle().substring(h.getTitle().lastIndexOf(".")).toLowerCase().contains(datatype))
            {
                list.add(h.toStringArray());
            }
        }
        return list;
    }
    public static List<String> getItemsTypes(){
        List<String> list = new ArrayList<>();
        for (Map.Entry<String, Item> entry : fileSystem.entrySet()) {
            Item  h = entry.getValue();
            String dataType = h.getTitle().substring(h.getTitle().lastIndexOf(".")).toLowerCase();
            if(!list.contains(dataType)){
                list.add(dataType);
            }
        }
        return list;
    }
}
