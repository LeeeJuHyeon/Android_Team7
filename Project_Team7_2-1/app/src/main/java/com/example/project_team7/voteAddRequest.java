package com.example.project_team7;

/**
 * Created by 주현 on 2017-11-29.
 */

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

public class voteAddRequest extends StringRequest {
    final static private String URL = "http://towsung.cafe24.com/vote.php";

    private Map<String,String> parameters;

    public voteAddRequest(String CATEGORY, String TITLE, String NICKNAME, String STARTTIME, String TIMELIMIT, Response.Listener<String>listener){
        super(Method.POST,URL, listener,null);
        parameters = new HashMap<>();
        parameters.put("CATEGORY", CATEGORY);
        parameters.put("TITLE", TITLE);
        parameters.put("NICKNAME", NICKNAME);
        parameters.put("STARTTIME", STARTTIME);
        parameters.put("TIMELIMIT", TIMELIMIT);
    }


    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
