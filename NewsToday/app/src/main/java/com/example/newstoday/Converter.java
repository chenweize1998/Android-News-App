package com.example.newstoday;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.ArrayMap;
import android.util.ArraySet;

import androidx.annotation.Nullable;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

class Converter {
    @TypeConverter
    public static String[] fromTimestamp(String data){
        if(data == null || data.equals("")){
            return new String[0];
        }
        return data.split("#\\^#");
    }

    @TypeConverter
    public static String toTimestamp(String[] data){
        if(data == null || data.length==0){
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for(String d:data){
            sb.append(d);
            sb.append("#^#");
        }
        return sb.toString();
    }
}

class SetConverter{

    @TypeConverter
    @Nullable
    public static ArraySet<String> fromTimestamp(String data){
        if(data==null || data.length() == 0){
            return new ArraySet<>();
        }
        String[] da = data.split("#\\^#");
        ArraySet as = new ArraySet();
        for(String d:da) {
            as.add(d);
        }
        return as;
    }

    @TypeConverter
    @Nullable
    public static String toTimestamp(ArraySet<String> as){
        StringBuffer sb = new StringBuffer();
        if(as==null || as.size() == 0){
            return "";
        }
        for(String a:as){
            sb.append(a);
            sb.append("#^#");
        }
        return sb.toString();
    }
}

class ListConverter{
    @TypeConverter
    @Nullable
    public static ArrayList<String> fromTimestamp(String data){
        if(data==null || data.length() == 0){
            return new ArrayList<>();
        }
        String[] da = data.split("#\\^#");
        ArrayList al = new ArrayList();
        for(String d:da) {
            al.add(d);
        }
        return al;
    }

    @TypeConverter
    @Nullable
    public static String toTimestamp(ArrayList<String> al){
        StringBuffer sb = new StringBuffer();
        if(al==null || al.size() == 0){
            return "";
        }
        for(String a:al){
            sb.append(a);
            sb.append("#^#");
        }
        return sb.toString();
    }

}

