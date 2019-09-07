package com.example.myapplication.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.Service.SQLservice;
import com.example.myapplication.Utilities.User;

import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;

public class KeysWordsFilterActivity extends AppCompatActivity {
    TagContainerLayout mTagContainerLayout;
    private ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keys_words_filter);
        User user = (User)getApplication();
        mTagContainerLayout = (TagContainerLayout) findViewById(R.id.tagcontainerLayout);
        mTagContainerLayout.setTags(user.getKeyswords());
        //mTagContainerLayout.setDefaultImageDrawableID(R.drawable.yellow_avatar);
        mTagContainerLayout.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {
//                Toast.makeText(KeysWordsFilterActivity.this, "click-position:" + position + ", text:" + text,
//                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTagLongClick(final int position, final String text) {
                AlertDialog dialog = new AlertDialog.Builder(KeysWordsFilterActivity.this)
                        .setTitle("long click")
                        .setMessage("You will delete this key word")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (position < mTagContainerLayout.getChildCount()) {
                                    User user = (User)getApplication();
                                    String tag = (String) mTagContainerLayout.getTag(position);
                                    user.deleteKeywords(tag);
                                    mTagContainerLayout.removeTag(position);
                                    Intent intent = new Intent(KeysWordsFilterActivity.this,SQLservice.class);
                                    intent.putExtra("flag",User.DELETE_FLITER);
                                    intent.putExtra("data",tag);
                                }

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create();
                dialog.show();
            }

            @Override
            public void onSelectedTagDrag(int position, String text) {}

            @Override
            public void onTagCrossClick(int position) {
//                mTagContainerLayout1.removeTag(position);
//                Toast.makeText(KeysWordsFilterActivity.this, "Click TagView cross! position = " + position,
//                        Toast.LENGTH_SHORT).show();
                if (position < mTagContainerLayout.getChildCount()) {
                    User user = (User)getApplication();
                    String tag = (String) mTagContainerLayout.getTag(position);
                    user.deleteKeywords(tag);
                    mTagContainerLayout.removeTag(position);
                    Intent intent = new Intent(KeysWordsFilterActivity.this,SQLservice.class);
                    intent.putExtra("flag",User.DELETE_FLITER);
                    intent.putExtra("data",tag);
                }
            }
        });
        final EditText text = (EditText) findViewById(R.id.text_tag);
        Button btnAddTag = (Button) findViewById(R.id.btn_add_tag);
        btnAddTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tag = text.getText().toString();
                mTagContainerLayout.addTag(tag);
                User user = (User)getApplication();
                user.addKeywords(tag);
                Intent intent = new Intent(KeysWordsFilterActivity.this,SQLservice.class);
                intent.putExtra("flag",User.ADD_FLITER);
                intent.putExtra("data",tag);
                // Add tag in the specified position
//                mTagContainerLayout1.addTag(text.getText().toString(), 4);
            }
        });
        back = findViewById(R.id.keys_tag_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        setResult(3);
        finish();
    }
}

