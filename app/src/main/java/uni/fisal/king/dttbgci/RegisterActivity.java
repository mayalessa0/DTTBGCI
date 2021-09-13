package uni.fisal.king.dttbgci;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.StringRequest;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import uni.fisal.king.dttbgci.Firebase_notification.MySingleton;
import uni.fisal.king.dttbgci.Firebase_notification.save_token;

public class RegisterActivity extends AppCompatActivity {
    EditText username_ET, password_ET, name_ET ,
            mobile_ET, id_ET, address_ET, certificate_ET;

    String username, user_type;

    //define drop list field
    Spinner spinner_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);



        // define EditText by id

        username_ET = findViewById(R.id.date);
        password_ET = findViewById(R.id.title);
        name_ET = findViewById(R.id.content);
        mobile_ET = findViewById(R.id.mobile_register);
        address_ET = findViewById(R.id.address_register);
        id_ET = findViewById(R.id.id_register);
        certificate_ET = findViewById(R.id.certif_register);
        TextView username_tag = findViewById(R.id.textView1);
        TextView certificate_TV = findViewById(R.id.textView4);
        TextView type_TV = findViewById(R.id.textView8);
        spinner_type =  findViewById(R.id.spinner);


        //receive parameter from past activity
        user_type = getIntent().getStringExtra("user_type");

        //change username tag into doctor id and disappear spinner field
        if (user_type.equals("doctor")){
            username_tag.setText("Doctor ID");
            type_TV.setVisibility(View.INVISIBLE);
            spinner_type.setVisibility(View.INVISIBLE);

        }else{
            //if user type is diabetic certificate field will disappeared
            certificate_ET.setVisibility(View.INVISIBLE);
            certificate_TV.setVisibility(View.INVISIBLE);


            // Create an ArrayAdapter using the string array and a default spinner layout
            ArrayAdapter<CharSequence> adapter_type = ArrayAdapter.createFromResource(this,
                    R.array.diabetes_type, android.R.layout.simple_spinner_item);

            // Specify the layout to use when the list of choices appears
            adapter_type.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Apply the adapter to the spinner
            spinner_type.setAdapter(adapter_type);




        }
    }

    public void register(View view) {

        String password, name, mobile, address, id, certifi, diabetes_type;

        username = username_ET.getText().toString().trim();
        password = password_ET.getText().toString().trim();
        name = name_ET.getText().toString().trim();
        mobile = mobile_ET.getText().toString().trim();
        address = address_ET.getText().toString().trim();
        id = id_ET.getText().toString().trim();
        certifi = certificate_ET.getText().toString().trim();
        diabetes_type = String.valueOf(spinner_type.getSelectedItem());


        // flag to check invalid input
        boolean error = false;


        //format of phone num should be like 9665xxxxxxxx
        String regexStr = "^(9665)?[0-9]{12}$";

        //check length of input
        if (username.length() > 20 || username.isEmpty()) {
            // show error message if the input is empty or more than 15 digit
            Toast.makeText(getApplicationContext(),
                    "Please fill (Username) field and don't exceed 20 digit.", Toast.LENGTH_LONG).show();
            error = true;
            return;
        }
        if (password.length() > 20 || password.isEmpty()) {
            // show error message if the input is empty or more than 15 digit
            Toast.makeText(getApplicationContext(),
                    "Please fill (Password) field and don't exceed 20 digit.", Toast.LENGTH_LONG).show();
            error = true;
            return;
        }

        if (name.length() > 50 || name.isEmpty()) {
            // show error message if the input is empty or more than 50
            Toast.makeText(getApplicationContext(),
                    "Please fill (Name) field and don't exceed 50 digit.", Toast.LENGTH_LONG).show();
            error = true;
            return;

        }


        if (address.length() > 50 || address.isEmpty()) {
            // show error message if the input is empty or more than 50
            Toast.makeText(getApplicationContext(),
                    "Please fill (Address) field and don't exceed 50 digit.", Toast.LENGTH_LONG).show();
            error = true;
            return;

        }


        if (!mobile.matches(regexStr)) {
            // show error message if the input is empty or not phone
            Toast.makeText(getApplicationContext(),
                    "Invalid Phone number.", Toast.LENGTH_LONG).show();
            error = true;
            return;

        }

        if (id.length() > 10 || id.isEmpty()) {
            // show error message if the input is empty or more than 10
            Toast.makeText(getApplicationContext(),
                    "Please fill (National ID) field and don't exceed 10 digit", Toast.LENGTH_LONG).show();
            error = true;
            return;

        }
        if (user_type.equals("doctor")){
            if (certifi.isEmpty()) {
                // show error message if the input is empty or more than 10
                Toast.makeText(getApplicationContext(),
                        "Please fill (Certificate) field", Toast.LENGTH_LONG).show();
                error = true;
                return;

            }
        }



        if (!error) {

            //define new object for register_with_db class
            register_with_db obj = new register_with_db();

            //execute obj object of "register_with_db" class with 8 attributes
            obj.execute(password, name, mobile, address, id, certifi, diabetes_type);



        }
    }



    /* this is helper class allows you to perform background operations and publish results
     on the UI thread without having to manipulate threads and/or handlers*/

    class register_with_db extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            // receiving attributes
            String pw = params[0];
            String full_name = params[1];
            String phone_num = params[2];
            String address = params[3];
            String id = params[4];
            String certificate = params[5];
            String diabetes_type = params[6];

            //define c1 as object of connect class
            connect c1 =new connect();

            // prepare parameters to sending through register.php file
            String urlParams
                    = "a=" + username
                    + "&b=" + pw
                    + "&c=" + full_name
                    + "&d=" + phone_num
                    + "&e=" + address
                    + "&f=" + id
                    + "&g=" + certificate
                    + "&h=" + user_type
                    + "&i=" + diabetes_type;

            String return_msg = c1.make_connect("register.php",urlParams);


            return return_msg;// will send to onPostExecute function below
        }

        @Override
        protected void onPostExecute(String s)
        {
            String coming_msg;
            if(s.equals("")){ // means there is no error

                // show succeeded message
                coming_msg="Successful Register .. \n Welcome " + username + " To Diabetes Tracker.";
                Toast.makeText(getApplicationContext(), coming_msg, Toast.LENGTH_LONG).show();


                //save username to global class which has get/set function
                final global_variables globalVariable = (global_variables) getApplicationContext();
                globalVariable.set_username(username);
                globalVariable.set_usertype(user_type);

                if(user_type.equals("diabetic")) {
                    // this is for get token (device ID ) and record it into database
                    SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.FCM_PREF), Context.MODE_PRIVATE);
                    String ff = FirebaseInstanceId.getInstance().getToken();
                    final String token = sharedPreferences.getString(getString(R.string.FCM_TOKEN), ff);
                    save_token save_token = new save_token();
                    StringRequest request = save_token.insert(token, username);
                    MySingleton.getmInstence(RegisterActivity.this).addToRequestQueue(request);
                }


                Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(i);





            }
            else
            {
                try {
                    // declare new json array to receive data from data base
                    JSONObject root = new JSONObject(s);

                    // assign the text inside JSON object to coming_msg
                    coming_msg = root.getString("message");

                    // show error message that came from data base
                    Toast.makeText(getApplicationContext(), coming_msg, Toast.LENGTH_LONG).show();



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }

    }

}
