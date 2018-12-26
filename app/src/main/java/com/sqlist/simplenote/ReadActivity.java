package com.sqlist.simplenote;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Locale;

/**
 * Created by asus on 2018/1/10.
 */

public class ReadActivity extends AppCompatActivity {

    private TextToSpeech tts;
    private ImageView backBtn;
    private EditText contentText;
    private Button readBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.read_activity);

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS)
                {
                    int re = tts.setLanguage(Locale.US);

                    if (re != TextToSpeech.LANG_COUNTRY_AVAILABLE && re != TextToSpeech.LANG_AVAILABLE)
                    {
                        Toast.makeText(ReadActivity.this,  "暂不支持这种语言的朗读", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        backBtn = findViewById(R.id.back);
        contentText = findViewById(R.id.content);
        readBtn = findViewById(R.id.read);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        readBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = contentText.getText().toString();
                tts.speak(content, TextToSpeech.QUEUE_ADD, null, "speech");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tts != null)
        {
            tts.shutdown();
        }
    }
}
