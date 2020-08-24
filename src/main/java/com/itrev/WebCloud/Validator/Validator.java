/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itrev.WebCloud.Validator;
import java.util.List;
import java.util.Arrays;

/**
 *
 * @author Matt
 */
public class Validator {//Класс валидатора. Содержит методы проверки размера файла и формата файла.
    // Размер ограничен константой 15 Мбайт. Известные форматы находятся в validTypes.
    // Также валидатор хранит вспомогательное поле errorDescription, необходимое для вывода информации об ошибке на экран.
    private static List<String> validTypes=Arrays.asList(new String[]{".png",".bmp",".jpg",".jpeg",".docx",".doc",".xls",".xlsx",".ppt",".pptx",".pdf",".xml",".html",".mp3",".mp4"});
    private static String[] errorDescriptions={"","Ошибка валидации. Превышен максимальный размер файла (15 Мбайт)","Ошибка валидации. Невозможно определить тип файла"};
    public static boolean ValidateSize(long size){//размер
        return size<=15*(Math.pow(2, 20));
    }
    public static boolean ValidateType(String name){//формат
        return validTypes.contains(name.substring(name.lastIndexOf(".")).toLowerCase());
    }
    public static String getDescription(int code){//Вспомогательный метод для вывода информации об ошибке.
        // При использовании значения по умолчанию (0) возвращает пустую строку
        return errorDescriptions[code];
    }
}
