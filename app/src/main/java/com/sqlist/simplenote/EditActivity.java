package com.sqlist.simplenote;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.sqlist.simplenote.dao.NoteDao;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by asus on 2018/1/8.
 */

public class EditActivity extends AppCompatActivity {

    private static final String TAG = "-SimpleNote-";
    private static final int OPEN_ALBUM = 0;
    private static final int OPEN_CAMERA = 1;
    private final String format = "yyyy-MM-dd HH:mm";
    private final SimpleDateFormat sdf = new SimpleDateFormat(format);

    //是否为粗体
    private Boolean boldFlag = false;
    //是否为斜体
    private Boolean italicFlag = false;

    //照相后图片的实际路径
    private String mCurrentPath;

    private ImageView backBtn;
    private TextView timeView;
    private EditText contentEditText;
    private ImageView picImg;
    private ImageView camImg;
    private ImageView alarmImg;
    private ImageView boldImg;
    private ImageView italicImg;

    private Date editTime;

    private int windowWidth;
    private NoteDao noteDao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.edit_activity);

        WindowManager wm = this.getWindowManager();
        int windowWidth = wm.getDefaultDisplay().getWidth();

        noteDao = new NoteDao(this);

        backBtn = (ImageView) findViewById(R.id.back);
        timeView = (TextView) findViewById(R.id.timeView);
        contentEditText = (EditText) findViewById(R.id.content);
        picImg = (ImageView) findViewById(R.id.picImg);
        camImg = (ImageView) findViewById(R.id.camImg);
        alarmImg = findViewById(R.id.alarmImg);
//        boldImg = (ImageView) findViewById(R.id.boldImg);
//        italicImg = (ImageView) findViewById(R.id.italicImg);

        editTime = new Date();

        timeView.setText(sdf.format(editTime));

        alarmImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar currentTime = Calendar.getInstance();
                new TimePickerDialog(EditActivity.this, 0, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                        Intent intent = new Intent(EditActivity.this, AlarmActivity.class);
//                        PendingIntent pi = PendingIntent.getActivity(EditActivity.this, 0, intent, 0);
                        Calendar c = Calendar.getInstance();
                        c.set(Calendar.HOUR, hourOfDay);
                        c.set(Calendar.MINUTE, minute);
                        c.set(Calendar.SECOND, 0);
                        c.set(Calendar.MILLISECOND, 0);
                        Log.d(TAG, "hourOfDay: " + hourOfDay + ", minute: " + minute);

                        String format = "yyyy-MM-dd HH:mm:ss";
                        SimpleDateFormat sdf = new SimpleDateFormat(format);
                        long between = (c.getTimeInMillis() - currentTime.getTimeInMillis());
                        Log.d(TAG, "between: " + between);
                        Log.d(TAG, "c: " + sdf.format(c.getTime()) + ", currentTime: " + sdf.format(currentTime.getTime()));
                        final Timer timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(EditActivity.this, AlarmActivity.class);
                                intent.putExtra("content", contentEditText.getText().toString());
                                startActivity(intent);

                                timer.cancel();
                            }
                        }, between, 10000000);

