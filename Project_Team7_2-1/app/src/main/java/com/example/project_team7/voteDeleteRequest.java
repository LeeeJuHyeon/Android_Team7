package com.example.project_team7;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 주현 on 2017-12-05.
 */

public class voteDeleteRequest extends StringRequest {
    final static private String URL = "http://towsung.cafe24.com/vote_delete.php";

    private Map<String,String> parameters;

    public voteDeleteRequest(int NUMBER, Response.Listener<String>listener){
        super(Method.POST,URL, listener,null);
        parameters = new HashMap<>();
        parameters.put("NUMBER", NUMBER+"");
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
