package uni.fisal.king.dttbgci;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.toolbox.StringRequest;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import uni.fisal.king.dttbgci.Firebase_notification.MySingleton;
import uni.fisal.king.dttbgci.Firebase_notification.save_token;

public class MainActivity extends AppCompatActivity {

    //define EditText element (field)
    EditText username, password;
    //define string element
    String user;


    // this function for create login activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // link EditText has been created here with that was in XML file (activity_main.xml)
        // by id.
        username = findViewById(R.id.login_un);
        password = findViewById(R.id.login_pw);


    }

    // this function represent login button
    public void login(View view) {


        String  pw;

        //check if user enter his username and password
        if (username.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(),
                    "Please Enter Your Username and Password", Toast.LENGTH_SHORT).show();
        } else {
            // get text that typed in username field and password field
            user = username.getText().toString();
            pw = password.getText().toString();

            login_with_db obj = new login_with_db(); //define new object for login_with_db class
            obj.execute(pw); //execute obj object of "login_with_db" class with pw as attribute

        }
    }

    //these tow function below will launch RegisterActivity
    public void go_to_register_diabetic(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.putExtra("user_type","diabetic");
        startActivity(intent);
    }

    public void go_to_register_doctor(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.putExtra("user_type","doctor");
        startActivity(intent);
    }



    /* this is helper class allows you to perform background operations and publish results
     on the UI thread without having to manipulate threads and/or handlers*/

    private class login_with_db extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            // receiving attributes
            String password = params[0];

            //define c1 as object of connect class
            connect c1 =new connect();

            // prepare parameters to sending through login.php file
            String urlParams = "a=" + user + "&b=" + password;

            String return_msg = c1.make_connect("login.php",urlParams);


            return return_msg;// will send value to onPostExecute function below
        }

        @Override
        protected void onPostExecute(String s)
        {


            String user_type, coming_msg;
            boolean response_status;

            try{
                // declare new json array to receive data from database
                JSONObject root = new JSONObject(s);

                //take first element of JSON object
                JSONObject user_data = root.getJSONObject("response");

                // inside user_data object there is sub object called status
                response_status = user_data.getBoolean("status");

                // also there is another sub object called message
                coming_msg = user_data.getString("message");

                if(!response_status) { // means there is an error in login process

                    //this to inform user by message
                    Toast.makeText(getApplicationContext(), coming_msg, Toast.LENGTH_LONG).show();

                }

                else{
                    user_type = user_data.getString("user_type");
                    s ="Successfully login, Welcome " + user + " to Diabetes Tracker"; // success message
                    Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();

                    //save username to global class which has get/set function
                    final global_variables globalVariable = (global_variables) getApplicationContext();
                    globalVariable.set_username(user);
                    globalVariable.set_usertype(user_type);

                    if(user_type.equals("diabetic")) {
                        // this is for get token (device ID ) and record it into database
                        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.FCM_PREF), Context.MODE_PRIVATE);
                        String ff = FirebaseInstanceId.getInstance().getToken();
                        final String token = sharedPreferences.getString(getString(R.string.FCM_TOKEN), ff);
                        save_token save_token = new save_token();
                        StringRequest request = save_token.insert(token, user);
                        MySingleton.getmInstence(MainActivity.this).addToRequestQueue(request);
                    }


                    Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(i);

                }

            }

            catch (JSONException e)
            {
                e.printStackTrace();
            }


        }

    }

    // we override this function to disable back button for login page on android device
    @Override
    public void onBackPressed() {
    }

}