//                        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
//                        {
//                            alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);
//                        }
//                        else
//                        {
//                            alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);
//                        }
                        Toast.makeText(EditActivity.this, "闹钟设置成功", Toast.LENGTH_LONG).show();

                        Map<String, Object> params = new HashMap<>();
                        params.put("content", contentEditText.getText());
                        params.put("time", sdf.format(editTime));
                        Log.d(TAG, "content: " + contentEditText.getText() + "time: " + editTime.getTime());
                        Boolean re = noteDao.add(params);
                        if (!re)
                        {
                            Toast.makeText(EditActivity.this, "保存失败", Toast.LENGTH_LONG).show();
                        }
                        EditActivity.this.finish();
                    }
                }, currentTime.get(Calendar.HOUR_OF_DAY), currentTime.get(Calendar.MINUTE), false).show();
            }
        });

        Intent intent = getIntent();
        final Bundle bundle = intent.getExtras();
        if (bundle != null)
        {
            timeView.setText(bundle.getString("time"));
//            contentEditText.setText(Html.fromHtml(bundle.getString("content"), new Html.ImageGetter() {
//                @Override
//                public Drawable getDrawable(String source) {
//                    // 获取本地图片
//                    Drawable drawable = Drawable.createFromPath(source);
//                    // 必须设为图片的边际,不然TextView显示不出图片
//                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
//                    // 将其返回
//                    return drawable;
//                }
//            }, null));

            //获得EditText的宽度用于后面的缩放
            int editTextWidth = windowWidth - 20;
            Log.d(TAG, "editTextWidth: " + editTextWidth);
            String imagePath = bundle.getString("content");
            SpannableString ss = new SpannableString(imagePath);
            Pattern p = Pattern.compile("/storage/emulated/0.+?\\.\\w{3}");
            Matcher m = p.matcher(imagePath);
            while(m.find())
            {
                //用path获取Bitmap对象
                Bitmap bitmap = BitmapFactory.decodeFile(m.group());
                Matrix matrix = new Matrix();
                matrix.setScale(1f, 1f);

                //获取图片的长宽
                int imgWidth = bitmap.getWidth();
                int imgHeight = bitmap.getHeight();
                Log.d(TAG, "图片高度为：" + imgHeight + "， 宽度为：" + imgWidth);

                //如果图片的宽度大于EditText的宽度则缩放
                if (imgWidth > editTextWidth)
                {
                    float scale = (float) editTextWidth / imgWidth;
                    Log.d(TAG, "缩放因子：" + scale);
                    matrix.setScale(scale, scale);
                }

                //对原图片进行缩放
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, imgWidth, imgHeight, matrix, true);
                ImageSpan imageSpan = new ImageSpan(this, bitmap);

                ss.setSpan(imageSpan, m.start(), m.end(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            contentEditText.setText(ss);
            contentEditText.setSelection(ss.length());

            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Editable newContent = contentEditText.getText();
                    if (!bundle.getString("content").equals(newContent.toString()))
                    {
                        Map<String, Object> params = new HashMap<>();
                        params.put("id", bundle.getInt("id"));
                        params.put("content",newContent);
                        params.put("time", sdf.format(editTime));

                        Boolean re = noteDao.update(params);
                        if (!re)
                        {
                            Toast.makeText(EditActivity.this, "更新失败", Toast.LENGTH_LONG).show();
                        }
                    }
                    finish();
                }
            });
        }
        else
        {
            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!("".equals(contentEditText.getText()) || contentEditText.getText().length() == 0))
                    {
                        Map<String, Object> params = new HashMap<>();
                        params.put("content", contentEditText.getText());
                        params.put("time", sdf.format(editTime));
                        Log.d(TAG, "content: " + contentEditText.getText() + "time: " + editTime.getTime());
                        Boolean re = noteDao.add(params);
                        if (!re)
                        {
                            Toast.makeText(EditActivity.this, "保存失败", Toast.LENGTH_LONG).show();
                        }
                    }
                    finish();
                }
            });
        }

        //给editText添加监听
