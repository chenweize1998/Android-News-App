package com.example.newstoday;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.ArraySet;

import androidx.annotation.Nullable;
import androidx.room.TypeConverter;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Set;

class Converter {
    @TypeConverter
    public static String[] fromTimestamp(String data){
        if(data == null){
            return null;
        }
        return data.split(",");
    }

    @TypeConverter
    public static String toTimestamp(String[] data){
        if(data == null){
            return null;
        }
        StringBuffer sb = new StringBuffer();
        for(String d:data){
            sb.append(d);
            sb.append(",");
        }
        return sb.toString();
    }
}

class ImageConverter{

    @TypeConverter
    public static Bitmap fromTimestamp(byte[] bytes){
        if(bytes == null){
            return null;
        }
        if (bytes.length != 0) {
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        } else {
            return null;
        }
    }

    @TypeConverter
    public static byte[] toTimestamp(Bitmap bitmap){
        if(bitmap == null){
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
}

class SetConverter{

    @TypeConverter
    @Nullable
    public static ArraySet<String> fromTimestamp(String data){
        if(data==null){
            return null;
        }
        String[] da = data.split(",");
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
        if(as==null){
            return null;
        }
        for(String a:as){
            sb.append(a);
            sb.append(",");
        }
        return sb.toString();
    }
}
