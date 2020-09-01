/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itrev.WebCloud.validator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Arrays;
import org.springframework.validation.*;
/**
 *
 * @author Matt
 */
@Service
public class Validator  {//Класс валидатора. Содержит методы проверки размера файла и формата файла.
    // Размер ограничен константой 15 Мбайт. Известные форматы находятся в validTypes.
    // Также валидатор хранит вспомогательное поле errorDescription, необходимое для вывода информации об ошибке на экран.
    private static final List<String> validTypes=Arrays.asList(new String[]{".png",".bmp",".jpg",".jpeg",".docx",".doc",".xls",".xlsx",".ppt",".pptx",".pdf",".xml",".html",".mp3",".mp4",".zip"});
    private static final long fileSize=15728640;//15*1024*1024 байт
    public static boolean validateSize(long size){//размер
        return size<=fileSize;
    }
    public static boolean validateType(String name){//формат
        return validTypes.contains(name.substring(name.lastIndexOf(".")).toLowerCase());
    }
}
