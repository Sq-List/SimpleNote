package com.sqlist.simplenote;

import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by asus on 2018/1/10.
 */

public class AlarmActivity extends AppCompatActivity {

    MediaPlayer alarmMusic;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        alarmMusic = MediaPlayer.create(this, R.raw.alarm);
        alarmMusic.setLooping(true);
        alarmMusic.start();

        new AlertDialog.Builder(AlarmActivity.this).setMessage(getIntent().getStringExtra("content"))
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alarmMusic.stop();
                        AlarmActivity.this.finish();
                    }
                }).show();

    }
}
