package com.example.project_team7;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 주현 on 2017-12-11.
 */

public class voteTagUpdate extends StringRequest {
    final static private String URL = "http://towsung.cafe24.com/votecontents_modify.php";

    private Map<String,String> parameters;

    public voteTagUpdate(int NUMBER, int Cand_Num, String tags, Response.Listener<String>listener){
        super(Method.POST,URL, listener,null);
        parameters = new HashMap<>();
        parameters.put("NUMBER", NUMBER+"");
        parameters.put("Cand_Num", Cand_Num+"");
        parameters.put("tags", tags);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
