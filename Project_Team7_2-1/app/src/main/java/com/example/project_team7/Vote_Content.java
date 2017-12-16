package com.example.project_team7;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;

/**
 * Created by 주현 on 2017-11-28.
 */

public class Vote_Content extends AppCompatActivity {
    TextView tv_category;
    TextView tv_title;
    TextView tv_nick;
    TextView tv_start;
    TextView tv_time;
    TextView tv_tag;
    ImageButton[] ib_image_array;
    int cand_cnt=0;
    Button bt_modify;
    Button bt_delete;

    String login_nick = null;
    int NUMBER = 0;
    String Url = "http://towsung.cafe24.com/uploads/";
    String tag = null;
    String image = null;
    int Cand_Num = 0;
    int h=0;
    int hand = 0;


    LayoutInflater mInflater2;
    LinearLayout mRoot1Linear2;

    //int i=0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_votecontent);

        tv_category = (TextView) findViewById(R.id.tv_category);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_nick = (TextView) findViewById(R.id.tv_nick);
        tv_start = (TextView) findViewById(R.id.tv_start);
        tv_time = (TextView) findViewById(R.id.tv_time);
        //액션바 타이틀 변경
        getSupportActionBar().setTitle("투표 게시판");

        Intent intent2 = getIntent();//후보 개수 전달

        login_nick = intent2.getStringExtra("login_nick").toString();
        String NICKNAME = intent2.getStringExtra("NICKNAME").toString();
        String TIMELIMIT = intent2.getStringExtra("TIMELIMIT").toString();

        bt_modify = (Button) findViewById(R.id.bt_modify);
        bt_delete = (Button) findViewById(R.id.bt_delete);
        if (login_nick.equals(NICKNAME)) {//현재 로그인된 사람이 이 글의 작성자일 때
            bt_modify.setVisibility(View.VISIBLE);
            bt_delete.setVisibility(View.VISIBLE);
        } else {
            bt_modify.setVisibility(View.INVISIBLE);
            bt_delete.setVisibility(View.INVISIBLE);
        }

        mInflater2 = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        mRoot1Linear2 = (LinearLayout) findViewById(R.id.linear2_root1);

        voteGetNumRequest getnumRequest = new voteGetNumRequest(NICKNAME, TIMELIMIT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonR = new JSONObject(response);
                    NUMBER = jsonR.getInt("NUMBER");
                    //vote_contents 글넘버 update
                    votecontentNumRequest contentNumRequest = new votecontentNumRequest(NUMBER, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //글넘버로부터 vote내용 받아오기
                            voteGetFromNumRequest getFromNumrequest = new voteGetFromNumRequest(NUMBER, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonResponse = new JSONObject(response);
                                        String TITLE = jsonResponse.getString("TITLE");
                                        String NICKNAME = jsonResponse.getString("NICKNAME");
                                        String STARTTIME = jsonResponse.getString("STARTTIME");
                                        String TIMELIMIT = jsonResponse.getString("TIMELIMIT");
                                        String CATEGORY = jsonResponse.getString("CATEGORY");
                                        tv_title.setText(TITLE);
                                        tv_category.setText(CATEGORY);
                                        tv_nick.setText(NICKNAME);
                                        tv_start.setText(STARTTIME);
                                        tv_time.setText(TIMELIMIT);

                                        imageDownRequest imagdown = new imageDownRequest(NUMBER,  new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    JSONArray candidate = new JSONArray(response);
                                                    cand_cnt = candidate.length();
                                                    ib_image_array = new ImageButton[cand_cnt];
                                                    for(int c=0;c<candidate.length();c++)
                                                        mInflater2.inflate(R.layout.inflate2, mRoot1Linear2, true);
                                                    for (h=0;h<candidate.length();h++){
                                                        JSONObject candidate_data = candidate.getJSONObject(h);
                                                        tag = candidate_data.getString("tags");
                                                        tv_tag = (TextView) mRoot1Linear2.getChildAt(h).findViewById(R.id.tv_item2);
                                                        tv_tag.setText(tag);
                                                        image = candidate_data.getString("image");
                                                        ib_image_array[h] = (ImageButton)mRoot1Linear2.getChildAt(h).findViewById(R.id.ib_item2);

                                                        if (image.equals("null")){
                                                            Picasso.with(Vote_Content.this)
                                                                    .load(Url+"ic_crop_original_black_48dp.png".toString())
                                                                    .into(ib_image_array[h]);
                                                        }
                                                        else {
                                                            Picasso.with(Vote_Content.this)
                                                                    .load(Url+image.toString())
                                                                    .into(ib_image_array[h]);
                                                         }

                                                        ib_image_array[h].setOnClickListener(imag_ocn);


                                                    }
                                                 } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                        RequestQueue queue3 = Volley.newRequestQueue(Vote_Content.this);
                                        queue3.add(imagdown);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            RequestQueue queue = Volley.newRequestQueue(Vote_Content.this);
                            queue.add(getFromNumrequest);
                        }
                    });
                    RequestQueue queue2 = Volley.newRequestQueue(Vote_Content.this);
                    queue2.add(contentNumRequest);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        RequestQueue queue5 = Volley.newRequestQueue(Vote_Content.this);
        queue5.add(getnumRequest);


