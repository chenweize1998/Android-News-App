package com.example.newstoday.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.newstoday.Adapter.PublishImageAdapter;
import com.example.newstoday.R;
import com.example.newstoday.UserMessage;
import com.example.newstoday.UserMessageManager;
import com.zhihu.matisse.Matisse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.newstoday.Activity.NewsItem.PUBLISH;
import static java.security.AccessController.getContext;
import static com.example.newstoday.Activity.NewsItem.PUBLISH_CHOOSE_IMAGE;

public class Publish extends AppCompatActivity {
    private RecyclerView recyclerView;
    private PublishImageAdapter publishImageAdapter;
    private ArrayList<Uri> mSelected = new ArrayList<>();
    private UserMessageManager userMessageManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);

        final EditText editText = findViewById(R.id.publish_text);
        editText.requestFocus();

        recyclerView = findViewById(R.id.publish_image_recycler);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        publishImageAdapter = new PublishImageAdapter(mSelected, this);
        recyclerView.setAdapter(publishImageAdapter);

        userMessageManager = UserMessageManager.getUserMessageManager(getApplicationContext());
        ImageButton imageButton = findViewById(R.id.publish_check_btn);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    userMessageManager.addOneUserMessage(
                            new UserMessage(null,
                            Table.header.getActiveProfile().getEmail().toString(),
                            editText.getText().toString(),
                            mSelected.size() > 0?MediaStore.Images.Media.getBitmap(getContentResolver(), mSelected.get(0)) : null
                ));
                } catch (IOException e){
                    e.printStackTrace();
                    return;
                }
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PUBLISH_CHOOSE_IMAGE && resultCode == RESULT_OK){
            mSelected = new ArrayList<>(Matisse.obtainResult(data));
            publishImageAdapter.updateImages(mSelected);
            publishImageAdapter.notifyDataSetChanged();
        }
    }
}
