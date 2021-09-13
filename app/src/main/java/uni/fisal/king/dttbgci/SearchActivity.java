package uni.fisal.king.dttbgci;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    String username, userType, selected_item, response;
    AutoCompleteTextView text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        get_search_suggestions obj = new get_search_suggestions();
        obj.execute();
        text = findViewById(R.id.autoCompleteTextView);


    }



    class get_search_suggestions extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {


            connect c1 = new connect();
            String return_msg= c1.make_connect("search_suggestions.php",null);
            return return_msg;
        }

        @Override
        protected void onPostExecute(String responseBody)
        {

            if(responseBody.equals("")){ // means there is an error
                Toast.makeText(getApplicationContext(), "failed response from database!!", Toast.LENGTH_LONG).show();
            }
            else
            {
                response = responseBody;
                decodeJSON(response);
            }
        }
    }

    public void view_profile(View view) {
        decodeJSON(response);
        String msg;

        if(text.getText().toString().isEmpty()){

            msg ="You should type correct username that you desire to view his info";
            Toast.makeText(getBaseContext(), msg , Toast.LENGTH_LONG).show();

        }else {
            selected_item = text.getText().toString();
            String[] data = selected_item.split("  -  ");
            if(data.length != 2){
                msg ="Make sure you entered correct user ID";
                Toast.makeText(getBaseContext(), msg , Toast.LENGTH_LONG).show();
            }
            else {
                username = data[0];
                userType= data[1];
                Intent i = new Intent(getApplicationContext(), View_profileActivity.class);
                i.putExtra("user_type", userType);
                i.putExtra("username", username);
                i.putExtra("past_activity", "SearchActivity");
                startActivity(i);
            }


        }
    }

    @Override
    public void onResume()
    {  // After a pause OR at startup
        super.onResume();
        text.setText("");

    }

    private void decodeJSON ( String responseBody){

        try {
            JSONObject json = new JSONObject(responseBody);

            JSONArray ja_data = json.getJSONArray("posts");


            final String[] str1 = new String[ja_data.length()];

            for(int i=0;i<ja_data.length();i++)
            {
                JSONObject jObj = ja_data.getJSONObject(i);
                String s = jObj.getString("post");
                JSONObject jObject = new JSONObject(s);
                str1[i]= jObject.getString("user_id") +"  -  " + jObject.getString("user_type");
            }


            final List<String> list = new ArrayList<String>();

            for(int i=0;i<str1.length;i++)
            {
                list.add(str1[i]);
            }

            Collections.sort(list);

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                    (getApplicationContext(), android.R.layout.simple_spinner_item, list);

            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            text.setThreshold(1);
            text.setAdapter(dataAdapter);
        }

        catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));

    }



}
