package com.example.project_team7;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Time;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by 주현 on 2017-11-19.
 */

public class Vote_new extends AppCompatActivity {

    int i = 0;//minflater로 늘린 레이아웃 갯수

    EditText etTitle;
    EditText etTimelimit;
    TextView tvLimitText;
    TextView tvModifySet;
    Button btAdd;
    Button btVoteenroll;
    EditText et_Tag;
    ImageButton ib_image;
    Uri mImageCaptureUri;
    String absolutePath;
    RadioGroup rg_category;
    RadioButton rb_food;
    RadioButton rb_beauty;
    RadioButton rb_culture;
    RadioButton rb_etc;
    String category=null;
    String TITLE = null;
    String login_nick = null;
    String NICKNAME = null;
    String STARTTIME = null;
    String TIMELIMIT = null;
    int NUMBER = 0;
    String CASE = null;
    String CATEGORY = null;
    Bitmap bitmap = null;
    ImageButton[] ib_image_array;
    int cand_cnt=0;
    String tag = null;
    String image = null;
    String Url = "http://towsung.cafe24.com/uploads/";
    LayoutInflater mInflater;
    LinearLayout mRoot1Linear;
    int thread_i = 0;


    public static final String UPLOAD_URL = "http://simplifiedcoding.16mb.com/ImageUpload/upload.php";
    public static final String UPLOAD_KEY = "image";
    public static final String TAG = "MY MESSAGE";
    private int PICK_IMAGE_REQUEST = 1;
    private Uri filePath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_votenew);

        Intent intent = getIntent();
        CASE = intent.getStringExtra("case");
        if(CASE.equals("modify")){
            NUMBER = Integer.parseInt(intent.getStringExtra("NUMBER"));
        }

        etTitle = (EditText)findViewById(R.id.et_title);
        etTimelimit = (EditText)findViewById(R.id.et_timelimit);
        tvLimitText = (TextView)findViewById(R.id.tv_limittext);
        tvModifySet = (TextView)findViewById(R.id.modifySet);
        btAdd = (Button)findViewById(R.id.bt_add);
        btVoteenroll = (Button)findViewById(R.id.bt_voteenroll);
        rg_category = (RadioGroup)findViewById(R.id.rg_category);
        rb_food = (RadioButton)findViewById(R.id.rb_food);
        rb_beauty = (RadioButton)findViewById(R.id.rb_beauty);
        rb_culture = (RadioButton)findViewById(R.id.rb_culture);
        rb_etc = (RadioButton)findViewById(R.id.rb_etc);

        mInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        mRoot1Linear = (LinearLayout) findViewById(R.id.linear_root1);
        // LinearLayout mRoot2Linear = (LinearLayout) findViewById(R.id.linear_root2);


        if(CASE.equals("new")){
            getSupportActionBar().setTitle("투표 게시글 작성");//액션바 타이틀 변경
            View v1 = mInflater.inflate(R.layout.inflate, mRoot1Linear, true);i++;
            ib_image = (ImageButton)v1.findViewById(R.id.ib_item1);
            et_Tag = (EditText)v1.findViewById(R.id.et_item1);

            ib_image.setOnClickListener(ocn2);
        }else{//"modify
            getSupportActionBar().setTitle("투표 게시글 수정");//액션바 타이틀 변경

            voteGetFromNumRequest getFromNumrequest = new voteGetFromNumRequest(NUMBER, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        TITLE = jsonResponse.getString("TITLE");
                        etTitle.setText(TITLE);
                        NICKNAME = jsonResponse.getString("NICKNAME");
                        STARTTIME = jsonResponse.getString("STARTTIME");
                        tvModifySet.setText("작성시간: "+STARTTIME);
                        TIMELIMIT = jsonResponse.getString("TIMELIMIT");
                        etTimelimit.setVisibility(View.GONE);
                        tvLimitText.setText(TIMELIMIT);
                        CATEGORY = jsonResponse.getString("CATEGORY");
                        if(CATEGORY.equals("food")){
                            rb_food.setChecked(true);
                        }else if(CATEGORY.equals("beauty")){
                            rb_beauty.setChecked(true);
                        }else if(CATEGORY.equals("culture")){
                            rb_culture.setChecked(true);
                        }else{//"etc"
                            rb_etc.setChecked(true);
                        }//분류 set
                        btVoteenroll.setText("게시물 수정");

                        imageDownRequest imagdown = new imageDownRequest(NUMBER,  new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONArray candidate = new JSONArray(response);
                                    cand_cnt = candidate.length();
                                    ib_image_array = new ImageButton[cand_cnt];
                                    for(int c=0;c<candidate.length();c++)
                                        mInflater.inflate(R.layout.inflate, mRoot1Linear, true);
                                    for (int h=0;h<candidate.length();h++){
                                        JSONObject candidate_data = candidate.getJSONObject(h);
                                        tag = candidate_data.getString("tags");
                                        et_Tag = (EditText) mRoot1Linear.getChildAt(h).findViewById(R.id.et_item1);
                                        et_Tag.setText(tag);
                                        image = candidate_data.getString("image");
                                        ib_image_array[h] = (ImageButton)mRoot1Linear.getChildAt(h).findViewById(R.id.ib_item1);
                                        if (image.equals("null")){
                                            Picasso.with(Vote_new.this)
                                                    .load(Url+"ic_crop_original_black_48dp.png".toString())
                                                    .into(ib_image_array[h]);
                                        }
                                        else {
                                            Picasso.with(Vote_new.this)
                                                    .load(Url+image.toString())
                                                    .into(ib_image_array[h]);
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        RequestQueue queue3 = Volley.newRequestQueue(Vote_new.this);
                        queue3.add(imagdown);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
            RequestQueue queue = Volley.newRequestQueue(Vote_new.this);
            queue.add(getFromNumrequest);

            //query 글넘버 같은 거 몇개인지 세고 inflate하기

            btAdd.setVisibility(View.INVISIBLE);//후보 추가 불가능


          //  int items=3;//후보 갯수가 3개라고 가정 //쿼리문에 의해 후보 갯수 불러오기
           // int i=0;//보여진 갯수
          //  while (i<items){
              // if(i%2==0){
         /*           View v1 = mInflater.inflate(R.layout.inflate2, mRoot1Linear, true);i++;
                    View lastChild = mRoot1Linear.getChildAt(mRoot1Linear.getChildCount()-1);
                    ib_image = (ImageButton)lastChild.findViewById(R.id.ib_item1);
                    et_Tag = (EditText)lastChild.findViewById(R.id.et_item1);

                    //ib_image.setImageBitmap();
              /*  }
                else if(i%2==1){
                    View v2 = mInflater.inflate(R.layout.inflate2, mRoot2Linear, true);i++;
                    View lastChild = mRoot2Linear.getChildAt(mRoot2Linear.getChildCount()-1);
                    ib_image = (ImageButton)lastChild.findViewById(R.id.ib_item1);
                    et_Tag = (EditText)lastChild.findViewById(R.id.et_item1);
                    //ib_image.setImageBitmap();
                } */
           // }
            //이미지랑 텍스트 불러오기

        }

        btAdd.setOnClickListener(ocn1);
        btVoteenroll.setOnClickListener(ocn1);
        rg_category.setOnCheckedChangeListener(occl);

    }
    RadioGroup.OnCheckedChangeListener occl = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            switch (i){
                case R.id.rb_food:
                    category = "food";
                    break;
                case R.id.rb_beauty:
                    category = "beauty";
                    break;
                case R.id.rb_culture:
                    category = "culture";
                    break;
                case R.id.rb_etc:
                    category = "etc";
                    break;
            }
        }
    };

    Button.OnClickListener ocn1 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Bitmap tmpBitmap=null;
            Bitmap tmpBitmap1=null;
            if(CASE.equals("new")){
                Drawable temp = ib_image.getDrawable();
                Drawable temp1 = getResources().getDrawable(R.drawable.ic_crop_original_black_48dp);
                if(temp != null) {
                    tmpBitmap = ((BitmapDrawable) temp).getBitmap();
                    tmpBitmap1 = ((BitmapDrawable) temp1).getBitmap();
                }
            }

            String tags = et_Tag.getText().toString();



            switch (view.getId()) {
                case R.id.bt_add://누를 때마다 후보 내용 한개씩 update해주기
                    if(tmpBitmap.equals(tmpBitmap1))
                    {
                       Toast.makeText(Vote_new.this,"실행",Toast.LENGTH_LONG).show();
                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try
                                {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    boolean success = jsonResponse.getBoolean("success");
                                    if(success){
                                        AlertDialog.Builder builder = new AlertDialog.Builder(Vote_new.this);
                                        builder.setMessage("Success!!!");
                                    }
                                    else
                                    {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(Vote_new.this);
                                        builder.setMessage("Fail!!!");
                                    }
                                }
                                catch (JSONException e)
                                {
                                    e.printStackTrace();
                                }
                            }
                        };
                        UploadNoimage uploadNoimage = new UploadNoimage(NUMBER,tags,i,responseListener);
                        RequestQueue queue = Volley.newRequestQueue(Vote_new.this);
                        queue.add(uploadNoimage);
                    }


                    LayoutInflater mInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                    LinearLayout mRoot1Linear = (LinearLayout) findViewById(R.id.linear_root1);
                    // LinearLayout mRoot2Linear = (LinearLayout) findViewById(R.id.linear_root2);
                    // if (i % 2 == 0) {
                        View v1 = mInflater.inflate(R.layout.inflate, mRoot1Linear, true);
                        i++;
                        View lastChild = mRoot1Linear.getChildAt(mRoot1Linear.getChildCount() - 1);
                        ib_image = (ImageButton) lastChild.findViewById(R.id.ib_item1);
                        et_Tag = (EditText)lastChild.findViewById(R.id.et_item1);
                        ib_image.setOnClickListener(ocn2);
                    /* } else {
                        View v2 = mInflater.inflate(R.layout.inflate, mRoot2Linear, true);
                        i++;
                        View lastChild = mRoot2Linear.getChildAt(mRoot2Linear.getChildCount() - 1);
                        ib_image = (ImageButton) lastChild.findViewById(R.id.ib_item1);
                        ib_image.setOnClickListener(ocn2);
                    } */
                    break;
                case R.id.bt_voteenroll://new랑 modify랑 나누기
                    //modify intent로 넘겨받은 number로 update 실행


                    CATEGORY = category;
                    TITLE = etTitle.getText().toString();
                    login_nick = "textnick";
                    NICKNAME = login_nick;

                    if(CASE.equals("new")){
                        if(tmpBitmap.equals(tmpBitmap1))
                        {
                            Response.Listener<String> responseListener = new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try
                                    {
                                        JSONObject jsonResponse = new JSONObject(response);
                                        boolean success = jsonResponse.getBoolean("success");
                                        if(success){
                                            AlertDialog.Builder builder = new AlertDialog.Builder(Vote_new.this);
                                            builder.setMessage("Success!!!");
                                        }
                                        else
                                        {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(Vote_new.this);
                                            builder.setMessage("Fail!!!");
                                        }
                                    }
                                    catch (JSONException e)
                                    {
                                        e.printStackTrace();
                                    }
                                }
                            };
                            UploadNoimage uploadNoimage = new UploadNoimage(NUMBER,tags,i,responseListener);
                            RequestQueue queue = Volley.newRequestQueue(Vote_new.this);
                            queue.add(uploadNoimage);
                        }

                        DateFormat df = DateFormat.getDateTimeInstance();
                        String STARTTIME = df.format(System.currentTimeMillis());
                        TIMELIMIT = addHour(Integer.parseInt(etTimelimit.getText().toString()));

                        if (category == null || TITLE.length() == 0 || TIMELIMIT.length() == 0) {
                            Toast.makeText(Vote_new.this, "채워지지 않은 칸이 있습니다", Toast.LENGTH_SHORT).show();
                        } else {
                            Response.Listener<String> resL = new Response.Listener<String>() {

                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonR = new JSONObject(response);
                                        boolean success = jsonR.getBoolean("success");

                                        if (success) {

                                            Intent intent = new Intent(Vote_new.this, Vote_Content.class);
                                            intent.putExtra("login_nick", login_nick);
                                            intent.putExtra("NICKNAME", NICKNAME);
                                            intent.putExtra("TIMELIMIT", TIMELIMIT);
                                            intent.putExtra("numOfItems", i);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(Vote_new.this);
                                            builder.setMessage("Fail")
                                                    .setNegativeButton("다시 시도", null)
                                                    .create()
                                                    .show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            };
                            voteAddRequest addRequest = new voteAddRequest(CATEGORY, TITLE, login_nick, STARTTIME, TIMELIMIT, resL);
                            RequestQueue queue = Volley.newRequestQueue(Vote_new.this);
                            queue.add(addRequest);

                        }
                    }else{//modify
                        if (category == null || TITLE.length() == 0 ) {
                            Toast.makeText(Vote_new.this, "채워지지 않은 칸이 있습니다", Toast.LENGTH_SHORT).show();
                        }else{

                            BackThread thread = new BackThread();
                            thread.setDaemon(true);
                            thread.start();

                            voteModifyRequest modifyRequest = new voteModifyRequest(TITLE, CATEGORY, NUMBER, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try{
                                        Toast.makeText(Vote_new.this, "게시글이 수정되었습니다", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(Vote_new.this, Vote_Content.class);
                                        intent.putExtra("login_nick", login_nick);
                                        intent.putExtra("NICKNAME", NICKNAME);
                                        intent.putExtra("TIMELIMIT", TIMELIMIT);
                                        intent.putExtra("numOfItems", i);
                                        startActivity(intent);
                                        finish();
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }
                            });
                            RequestQueue queue = Volley.newRequestQueue(Vote_new.this);
                            queue.add(modifyRequest);
                        }
                    }
                    break;
            }
        }
        public String addHour(int hour){//StartTime + 사용자가 입력한 숫자 = TimeLimit
            String timelimit;

            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date(System.currentTimeMillis()));
            cal.add(Calendar.HOUR, hour);
            timelimit = DateFormat.getDateTimeInstance().format(cal.getTime());
            return timelimit;
        }
    };
    class BackThread extends Thread{
        @Override
        public void run() {
            super.run();
            for(thread_i=0;thread_i<cand_cnt;thread_i++){
                handler.sendEmptyMessage(0);
                try{
                    Thread.sleep(500);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }

        }
    }
    Handler handler = new Handler(){//게시글 modify에서 tags 넘겨주기
        @Override
        public void handleMessage(Message msg){
            if(msg.what == 0){
                et_Tag = (EditText) mRoot1Linear.getChildAt(thread_i).findViewById(R.id.et_item1);
                tag = et_Tag.getText().toString();
                voteTagUpdate tagUpdateRequest = new voteTagUpdate(NUMBER, thread_i+1, tag, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                }
                });
                Volley.newRequestQueue(Vote_new.this).add(tagUpdateRequest);
            }
        }
    };
    ImageButton.OnClickListener ocn2 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(CASE.equals("new")){
                DialogInterface.OnClickListener nullListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ib_image.setImageBitmap(null);
                    }
                };
                DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        doTakeAlbumAction();
                    }
                };
                new AlertDialog.Builder(Vote_new.this)
                        .setTitle("이미지 삽입/삭제")
                        .setPositiveButton("이미지 삭제", nullListener)
                        .setNegativeButton("앨범선택", albumListener)
                        .show();
            }
            //modify 상태에서는 이미지 수정 불가능
        }

    };

   /* public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte [] arr = baos.toByteArray();
        String image = Base64.encodeToString(arr, Base64.DEFAULT);
        String tmp = "";
        try{
            tmp = "&imagedevice="+ URLEncoder.encode(image, "utf-8");
        }catch (Exception e){
            Log.e("exception", e.toString());
        }
        return tmp;
    }
*/

  /*  public void doTakePhotoAction(){//카메라에서 사진 촬영
        Intent intent_camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //임시 파일 경로 생성
        String url = "tmp_"+String.valueOf(System.currentTimeMillis())+".jpg";
        mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));

        intent_camera.putExtra(MediaStore.EXTRA_OUTPUT,mImageCaptureUri);
        startActivityForResult(intent_camera, 2);
    }*/
    public void doTakeAlbumAction(){//앨범에서 이미지 가져오기
        //앨범 호출
        Intent intent_album = new Intent(Intent.ACTION_PICK);
        intent_album.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent_album, 1);
    }
    private void storeCropImage(Bitmap bitmap, String filePath){//Bitmap 저장 부분
        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Team7";//저장할 위치 폴더 이름
        File directory_Team7 = new File(dirPath);//저장할 위치 폴더 생성
        if(!directory_Team7.exists())
            directory_Team7.mkdir();

        File copyFile = new File(filePath);//사진 파일을 저장할 위치 파일에 생성
        BufferedOutputStream out = null;

        try{
            copyFile.createNewFile();
            out = new BufferedOutputStream(new FileOutputStream(copyFile));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(copyFile)));
            out.flush();
            out.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if (resultCode!=RESULT_OK)
            return ;
        switch (requestCode){
            case 1://앨범에서 사진 고르고 이미지 처리
                mImageCaptureUri = data.getData();
                Log.d("Team7", mImageCaptureUri.getPath().toString());
                Intent intent0 = new Intent("com.android.camera.action.CROP");
                intent0.setDataAndType(mImageCaptureUri,"image/*");
                //크롭할 이미지를 200x200 크기로 저장
                intent0.putExtra("outputX",200);
                intent0.putExtra("outputY", 200);
                intent0.putExtra("aspectX", 1);
                intent0.putExtra("aspectY", 1);
                intent0.putExtra("scale", true);
                intent0.putExtra("return-data", true);
                startActivityForResult(intent0, 2);
                break;
            case 2://이미지 크롭
                if (resultCode!=RESULT_OK)
                    return ;
                final Bundle extras = data.getExtras();
                //크롭된 이미지를 저장하기 위한 파일 경로
                String filePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Team7/"+System.currentTimeMillis()+".jpg";
                if(extras!=null){
                    Bitmap photo = extras.getParcelable("data");//크롭된 비트맵
                    ib_image.setImageBitmap(photo);//이미지 버튼에 비트맵 출력
                    bitmap = photo;

                    uploadBitmap(photo);
                    storeCropImage(photo, filePath);//크롭된 이미지를 외부저장소, 앨범에 저장
                    absolutePath = filePath;
                    break;
                }
                //임시 파일 삭제
                File f = new File(mImageCaptureUri.getPath());
                if(f.exists())
                    f.delete();
        }
    }
    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void uploadBitmap(final Bitmap bitmap) {

        //getting the tag from the edittext
        final String tags = et_Tag.getText().toString().trim();

        //our custom volley request
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, EndPoints.UPLOAD_URL,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            /*
            * If you want to add more parameters with the image
            * you can do it here
            * here we have only one parameter with the image
            * which is tags
            * */
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("NUMBER",0+"");//NUMBER+""); // 오른쪽 변수에 다가 게시글 넘버 받아와야함.
                params.put("tags", tags);
                params.put("Cand_Num",i+"");
                return params;
            }

            /*
            * Here we are passing image by renaming it with a unique name
            * */
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("pic", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };

        //adding the request to volley
        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(CASE.equals("new")){
            DialogInterface.OnClickListener deleteListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    votecontentDelete contentDeleteRequest = new votecontentDelete(new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                        }
                    });
                    Volley.newRequestQueue(Vote_new.this).add(contentDeleteRequest);
                    Intent intent1 = new Intent(Vote_new.this, Vote_Activity.class);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent1);
                    finish();
                }
            };
            new AlertDialog.Builder(Vote_new.this)
                    .setTitle("게시글 작성 화면에서 벗어나시겠습니까? 작성한 내용은 지워집니다.")
                    .setPositiveButton("확인", deleteListener)
                    .setNegativeButton("이어서 작성", null)
                    .show();
        }else{//modify
            DialogInterface.OnClickListener nullListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent1 = new Intent(Vote_new.this, Vote_Activity.class);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent1);
                    finish();
                }
            };
            new AlertDialog.Builder(Vote_new.this)
                    .setTitle("게시글 수정 화면에서 벗어나시겠습니까? 작성한 내용은 저장되지 않습니다.")
                    .setPositiveButton("확인", nullListener)
                    .setNegativeButton("이어서 작성", null)
                    .show();
        }

        return super.onKeyDown(keyCode, event);
    }
}

