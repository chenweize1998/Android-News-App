package com.example.newstoday;

import android.os.Environment;

import okhttp3.*;

import java.io.File;
import java.io.IOException;

public class PostImage {

    /**
     * 这个函数用来上传图片，你只需要传递进来文件的本地路径即可
     * 之后你就可以通过picasso直接从服务器下载图片
     * */
    public boolean postImage(String imagePath){
        //1.创建OkHttpClient对象
        OkHttpClient  okHttpClient = new OkHttpClient();
        //上传的图片
        String url = "";
        File file = new File(imagePath);
        //2.通过RequestBody.create 创建requestBody对象,application/octet-stream 表示文件是任意二进制数据流
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("filename", imagePath)
                .addFormDataPart("image", imagePath.substring(imagePath.lastIndexOf("/") + 1), RequestBody.create(file, MediaType.parse("application/octet-stream")))
                .build();

        //3.创建Request对象，设置URL地址，将RequestBody作为post方法的参数传入
        Request request = new Request.Builder().url("").post(requestBody).build();
        //4.创建一个call对象,参数就是Request请求对象
        Call call = okHttpClient.newCall(request);
        //5.请求加入调度,重写回调方法
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) {
            }
        });
        return true;
    }
}