package com.example.newstoday;
import java.io.*;
import java.net.*;
import org.json.*;

public class JsonDataFromUrl {

    public static JSONObject getJsonData(final String size, final String startDate, final String endDate, final String words, final String categories) {
        try {
            String sizeUtf8 = (size == null)? "":URLEncoder.encode(size, "utf-8");
            String startDateUtf8 = (startDate == null)? "":URLEncoder.encode(startDate, "utf-8");
            String endDateUtf8 = (endDate == null)? "":URLEncoder.encode(endDate, "utf-8");
            String wordsUtf8 = (words == null)? "":URLEncoder.encode(words, "utf-8");
            String categoriesUtf8 = (categories == null)? "":URLEncoder.encode(categories, "utf-8");
            String url = "https://api2.newsminer.net/svc/news/queryNewsList?size=" + sizeUtf8 + "&startDate=" + startDateUtf8 + "&endDate=" + endDateUtf8 + "&words=" + wordsUtf8 + "&categories=" + categoriesUtf8;
            String json = getHttpResponse(url);
            JSONObject jsonObj = new JSONObject(json);
            return jsonObj;

        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e){
            e.printStackTrace();
        }

        return null;
    }

    private static String getHttpResponse(String allConfigUrl) {
        BufferedReader in = null;
        StringBuffer result = null;
        try {

            URL url = new URL(allConfigUrl);
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("Charset", "gbk");

            connection.connect();

            result = new StringBuffer();
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }

            return result.toString();

        } catch (Exception e) {
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