//        contentEditText.addTextChangedListener(new EditTextChangedListener());
    }

    public void doClick(View view)
    {
        switch (view.getId())
        {
            case R.id.picImg:
                Log.d(TAG, "打开相册");
                openAlbum();
                break;

            case R.id.camImg:
                Log.d(TAG, "打开相机");
                openCamera();
                break;

//            case R.id.boldImg:
//                changeBold();
//                break;
//
//            case R.id.italicImg:
//                changeItalic();
//                break;
        }
    }

    //打开相册的方法
    private void openAlbum()
    {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, OPEN_ALBUM);
    }

    //打开照相机的方法
    private void openCamera()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        String authority = getApplicationContext().getPackageName() + ".provider";
        Uri fileUri = FileProvider.getUriForFile(this, authority, getOutputMediaFile());

        //将拍下的图片保存到fileUri路径下
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        startActivityForResult(intent, OPEN_CAMERA);
    }

    //是否设置为粗体
    private void changeBold()
    {
        if (boldFlag)
        {
            //设置图片，即XML中的src
            boldImg.setImageDrawable(getDrawable(R.drawable.bold));
            Log.d(TAG, "取消粗体");
            boldFlag = false;
        }
        else
        {
            boldImg.setImageDrawable(getDrawable(R.drawable.bold_click));
            Log.d(TAG, "设置为粗体");
            boldFlag = true;
        }
    }

    //是否设置为斜体
    private void changeItalic()
    {
        if (italicFlag)
        {
            italicImg.setImageDrawable(getDrawable(R.drawable.italic));
            Log.d(TAG, "取消斜体");
            italicFlag = false;
        }
        else
        {
            italicImg.setImageDrawable(getDrawable(R.drawable.italic_click));
            Log.d(TAG, "设置为斜体");
            italicFlag = true;
        }
    }

    private void insertImg(final String path)
    {
        //获得EditText的宽度用于后面的缩放
        int editTextWidth = contentEditText.getWidth();

        //用path获取Bitmap对象
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        Matrix matrix = new Matrix();
        matrix.setScale(1f, 1f);

        //获取图片的长宽
        int imgWidth= bitmap.getWidth();
        int imgHeight= bitmap.getHeight();
        Log.d(TAG, "图片高度为：" + imgHeight + "， 宽度为：" + imgWidth);

        //如果图片的宽度大于EditText的宽度则缩放
        if (imgWidth > editTextWidth)
        {
            float scale = (float) editTextWidth / imgWidth;
            matrix.setScale(scale, scale);
        }

        //对原图片进行缩放
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, imgWidth, imgHeight, matrix, true);
        final ImageSpan imageSpan = new ImageSpan(this, bitmap);

        final String imgPath = "<img src=" + path + "/>";
        //用path初始化SpannableString对象
        SpannableString spannableString = new SpannableString(path);
        spannableString.setSpan(imageSpan, 0, path.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);

        Editable editable = contentEditText.getEditableText();
        int index = contentEditText.getSelectionStart();
        spannableString.getSpans(0, spannableString.length(), ImageSpan.class);

        Log.d(TAG, "插入图片：" + path);
        editable.insert(index, spannableString);

        contentEditText.append("\n");
        Log.d(TAG, "当前editText内容为：" + contentEditText.getText());
        Log.d(TAG, "当前editText内容为：" + Html.escapeHtml(contentEditText.getText()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK)
        {
            Log.d(TAG, "用户操作成功");
            //获得EditText的宽度用于后面的缩放
            int editTextWidth = contentEditText.getWidth();

            if (requestCode == OPEN_ALBUM)
            {
                //处理从相册返回的图片数据
                //使用getData方法获取要调用的接口
                Uri uri = data.getData();
                //第二个参数表示要查询的数据的字段名
                Cursor c = getContentResolver().query(uri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
                if (c != null)
                {
                    c.moveToFirst();
                    //通过游标来获取名为MediaStore.Images.Media.DATA字段的值
                    String path = c.getString(c.getColumnIndex(MediaStore.Images.Media.DATA));

                    insertImg(path);
                }
            }
            else if (requestCode == OPEN_CAMERA)
            {
                insertImg(mCurrentPath);
            }
        }
    }

    //创建相机拍下图片的目录
    private File getOutputMediaFile()
    {
        File mediaStorageDir = null;
        try
        {
            String path = Environment.getExternalStorageDirectory() + "/simpleNote";
            mediaStorageDir = new File(path);

            if (!mediaStorageDir.exists())
            {
                mediaStorageDir.mkdirs();
            }

            Log.d(TAG, "成功创建目录: "
                    + mediaStorageDir);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.d(TAG, "无法创建目录: "
                    + mediaStorageDir);
        }

        if (!mediaStorageDir.exists())
        {
            if (!mediaStorageDir.mkdirs())
            {
                // 在SD卡上创建文件夹需要权限
                Log.d(TAG, "创建目录失败，请确认是否有对SD卡创建目录的权限");
                return null;
            }
        }

        File mediaFile;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");

        //获取实际路径
        mCurrentPath = mediaFile.getAbsolutePath();
        Log.d(TAG, "要保存的图片名为：" + mediaFile.getAbsolutePath());
        return mediaFile;
    }

    class EditTextChangedListener implements TextWatcher
    {
        //定义当前输入的字符数
        private int charCount = 0;

        //s:变化前的所有字符； start:字符开始的位置； count:变化前的总字节数；after:变化后的字节数
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        //s：变化后的所有字符；start：字符起始的位置；before: 变化之前的总字节数；count:变化后的字节
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Log.d(TAG, "char: " + s + ", start: " + start + ", before: " + before + ", count: " + count);
            //判断当前输入的字符数，与文本框内的字符数长度是否一样，如果一样，则不进行操作
            //主要用来跳出循环，当改变文字时，onTextChanged就认为有所变化，会进入死循环，所以采用这种方式结束循环
            if(charCount != contentEditText.length())
            {
                //将当前字符串的长度给输入字符串变量
                charCount = contentEditText.length() + "<b><i></b></i>".length();
                Log.d(TAG, "字符长度：" + charCount);
                //定义SpannableString，它主要的用途就是可以改变editText,TextView中部分文字的格式，以及向其中插入图片等功能
                SpannableString ss;
//                String tmp;
                if(boldFlag && italicFlag)
                {
//                    tmp = s.subSequence(0, start) + "<b><i>" + s.subSequence(start, start + count) + "</i></b>";
                    ss = new SpannableString(s.subSequence(0, start) + "<b><i>" + s.subSequence(start, s.length()) + "</i></b>");
                    ss.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), start + "<b><i>".length(), ss.length() - "</b></i>".length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
//                else if (boldFlag)
//                {
//                    tmp = s.subSequence(0, start) + "<b>" + s.subSequence(start, start + count) + "</b>";
//                }
//                else if (italicFlag)
//                {
//                    tmp = s.subSequence(0, start) + "<i>" + s.subSequence(start, start + count) + "</i>";
//                }
                else
                {
//                    tmp = s.toString();
                    ss = new SpannableString(s);
                    ss.setSpan(new StyleSpan(Typeface.NORMAL), start, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }

                contentEditText.setText(ss);
//                contentEditText.setText(Html.fromHtml(tmp));
                Log.d(TAG, "当前editText内容为：" + ss);
//                Log.d(TAG, "当前editText内容为：" + Html.fromHtml(tmp));
            }
        }

        //s:变化后的所有字符
        @Override
        public void afterTextChanged(Editable s) {
            //将光标点，移动到最后一个字
            contentEditText.setSelection(s.length());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        noteDao.close();
    }
}


