package com.example.newstoday.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.newstoday.Adapter.PublishImageAdapter;
import com.example.newstoday.ImagePoster;
import com.example.newstoday.News;
import com.example.newstoday.R;
import com.example.newstoday.UserMessageManager;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.zhihu.matisse.Matisse;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import dmax.dialog.SpotsDialog;
import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static com.example.newstoday.Activity.NewsItem.PUBLISH_CHOOSE_IMAGE;

public class Publish extends AppCompatActivity {
    private RecyclerView recyclerView;
    private PublishImageAdapter publishImageAdapter;
    private ArrayList<Uri> mSelected = new ArrayList<>();
    private UserMessageManager userMessageManager;
    private AlertDialog spotsDialog;
    private ImagePoster imagePoster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);

        final EditText content = findViewById(R.id.publish_text);
        final EditText title = findViewById(R.id.publish_title);
        title.requestFocus();

        recyclerView = findViewById(R.id.publish_image_recycler);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        publishImageAdapter = new PublishImageAdapter(mSelected, this);
        recyclerView.setAdapter(publishImageAdapter);

        spotsDialog = new SpotsDialog.Builder()
                .setContext(Publish.this)
                .setCancelable(false)
                .setTheme(R.style.Compressing)
                .build();

        userMessageManager = UserMessageManager.getUserMessageManager(getApplicationContext());
        imagePoster = ImagePoster.getImagePoster(getApplicationContext());
        final ImageButton imageButton = findViewById(R.id.publish_check_btn);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String today = new SimpleDateFormat("yyyy-MM-dd\tHH:mm:ss", Locale.CHINA).format(new Date());
                String email = Table.header.getActiveProfile().getEmail().toString();
                boolean isAdd = publishImageAdapter.isAdd();
                String[] pics = new String[isAdd?mSelected.size()-1:mSelected.size()];
                int n = 0;
                for(Uri uri : mSelected){
                    if(n == pics.length)
                        break;
                    pics[n] = uri.toString();
                    ++n;
                }
                String[] picsNew = imagePoster.postUserImageToServer(pics);
                userMessageManager.addOneUserMessage(
                        new News(title.getText().toString(), today, content.getText().toString(), "关注", "",
                        email + System.currentTimeMillis(), picsNew, email, "",
                                "", new String[0], new String[0], "", "", null, null));
                Toast.makeText(getApplicationContext(), "发布成功", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PUBLISH_CHOOSE_IMAGE && resultCode == RESULT_OK){
            mSelected = new ArrayList<>(Matisse.obtainResult(data));
            final ArrayList<Uri> newSelected = new ArrayList<>();
            Luban.with(getApplicationContext())
                    .load(mSelected)
                    .ignoreBy(0)
                    .setTargetDir(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath())
                    .filter(new CompressionPredicate() {
                        @Override
                        public boolean apply(String path) {
                            return !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"));
                        }
                    })
                    .setCompressListener(new OnCompressListener() {
                        @Override
                        public void onStart() {
                            if(!spotsDialog.isShowing())
                                spotsDialog.show();
                        }

                        @Override
                        public void onSuccess(File file) {
                            newSelected.add(Uri.parse(file.toURI().toString()));
                            if(newSelected.size() == mSelected.size()){
                                spotsDialog.dismiss();
                                mSelected = newSelected;
                                publishImageAdapter.updateImages(mSelected);
                                publishImageAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(getApplicationContext(), "图片压缩失败", Toast.LENGTH_SHORT).show();
                        }
                    }).launch();
//            mSelected = PostImage.compress(mSelected, getApplicationContext(), spotsDialog);
////            publishImageAdapter.updateImages(mSelected);
////            publishImageAdapter.notifyDataSetChanged();
        }
    }
}
