package com.example.fashionapp.dataLocal;

import androidx.room.TypeConverter;

import java.util.ArrayList;
import java.util.List;

public class ConvertersString {
    @TypeConverter
    public static List<String> fromString(String value) {
        // Chuyển đổi từ chuỗi thành danh sách
        // Ví dụ: "a,b,c" -> [a, b, c]
        List<String> list = new ArrayList<>();
        if (value != null && !value.isEmpty()) {
            String[] items = value.split(",");
            for (String item : items) {
                list.add(item.trim());
            }
        }
        return list;
    }

    @TypeConverter
    public static String listToString(List<String> list) {
        // Chuyển đổi từ danh sách thành chuỗi
        // Ví dụ: [a, b, c] -> "a,b,c"
        StringBuilder sb = new StringBuilder();
        if (list != null && !list.isEmpty()) {
            for (String item : list) {
                sb.append(item).append(",");
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }
}

