package com.itrev.WebCloud.files;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.itrev.WebCloud.models.Item;
import com.itrev.WebCloud.exception.FileMemoryException;
import java.util.*;

/**
 *
 * @author Matt
 */
public class FileMemory {//класс, ответственный за размещение файлов в оперативной памяти
    private static Map<String, Item> fileSystem=new HashMap <>();//файловое хранилище

    public static void addFile(Item file) throws FileMemoryException{//добавление файла
        if(!fileSystem.containsKey(file.getTitle()))
            fileSystem.put(file.getTitle(), file);
        else throw new FileMemoryException("Попытка добавить существующий файл!");
    }
    public static Item readFile(String filename)throws FileMemoryException {//чтение файла
        if(!fileSystem.containsKey(filename)) throw new FileMemoryException("Файл с таким именем не существует!");
        return fileSystem.get(filename);
    }

    public static void removeFile(String filename)throws FileMemoryException {//удаление файла
        if(!fileSystem.containsKey(filename)) throw new FileMemoryException("Файл с таким именем не существует!");
        fileSystem.remove(filename);
    }
    public static void renameFile(String filename, String newfilename)throws FileMemoryException {//переименование файла
        if(!fileSystem.containsKey(filename)) throw new FileMemoryException("Файл с таким именем не существует!");
        Item temp = fileSystem.get(filename);
        String fName=temp.getTitle();
        String afterDot=fName.substring(fName.lastIndexOf(".")).toLowerCase();
        if(!newfilename.endsWith(afterDot)) newfilename+=afterDot;
        temp.setTitle(newfilename);
        temp.setChangeDate(new java.util.Date());
        temp.setLink("/d/"+newfilename);
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
    public static List<Item> getItemsInfoForJson(String info, Date fromd, Date tod, String datatype){
        List<Item> list = new ArrayList<>();
        for (Map.Entry<String, Item> entry : fileSystem.entrySet()) {
            Item  h = entry.getValue();
            if (h.getTitle().contains(info) &&
                    h.getChangeDate().after(fromd) &&
                    h.getChangeDate().before(tod) &&
                    h.getTitle().substring(h.getTitle().lastIndexOf(".")).toLowerCase().contains(datatype))
                list.add(h);

        }
        return list;
    }
    public static List<Item> getItemsInfoForJson(){
        List<Item> list = new ArrayList<>();
        for (Map.Entry<String, Item> entry : fileSystem.entrySet()) {
            Item  h = entry.getValue();
            list.add(h);
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
