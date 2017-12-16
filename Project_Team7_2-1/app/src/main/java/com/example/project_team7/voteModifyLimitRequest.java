package com.example.project_team7;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 주현 on 2017-12-06.
 */

public class voteModifyLimitRequest extends StringRequest {
    final static private String URL = "http://towsung.cafe24.com/vote_modifyLimit.php";

    private Map<String,String> parameters;

    public voteModifyLimitRequest(String TIMELIMIT, int NUMBER, Response.Listener<String>listener){
        super(Method.POST,URL, listener,null);
        parameters = new HashMap<>();
        parameters.put("TIMELIMIT", TIMELIMIT);
        parameters.put("NUMBER", NUMBER+"");
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
