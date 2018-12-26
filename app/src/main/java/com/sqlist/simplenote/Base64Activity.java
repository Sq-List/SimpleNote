package com.sqlist.simplenote;

import android.content.ClipboardManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.apaches.commons.codec.binary.Base64;

/**
 * Created by asus on 2018/1/10.
 */

public class Base64Activity extends AppCompatActivity {

    private ImageView backBtn;
    private Button encryptBtn;
    private Button decryptBtn;
    private Button copyBtn;
    private EditText content;
    private EditText result;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.base64_activity);

        backBtn = findViewById(R.id.back);
        encryptBtn = findViewById(R.id.encrypt);
        decryptBtn = findViewById(R.id.decrypt);
        copyBtn = findViewById(R.id.copy);
        content = findViewById(R.id.content);
        result = findViewById(R.id.result);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        encryptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String source = content.getText().toString();
                String target = Base64.encodeBase64String(source.getBytes());
                result.setText(target);
            }
        });

        decryptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String source = content.getText().toString();
                String target = new String(Base64.decodeBase64(source));
                result.setText(target);
            }
        });

        copyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                cm.setText(result.getText());
                Toast.makeText(Base64Activity.this, "复制成功", Toast.LENGTH_LONG).show();
            }
        });
    }
}
