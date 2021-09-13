package uni.fisal.king.dttbgci;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class View_profileActivity extends AppCompatActivity {

    TextView username_TV, password_TV, name_TV ,
            mobile_TV, id_TV, address_TV, certifi_TV, diabetes_type_TV;
    Button edit_button, go_to_medic;

    String username, user_type;
    String previose_activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);


        // define EditText by id

        username_TV = findViewById(R.id.textView10);
        password_TV = findViewById(R.id.pw_edit);
        name_TV = findViewById(R.id.full_name_edit);
        mobile_TV = findViewById(R.id.mobile_edit);
        address_TV = findViewById(R.id.address_edit);
        id_TV = findViewById(R.id.id_edit);
        certifi_TV = findViewById(R.id.certif_edit);
        TextView certificate_TV = findViewById(R.id.textView4);
        TextView type_TV = findViewById(R.id.textView8);
        diabetes_type_TV =  findViewById(R.id.type_TV);
        edit_button = findViewById(R.id.button4);
        go_to_medic = findViewById(R.id.button23);


        //receive parameter from past activity (searchActivity.java)
        user_type = getIntent().getStringExtra("user_type");
        username  = getIntent().getStringExtra("username");

        previose_activity = getIntent().getStringExtra("past_activity");

        //if there is no parameters so we will take username and user_type values
        // that had been saved in global class (global_variables)
        if(user_type == null&& username == null) {

            global_variables globalVariable = (global_variables) getApplicationContext();
            username = globalVariable.get_username();
            user_type = globalVariable.get_usertype();
            // disappear this button
            go_to_medic.setVisibility(View.INVISIBLE);
        }
        else{
            edit_button.setVisibility(View.INVISIBLE);
        }



        username_TV.setText(username);



        //disappear type field
        if (user_type.equals("doctor")){
            type_TV.setVisibility(View.INVISIBLE);
            diabetes_type_TV.setVisibility(View.INVISIBLE);

        }else{
            //if user type is diabetic certificate field will disappeared
            certificate_TV.setVisibility(View.INVISIBLE);
            certifi_TV.setVisibility(View.INVISIBLE);

        }

        get_user_info obj = new get_user_info();
        obj.execute(username);
    }




    class get_user_info extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {

            String urlParams
                    = "a=" + username
                    + "&b="+ user_type;

            connect c1 = new connect();
            String return_msg= c1.make_connect("get_user_info.php", urlParams);
            return return_msg;
        }

        @Override
        protected void onPostExecute(String responseBody)
        {
            String 	Password, name, mobile, id, address, certificate, type;

            if(responseBody.equals("")){ // means there is an error
                Toast.makeText(getApplicationContext(), "failed response from database!!", Toast.LENGTH_LONG).show();
            }
            else
            {
                try {

                    JSONObject json = new JSONObject(responseBody);

                    JSONArray ja_data = json.getJSONArray("posts");
                    JSONObject jObj = ja_data.getJSONObject(0);
                    String s = jObj.getString("post");
                    JSONObject jObject = new JSONObject(s);
                    //Password = jObject.getString("password");
                    //password_TV.setText(Password);
                    name = jObject.getString("full_name");
                    name_TV.setText(name);
                    mobile = jObject.getString("phone_number");
                    mobile_TV.setText(mobile);
                    id = jObject.getString("national_id");
                    id_TV.setText(id);
                    address = jObject.getString("address");
                    address_TV.setText(address);
                    if(user_type.equals("doctor")){
                        certificate = jObject.getString("certificate");
                        certifi_TV.setText(certificate);}
                    else{
                        type = jObject.getString("diabetes_type");
                        diabetes_type_TV.setText(type);
                    }
                }

                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void Edit(View view) {

        Intent i = new Intent(getApplicationContext(), Edit_profileActivity.class);
        startActivity(i);
    }


    @Override
    public void onBackPressed() {
        if(previose_activity.equals("SearchActivity")){

            startActivity(new Intent(getApplicationContext(), SearchActivity.class));

        }
        if(previose_activity.equals("HomeActivity")){

            startActivity(new Intent(getApplicationContext(), HomeActivity.class));


        }

    }

    public void view_medi_info(View view) {

        get_schedules obj = new get_schedules();
        obj.execute();
    }

    class get_schedules extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {

            String urlParams = "a=" + username;


            connect c1 = new connect();
            String return_msg= c1.make_connect("get_medications_list.php", urlParams);
            return return_msg;
        }

        @Override
        protected void onPostExecute(String responseBody)
        {
            if(responseBody.equals("")){ // means there is an error
                Toast.makeText(getApplicationContext(), "Failed from Database side", Toast.LENGTH_LONG).show();
            }
            else
            {
                Intent intent = new Intent(getApplicationContext(), List_of_schedulesActivity.class);
                intent.putExtra("schedules" , responseBody);
                startActivity(intent);

            }
        }
    }

}
