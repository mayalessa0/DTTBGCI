package uni.fisal.king.dttbgci.Firebase_notification;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class save_token {

    String server_url = "http://uniproject.co/fcm/fcm_insert.php";

    public StringRequest insert(final String token, final String username)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<>();
                params.put("fcm_token",token);
                params.put("username",username);
                return params;
            }
        };
        return stringRequest;
    }
}
