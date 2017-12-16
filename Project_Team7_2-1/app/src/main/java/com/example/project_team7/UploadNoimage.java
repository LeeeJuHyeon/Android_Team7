package com.example.project_team7;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by LG on 2017-11-25.
 */

public class UploadNoimage extends StringRequest {

    final static private String URL = "http://towsung.cafe24.com/Upload_no_image.php";
    private Map<String, String> parameters;

    public UploadNoimage(int NUMBER, String tags, int Cand_Num, Response.Listener<String>listener){
        super(Method.POST,URL,listener,null);
        parameters = new HashMap<>();
        parameters.put("NUMBER",NUMBER+"");
        parameters.put("tags",tags);
        parameters.put("Cand_Num",Cand_Num+"");

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
