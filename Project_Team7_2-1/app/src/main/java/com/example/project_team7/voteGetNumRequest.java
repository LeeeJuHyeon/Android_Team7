package com.example.project_team7;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 주현 on 2017-12-01.
 */

public class voteGetNumRequest extends StringRequest {
    final static private String URL = "http://towsung.cafe24.com/vote_getnum.php";

    private Map<String,String> parameters;

    public voteGetNumRequest(String NICKNAME, String TIMELIMIT, Response.Listener<String>listener){
        super(Method.POST,URL, listener,null);
        parameters = new HashMap<>();
        parameters.put("NICKNAME", NICKNAME);
        parameters.put("TIMELIMIT", TIMELIMIT);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
