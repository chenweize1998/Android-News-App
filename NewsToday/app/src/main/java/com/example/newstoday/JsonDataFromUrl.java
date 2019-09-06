package com.example.newstoday;
import android.os.AsyncTask;

import java.io.*;
import java.net.*;
import java.util.concurrent.TimeoutException;

import org.json.*;

public class JsonDataFromUrl extends AsyncTask<String, Void, JSONObject> {
    private JSONObject jsonObject;

    @Override
    protected JSONObject doInBackground(String... strings){
        try {
            String sizeUtf8 = (strings[0] == null)? "":URLEncoder.encode(strings[0], "utf-8");
            String startDateUtf8 = (strings[1] == null)? "":URLEncoder.encode(strings[1], "utf-8");
            String endDateUtf8 = (strings[2] == null)? "":URLEncoder.encode(strings[2], "utf-8");
            String wordsUtf8 = (strings[3] == null)? "":URLEncoder.encode(strings[3], "utf-8");
            String categoriesUtf8 = (strings[4] == null)? "":URLEncoder.encode(strings[4], "utf-8");
            String pageUtf8 = (strings[5] == null)?"":URLEncoder.encode(strings[5], "utf-8");
            String url = "https://api2.newsminer.net/svc/news/queryNewsList?size=" + sizeUtf8
                    + "&startDate=" + startDateUtf8 + "&endDate=" + endDateUtf8 + "&words=" + wordsUtf8
                    + "&categories=" + categoriesUtf8 + "&page=" + pageUtf8;
//            System.out.println(url);
            String json = getHttpResponse(url);
            if(json == null){
                return null;
            }
            JSONObject jsonObj = new JSONObject(json);
            return jsonObj;
//            this.jsonObject = jsonObj;

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e){
            e.printStackTrace();
        }

        return null;
    }

//    public static JSONObject getJsonData(final String size, final String startDate, final String endDate, final String words, final String categories) {
//        try {
//            String sizeUtf8 = (size == null)? "":URLEncoder.encode(size, "utf-8");
//            String startDateUtf8 = (startDate == null)? "":URLEncoder.encode(startDate, "utf-8");
//            String endDateUtf8 = (endDate == null)? "":URLEncoder.encode(endDate, "utf-8");
//            String wordsUtf8 = (words == null)? "":URLEncoder.encode(words, "utf-8");
//            String categoriesUtf8 = (categories == null)? "":URLEncoder.encode(categories, "utf-8");
//            String url = "https://api2.newsminer.net/svc/news/queryNewsList?size=" + sizeUtf8 + "&startDate=" + startDateUtf8 + "&endDate=" + endDateUtf8 + "&words=" + wordsUtf8 + "&categories=" + categoriesUtf8;
//            String json = getHttpResponse(url);
//            JSONObject jsonObj = new JSONObject(json);
//            return jsonObj;
//
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        } catch (JSONException e){
//            e.printStackTrace();
//        }
//
//        return null;
//    }

    private static String getHttpResponse(String allConfigUrl) {
        BufferedReader in = null;
        StringBuffer result = null;
        try {

            URL url = new URL(allConfigUrl);
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("Charset", "UTF-8");
            connection.setConnectTimeout(500);
            connection.setReadTimeout(500);
//            System.out.println("Before connect");
            connection.connect();
//            System.out.println("After connect");

            result = new StringBuffer();
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }

            return result.toString();

        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

        return null;

    }
}
