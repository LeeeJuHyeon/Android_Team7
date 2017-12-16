package com.example.project_team7;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Vote_Activity extends AppCompatActivity {

    ListView lViewFood;
    ListView lViewBeauty;
    ListView lViewCulture;
    ListView lViewEtc;
    //LayoutInflater mInflater_beauty;
    //boolean mLockListView_beauty;
    MyListAdapter myListAdapter_food;
    ArrayList<list_item> list_itemArrayList_food = new ArrayList<list_item>();
    MyListAdapter myListAdapter_beauty;
    ArrayList<list_item> list_itemArrayList_beauty = new ArrayList<list_item>();
    MyListAdapter myListAdapter_culture;
    ArrayList<list_item> list_itemArrayList_culture = new ArrayList<list_item>();
    MyListAdapter myListAdapter_etc;
    ArrayList<list_item> list_itemArrayList_etc = new ArrayList<list_item>();
    String NICKNAME = null;
    String TIMELIMIT = null;
    String NUMBER = null;
    String category = null;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);i++;

        Intent intent = getIntent();//투표,채팅방 선택 activity로부터 & vote_content acitivy로부터
        if(intent.getStringExtra("case")=="delete"){
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //액션바 타이틀 변경
        getSupportActionBar().setTitle("투표 게시판");
        findViewById(R.id.ib_food).setOnClickListener(ocn);
        findViewById(R.id.ib_beauty).setOnClickListener(ocn);
        findViewById(R.id.ib_culture).setOnClickListener(ocn);
        findViewById(R.id.ib_etc).setOnClickListener(ocn);

        lViewFood = (ListView)findViewById(R.id.list1);
        lViewBeauty = (ListView)findViewById(R.id.list2);
        lViewCulture = (ListView)findViewById(R.id.list3);
        lViewEtc = (ListView)findViewById(R.id.list4);
        if(i==1)
            ocn.onClick(findViewById(R.id.ib_food));//처음에 food 게시글 출력되도록

        lViewFood.setOnItemClickListener(ocl);
        lViewBeauty.setOnItemClickListener(ocl);
        lViewCulture.setOnItemClickListener(ocl);
        lViewEtc.setOnItemClickListener(ocl);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                Intent intent = new Intent(Vote_Activity.this, Vote_new.class);
                intent.putExtra("case", "new");
                startActivity(intent);//글쓰는 activity로 넘어간다.
                finish();
            }
        });

        BackThread2 thread = new BackThread2();
        thread.setDaemon(true);
        thread.start();
    }
    class BackThread2 extends Thread{//마감되면 +마감 적기
        @Override
        public void run() {
            super.run();
            while(true){
                handler2.sendEmptyMessage(0);
                try{
                    Thread.sleep(500);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }

        }
    }
    Handler handler2 = new Handler(){
        @Override
        public void handleMessage(Message msg){
            if(msg.what == 0){
                //마감시간 지난거 timelimit에 마감으로 update
            }
        }
    };
    //글 목록에서 하나의 글 선택 시
    ListView.OnItemClickListener ocl = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            Intent intent2 = new Intent(Vote_Activity.this, Vote_Content.class);
            if(category.equals("food")){
                NICKNAME = list_itemArrayList_food.get(i).getNickname();
                TIMELIMIT = list_itemArrayList_food.get(i).getTime_limit();
            }else if(category.equals("beauty")){
                NICKNAME = list_itemArrayList_beauty.get(i).getNickname();
                TIMELIMIT = list_itemArrayList_beauty.get(i).getTime_limit();
            }else if(category.equals("culture")){
                NICKNAME = list_itemArrayList_culture.get(i).getNickname();
                TIMELIMIT = list_itemArrayList_culture.get(i).getTime_limit();
            }else{
                NICKNAME = list_itemArrayList_etc.get(i).getNickname();
                TIMELIMIT = list_itemArrayList_etc.get(i).getTime_limit();
            }
            String login_nick = "textnick";

            intent2.putExtra("NICKNAME",NICKNAME);
            intent2.putExtra("TIMELIMIT",TIMELIMIT);
            intent2.putExtra("login_nick", login_nick);
            startActivity(intent2);//글 내용 출력 activity로 넘어감
        }
    };
    int i1=0;int i2=0;int i3=0;int i4=0;
    ImageButton.OnClickListener ocn = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            lViewFood.setVisibility(View.INVISIBLE);
            lViewBeauty.setVisibility(View.INVISIBLE);
            lViewCulture.setVisibility(View.INVISIBLE);
            lViewEtc.setVisibility(View.INVISIBLE);
            final int id = view.getId();

            Response.Listener<String> resl = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    boolean chk = false;
                    switch (id){
                        case R.id.ib_food:
                            if(i1==0)chk=true;//1이면 response 실행
                            category = "food";
                            i1++;break;
                        case R.id.ib_beauty:
                            if(i2==0)chk=true;
                            category = "beauty";
                            i2++;break;
                        case R.id.ib_culture:
                            if(i3==0)chk=true;
                            category = "culture";
                            i3++;break;
                        case R.id.ib_etc:
                            if(i4==0)chk=true;
                            category = "etc";
                            i4++;break;
                    }
                    try{
                        JSONArray jArray = new JSONArray(response);
                        for(int i=0;i<jArray.length();i++){
                            JSONObject json_data = jArray.getJSONObject(i);
                            String TITLE = json_data.getString("TITLE");
                            String NICKNAME = json_data.getString("NICKNAME");

                            String TIMELIMIT = json_data.getString("TIMELIMIT");

                            switch (id){
                                case R.id.ib_food:
                                    i1++;
                                    if(chk==true){
                                        list_itemArrayList_food.add(new list_item(TITLE,NICKNAME,TIMELIMIT));
                                    }
                                    lViewFood.setVisibility(View.VISIBLE);
                                    myListAdapter_food = new MyListAdapter(Vote_Activity.this, list_itemArrayList_food);
                                    lViewFood.setAdapter(myListAdapter_food);
                                    break;
                                case R.id.ib_beauty:
                                    if(chk==true)
                                    list_itemArrayList_beauty.add(new list_item(TITLE,NICKNAME,TIMELIMIT));
                                    lViewBeauty.setVisibility(View.VISIBLE);
                                    myListAdapter_beauty = new MyListAdapter(Vote_Activity.this, list_itemArrayList_beauty);
                                    lViewBeauty.setAdapter(myListAdapter_beauty);
                                    break;
                                case R.id.ib_culture:
                                    if(chk==true)
                                    list_itemArrayList_culture.add(new list_item(TITLE,NICKNAME,TIMELIMIT));
                                    lViewCulture.setVisibility(View.VISIBLE);
                                    myListAdapter_culture = new MyListAdapter(Vote_Activity.this, list_itemArrayList_culture);
                                    lViewCulture.setAdapter(myListAdapter_culture);
                                    break;
                                case R.id.ib_etc:
                                    if(chk==true)
                                    list_itemArrayList_etc.add(new list_item(TITLE,NICKNAME,TIMELIMIT));
                                    lViewEtc.setVisibility(View.VISIBLE);
                                    myListAdapter_etc = new MyListAdapter(Vote_Activity.this, list_itemArrayList_etc);
                                    lViewEtc.setAdapter(myListAdapter_etc);
                                    break;
                            }
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            };
            switch (id){
                case R.id.ib_food:
                    voteGetRequest getRequest1 = new voteGetRequest("food", resl);
                    RequestQueue queue1 = Volley.newRequestQueue(Vote_Activity.this);
                    queue1.add(getRequest1);break;
                case R.id.ib_beauty:
                    voteGetRequest getRequest2 = new voteGetRequest("beauty", resl);
                    RequestQueue queue2 = Volley.newRequestQueue(Vote_Activity.this);
                    queue2.add(getRequest2);break;
                case R.id.ib_culture:
                    voteGetRequest getRequest3 = new voteGetRequest("culture", resl);
                    RequestQueue queue3 = Volley.newRequestQueue(Vote_Activity.this);
                    queue3.add(getRequest3);break;
                case R.id.ib_etc:
                    voteGetRequest getRequest4 = new voteGetRequest("etc", resl);
                    RequestQueue queue4 = Volley.newRequestQueue(Vote_Activity.this);
                    queue4.add(getRequest4);break;
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
       // Intent intent_b = new Intent(Vote_Activity.this, Vote_Activity.class);//투표,채팅방 선택 액티비티로 가기
     //   startActivity(intent_b);
     //   startActivity(this, Vote_Activity.class);
        finish();
        return super.onKeyDown(keyCode, event);
    }
}
