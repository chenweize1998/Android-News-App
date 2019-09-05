package com.example.newstoday;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.Toast;

import okhttp3.*;
import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class PostImage {

    /**
     * 这个函数用来上传图片，你只需要传递进来文件的本地路径即可
     * 之后你就可以通过picasso直接从服务器下载图片
     * */
    public static boolean postImage(String imagePath){
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

//    public static ArrayList<Uri> compress(final ArrayList<Uri> uris, final Context context, final AlertDialog spotsDialog){
//        final ArrayList<Uri> newSelected = new ArrayList<>();
//        Luban.with(context)
//                .load(uris)
//                .ignoreBy(0)
//                .setTargetDir(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath())
//                .filter(new CompressionPredicate() {
//                    @Override
//                    public boolean apply(String path) {
//                        return !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"));
//                    }
//                })
//                .setCompressListener(new OnCompressListener() {
//                    @Override
//                    public void onStart() {
//                        // TODO 压缩开始前调用，可以在方法内启动 loading UI
//                        if(!spotsDialog.isShowing())
//                            spotsDialog.show();
//                    }
//
//                    @Override
//                    public void onSuccess(File file) {
//                        // TODO 压缩成功后调用，返回压缩后的图片文件
//                        newSelected.add(Uri.parse(file.toURI().toString()));
//                        if(newSelected.size() == uris.size()){
//                            spotsDialog.dismiss();
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        // TODO 当压缩过程出现问题时调用
//                        Toast.makeText(context, "图片压缩失败", Toast.LENGTH_SHORT).show();
//                    }
//                }).launch();
//        return newSelected;
//    }

    public static String getRealPathFromURI(Uri contentURI, ContentResolver resolver) {
        String result;
        Cursor cursor = resolver.query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }
}