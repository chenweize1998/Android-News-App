package com.example.newstoday;

import android.content.Context;
import android.os.Environment;

import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ImagePoster {


    public static ImagePoster INSTANCE = null;
    public UserManager userManager;
    public UserMessageManager userMessageManager;

    private ImagePoster(Context context){
        userManager = UserManager.getUserManager(context);
        userMessageManager = UserMessageManager.getUserMessageManager(context);
    }

    public static ImagePoster getImagePoster(Context context){
        if(INSTANCE == null){
            INSTANCE = new ImagePoster(context);
        }
        return INSTANCE;
    }

    public boolean postAvaterToServer(String imagePath, User user){
        String filename = imagePath.substring(imagePath.lastIndexOf("/") + 1);
        boolean res = postImage(imagePath);
        String url = "http://166.111.5.239:8000/downloadImage/?filename="+filename;
        userManager.setUserAvatar(user, url);
        return res;
    }

    public String[] postUserImageToServer(String[] imagePaths){
        String[] urls = new String[imagePaths.length];
        for(int i = 0; i<imagePaths.length; i++){
            String filename = imagePaths[i].substring(imagePaths[i].lastIndexOf("/") + 1);
            if(!postImage(imagePaths[i])) {
                return null;
            }
            urls[i] = "http://166.111.5.239:8000/downloadImage/?filename="+filename;
        }
        return urls;
    }

    /**
     * 这个函数用来上传图片，你只需要传递进来文件的本地路径即可
     * 之后你就可以通过picasso直接从服务器下载图片
     * */
    private boolean postImage(String imagePath){
        //1.创建OkHttpClient对象
        OkHttpClient  okHttpClient = new OkHttpClient();
        //上传的图片
        String url = "http://166.111.5.239:8000/uploadImage/";
        File file = new File(imagePath);
        //2.通过RequestBody.create 创建requestBody对象,application/octet-stream 表示文件是任意二进制数据流
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("filename", imagePath)
                .addFormDataPart("image", imagePath.substring(imagePath.lastIndexOf("/") + 1), RequestBody.create(file, MediaType.parse("multipart/form-data")))
                .build();

        //3.创建Request对象，设置URL地址，将RequestBody作为post方法的参数传入
        Request request = new Request.Builder().url(url).post(requestBody).build();
        //4.创建一个call对象,参数就是Request请求对象
        Call call = okHttpClient.newCall(request);
        //5.请求加入调度,重写回调方法
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("Fail");
            }

            @Override
            public void onResponse(Call call, Response response) {
                System.out.println("Success");
            }
        });
        return true;
    }
}