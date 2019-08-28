package com.example.newstoday;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class UserManagerOnServer {


    public boolean userSignUp(String name, String password){
        try{
            PostHttpResponseTask postHttpResponseTask = new PostHttpResponseTask();
            String res = postHttpResponseTask.execute("http://183.172.218.1:8000/signUp", name, password).get();
            if(res.equals("Success")){
                return true;
            }
        }catch (ExecutionException e){
            e.printStackTrace();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean userSignIn(String name, String password){
        try{
            PostHttpResponseTask postHttpResponseTask = new PostHttpResponseTask();
            String res = postHttpResponseTask.execute("http://183.172.218.1:8000/signIn", name, password).get();
            if(res.equals("Success")){
                return true;
            }
        }catch (ExecutionException e){
            e.printStackTrace();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        return false;
    }

    private String postHttpResponse(String oriUrl, String name, String password){

        String user = "name="+name+"&password="+password;
        try{

            URL url = new URL(oriUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty("encoding", "UTF-8");//添加请求属性
            connection.setDoInput(true);//允许输入
            connection.setDoOutput(true);//允许输出
            connection.setRequestMethod("POST");//POST请求 要在获取输入输出流之前设置  否则报错


            //输出
            OutputStream os;
            os = connection.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osw);
            bw.write(user);
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
        }catch(MalformedURLException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }

    private class PostHttpResponseTask extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... params){
            return postHttpResponse(params[0], params[1], params[2]);
        }
    }

}