        bt_modify.setOnClickListener(ocn);
        bt_delete.setOnClickListener(ocn);

    }

    ImageButton.OnClickListener imag_ocn = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {

            DialogInterface.OnClickListener voteListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    doCount(view);
                }
            };
            new AlertDialog.Builder(Vote_Content.this)
                    .setTitle("투표하시겠습니까?")
                    .setNegativeButton("네", voteListener)
                    .setPositiveButton("취소", null)
                    .show();


        }
    };
    public void doCount(View view){//앨범에서 이미지 가져오기
        for (int i=0;i<cand_cnt;i++){
            if(ib_image_array[i]==view)
                Cand_Num = i+1;
        }
        voteCandidateCountUp candCount = new voteCandidateCountUp(NUMBER, Cand_Num, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(Vote_Content.this,"투표를 하였습니다.",Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(Vote_Content.this).add(candCount);
    }


    Button.OnClickListener ocn = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.bt_result://결과확인 버튼을 눌렀을 때
                    //투표 확인 결과 activity로 넘어감
                case R.id.bt_modify://수정하기 버튼을 눌렀을 때
                    Intent intent_modify = new Intent(Vote_Content.this, Vote_new.class);
                    intent_modify.putExtra("case", "modify");
                    intent_modify.putExtra("NUMBER", String.valueOf(NUMBER));
                    intent_modify.putExtra("login_nick", login_nick);
                    startActivity(intent_modify);
                    finish();
                    break;
                case R.id.bt_delete://삭제하기 버튼을 눌렀을 때
                    voteDeleteRequest deleteRequest = new voteDeleteRequest(NUMBER, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(Vote_Content.this, "삭제되었습니다", Toast.LENGTH_SHORT).show();

                            Intent intent_delete = new Intent(Vote_Content.this, Vote_Activity.class);
                            intent_delete.putExtra("case", "delete");
                            intent_delete.putExtra("login_nick", login_nick);
                            intent_delete.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent_delete);
                            finish();
                        }
                    });
                    RequestQueue queue2 = Volley.newRequestQueue(Vote_Content.this);
                    queue2.add(deleteRequest);

                    break;
            }
        }
    };


    //액션버튼 메뉴 액션바에 집어 넣기
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    //액션버튼은 클릭했을 때의 동작
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.action_bt_home){
            //홈버튼 눌렀을때 처음 메인 화면으로 가도록 코드 구현하기
            return true;
        }
        else if(id == R.id.action_bt_logout){
            //로그아웃 버튼 눌렀을 때 로그아웃 되도록 코드 구현하기
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Intent intent_b = new Intent(Vote_Content.this, Vote_Activity.class);
        intent_b.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent_b);
        finish();
        return super.onKeyDown(keyCode, event);
    }


}
