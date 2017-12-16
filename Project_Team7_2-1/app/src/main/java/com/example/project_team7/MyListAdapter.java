package com.example.project_team7;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by 주현 on 2017-11-28.
 */

public class MyListAdapter extends BaseAdapter{
    Context context;
    ArrayList<list_item> list_itemArrayList;

    TextView nickname;
    TextView title;
    TextView timelimit;

    public MyListAdapter(Context context, ArrayList<list_item> list_itemArrayList) {
        this.context = context;
        this.list_itemArrayList = list_itemArrayList;
    }

    @Override
    public int getCount() {//이 리스트뷰가 몇 개의 아이템을 가지고 있는지
        return this.list_itemArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return this.list_itemArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null){
            view = LayoutInflater.from(context).inflate(R.layout.list_item,null);
            nickname = (TextView)view.findViewById(R.id.list_nick);
            title = (TextView)view.findViewById(R.id.list_title);
            timelimit = (TextView)view.findViewById(R.id.list_limit);
        }
        nickname.setText(list_itemArrayList.get(i).getNickname());
        title.setText(list_itemArrayList.get(i).getTitle());
        timelimit.setText(list_itemArrayList.get(i).getTime_limit());
        return view;
    }
}
