package com.sqlist.simplenote;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * Created by asus on 2018/1/10.
 */

public class RGBHEXActivity extends AppCompatActivity {

    private ImageView backBtn;
    private EditText rText;
    private EditText gText;
    private EditText bText;
    private EditText hexText;
    private Button rgb2hexBtn;
    private Button hex2rgbBtn;
    private ImageView colorImg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.rgb_hex_activity);

        backBtn = findViewById(R.id.back);
        rText = findViewById(R.id.R);
        gText = findViewById(R.id.G);
        bText = findViewById(R.id.B);
        hexText = findViewById(R.id.hex);
        rgb2hexBtn = findViewById(R.id.rgb2hex);
        hex2rgbBtn = findViewById(R.id.hex2rgb);
        colorImg = findViewById(R.id.color);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rgb2hexBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer r = Integer.valueOf(rText.getText().toString());
                Integer g = Integer.valueOf(gText.getText().toString());
                Integer b = Integer.valueOf(bText.getText().toString());

                int color = Color.rgb(r, g, b);
                colorImg.setBackgroundColor(color);

                String hex = Integer.toHexString(r) + Integer.toHexString(g) + Integer.toHexString(b);
                hexText.setText(hex);
            }
        });

        hex2rgbBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hex = hexText.getText().toString();
                Integer r = Integer.parseInt(hex.length() == 3 ? hex.substring(0, 1) + hex.substring(0, 1) : hex.substring(0, 2), 16);
                Integer g = Integer.parseInt(hex.length() == 3 ? hex.substring(1, 2) + hex.substring(1, 2) : hex.substring(2, 4), 16);
                Integer b = Integer.parseInt(hex.length() == 3 ? hex.substring(2, 3) + hex.substring(2, 3) : hex.substring(4, 6), 16);

                int color = Color.rgb(r, g, b);
                colorImg.setBackgroundColor(color);

                rText.setText(r + "");
                gText.setText(g + "");
                bText.setText(b + "");
            }
        });
    }
}
