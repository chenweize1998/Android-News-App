package com.example.newstoday;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class ServerHttpResponse {

    private static ServerHttpResponse INSTANCE = null;
    private ServerHttpResponse(){

    }
    public static ServerHttpResponse getServerHttpResponse(){
        if(INSTANCE == null){
            INSTANCE = new ServerHttpResponse();
        }
        return INSTANCE;
    }



    public String getResponse(String oriUrl){
        try {
            GetHttpResponseTask getHttpResponseTask = new GetHttpResponseTask();
            String json = getHttpResponseTask.execute(oriUrl).get();
            return json;
        } catch (ExecutionException e){
            e.printStackTrace();
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        return null;
    }

    public String postResponse(String oriUrl, String data){
        try{
            PostHttpResponseTask postHttpResponseTask = new PostHttpResponseTask();
            return postHttpResponseTask.execute(oriUrl, data).get();
        }catch (ExecutionException e){
            e.printStackTrace();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        return null;
    }

    private String getHttpResponse(String allConfigUrl) {
        BufferedReader in = null;
        StringBuffer result = null;
        try {

            URL url = new URL(allConfigUrl);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestProperty("Charset", "UTF-8");
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(100);
            connection.connect();

            result = new StringBuffer();
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }

            return result.toString();

        }catch (SocketException e){
            return null;
        }
        catch (Exception e) {
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

    private class GetHttpResponseTask extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... url){
            return getHttpResponse(url[0]);
        }
    }



    private String postHttpResponse(String oriUrl, String data){

        try{

            URL url = new URL(oriUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty("encoding", "UTF-8");//添加请求属性
            connection.setDoInput(true);//允许输入
            connection.setDoOutput(true);//允许输出
            connection.setRequestMethod("POST");//POST请求 要在获取输入输出流之前设置  否则报错
            connection.setConnectTimeout(200);
            connection.connect();


            //输出
            OutputStream os;
            os = connection.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
            BufferedWriter bw = new BufferedWriter(osw);
            bw.write(data);
            bw.flush();

            //输入
            InputStream in = connection.getInputStream();
            InputStreamReader isr = new InputStreamReader(in,"UTF-8");
            BufferedReader br = new BufferedReader(isr);

            String line;
            StringBuilder sb = new StringBuilder();
            while((line = br.readLine()) != null)
            {
                sb.append(line);
            }
            bw.close();
            osw.close();
            os.close();
            br.close();
            isr.close();
            in.close();

            System.out.println(sb.toString());
            return sb.toString();
        }catch (SocketException e){
            return null;
        }catch(MalformedURLException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public class PostHttpResponseTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... objects){
            return postHttpResponse(objects[0], objects[1]);
        }
    }
}
