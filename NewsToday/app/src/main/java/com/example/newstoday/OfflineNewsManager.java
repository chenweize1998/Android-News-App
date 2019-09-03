package com.example.newstoday;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class OfflineNewsManager {


    private static OfflineNewsManager INSTANCE = null;
    private AppDB offlineNewsDB;
    private NewsDao offlineNewsDao;
    private NewsRepository newsRepository;


    private OfflineNewsManager(Context context){
        offlineNewsDB = AppDB.getAppDB(context, "offlineNews");
        offlineNewsDao = offlineNewsDB.newsDao();
        newsRepository = new NewsRepository(offlineNewsDB);
    }

    public static OfflineNewsManager getOfflineNewsManager(Context context){
        if(INSTANCE == null){
            INSTANCE = new OfflineNewsManager(context);
        }
        return INSTANCE;
    }

    public void addOneOfflineNews(News... news){
        downloadNewsPictureToLocalFile(news[0]);
        newsRepository.insertNews(news);
        System.out.println("新闻保存到本地成功");
    }

    public ArrayList<News> getAllOfflineNews(){
        return newsRepository.getAllNews();
    }

    public void deleteAllofflineNews(){
        newsRepository.clearNews();
    }

    private void downloadNewsPictureToLocalFile(News news){
        DownloadPictureFromURLToURITsak downloadPictureFromURLToURITsak = new DownloadPictureFromURLToURITsak();
        String[] urls = news.getImage();
        String[] uris = new String[urls.length];
        for(int i = 0; i<urls.length; i++){
            String uri = System.currentTimeMillis() +".jpg";
            uris[i] = uri;
            downloadPictureFromURLToURITsak.oriUrl = urls[i];
            downloadPictureFromURLToURITsak.uri = uri;
            Thread thread = new Thread(downloadPictureFromURLToURITsak);
            thread.start();
        }
        news.setImage(uris);
    }

    private class DownloadPictureFromURLToURITsak implements Runnable {

        public String oriUrl;
        public String uri;

        @Override
        public void run(){
            URL url = null;
            try {
                url = new URL(oriUrl);
                DataInputStream dataInputStream = new DataInputStream(url.openStream());

                FileOutputStream fileOutputStream = new FileOutputStream(new File(uri));
                ByteArrayOutputStream output = new ByteArrayOutputStream();

                byte[] buffer = new byte[2048];
                int length;

                while ((length = dataInputStream.read(buffer)) > 0) {
                    output.write(buffer, 0, length);
                }
                fileOutputStream.write(output.toByteArray());
                dataInputStream.close();
                fileOutputStream.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
