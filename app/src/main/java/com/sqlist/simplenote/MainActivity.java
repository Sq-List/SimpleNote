package com.sqlist.simplenote;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.sqlist.simplenote.dao.NoteDao;
import com.sqlist.simplenote.fragment.FirstFragment;
import com.sqlist.simplenote.fragment.FragAdapter;
import com.sqlist.simplenote.fragment.SecondFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity
        implements FirstFragment.Callbacks, SecondFragment.Callbacks
{

    private static final String TAG = "-SimpleNote-";
    private FloatingActionButton floatingActionButton;
    private DrawerLayout drawerLayout;
    private ImageView category;
    private ListView listView;
    private NoteDao noteDao;
    List<Map<String, Object>> params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //检查是否读写SD卡的权限
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        {
            //如果没有，则提示用户
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "MainActivity: onCreate");

        floatingActionButton = findViewById(R.id.addBtn);
        drawerLayout = findViewById(R.id.drawer_layout);
        category = findViewById(R.id.category);
        listView = findViewById(R.id.lv_left_menu);
        noteDao = new NoteDao(this);

        init();

        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.START);
            }
        });

        String[] contentArr = new String[]{"MD5加密", "Base64加密", "自动阅读", "RGB/HEX转换"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.left_menu_list_view, contentArr);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position)
                {
                    case 0:
                        Log.d(TAG, "点击MD5加密按钮");
                        startActivity(new Intent(MainActivity.this, MD5Activity.class));
                        break;

                    case 1:
                        Log.d(TAG, "点击Base64加密解密按钮");
                        startActivity(new Intent(MainActivity.this, Base64Activity.class));
                        break;

                    case 2:
                        Log.d(TAG, "点击自动阅读按钮");
                        startActivity(new Intent(MainActivity.this, ReadActivity.class));
                        break;

                    case 3:
                        Log.d(TAG, "点击RGB/HEX转换按钮");
                        startActivity(new Intent(MainActivity.this, RGBHEXActivity.class));
                        break;
                }
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                startActivity(intent);
            }
        });
    }

    private void init()
    {
        //使用适配器将ViewPager与Fragment绑定在一起
        FragAdapter fragAdapter = new FragAdapter(getSupportFragmentManager());
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(fragAdapter);

        //将TabLayout与ViewPager绑定在一起
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "MainActivity OnRestart");
        init();
    }

    public List<Map<String, Object>> getData()
    {
        Log.d(TAG, "MainActivity getData");
        params = noteDao.getAll();
        return params;
    }

    @Override
    public void onItemSelected(int position) {
        Intent intent = new Intent(MainActivity.this, EditActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("id", (Integer) params.get(position).get("id"));
        bundle.putString("time", (String) params.get(position).get("time"));
        bundle.putString("content", (String) params.get(position).get("content"));
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onItemLongSelect(final int position) {
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(MainActivity.this);
        normalDialog.setTitle("确定删除？");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Boolean re = noteDao.delete((Integer) params.get(position).get("id"));
                        if (re)
                        {
                            Toast.makeText(MainActivity.this, "删除成功", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this, "删除失败", Toast.LENGTH_LONG).show();
                        }
                        init();
                    }
                });
        normalDialog.setNegativeButton("关闭",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        // 显示
        normalDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        noteDao.close();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.md5:
                Log.d(TAG, "点击MD5加密按钮");
                startActivity(new Intent(MainActivity.this, MD5Activity.class));
                break;

            case R.id.base64:
                Log.d(TAG, "点击Base64加密解密按钮");
                startActivity(new Intent(MainActivity.this, Base64Activity.class));
                break;

            case R.id.read:
                Log.d(TAG, "点击自动阅读按钮");
                startActivity(new Intent(MainActivity.this, ReadActivity.class));
                break;

            case R.id.color:
                Log.d(TAG, "点击RGB/HEX转换按钮");
                startActivity(new Intent(MainActivity.this, RGBHEXActivity.class));
                break;
        }
    }
}
