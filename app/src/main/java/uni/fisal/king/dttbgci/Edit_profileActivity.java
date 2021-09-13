package uni.fisal.king.dttbgci;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Edit_profileActivity extends AppCompatActivity {

    EditText password_ET, name_ET ,
            mobile_ET, id_ET, address_ET, certificate_ET;

    String username, user_type;

    //define drop list field
    Spinner spinner_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // define EditText by id

        password_ET = findViewById(R.id.pw_edit);
        name_ET = findViewById(R.id.full_name_edit);
        mobile_ET = findViewById(R.id.mobile_edit);
        address_ET = findViewById(R.id.address_edit);
        id_ET = findViewById(R.id.id_edit);
        certificate_ET = findViewById(R.id.certif_edit);
        TextView certificate_TV = findViewById(R.id.textView4);
        TextView type_TV = findViewById(R.id.textView8);
        spinner_type =  findViewById(R.id.spinner);


        //receive parameter from past activity
        user_type = getIntent().getStringExtra("user_type");

        global_variables globalVariable = (global_variables) getApplicationContext();
        username  = globalVariable.get_username();
        user_type = globalVariable.get_usertype();


        //change username tag into doctor id and disappear spinner field
        if (user_type.equals("doctor")){
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
            String 	Password, name, mobile, id, address, certificate, diabetes_type;

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
                    Password = jObject.getString("password");
                    password_ET.setText(Password);
                    name = jObject.getString("full_name");
                    name_ET.setText(name);
                    mobile = jObject.getString("phone_number");
                    mobile_ET.setText(mobile);
                    id = jObject.getString("national_id");
                    id_ET.setText(id);
                    address = jObject.getString("address");
                    address_ET.setText(address);
                    if(user_type.equals("doctor")){
                        certificate = jObject.getString("certificate");
                        certificate_ET.setText(certificate);}
                    else {
                        diabetes_type = jObject.getString("diabetes_type");
                        spinner_type.setSelection((Integer.valueOf(diabetes_type) - 1));
                    }
                }

                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void save(View view) {

        String 	Password, name, mobile, id, address, certificate, diabetes_type;

        Password = password_ET.getText().toString();
        name = name_ET.getText().toString().trim();
        mobile = mobile_ET.getText().toString();
        id = id_ET.getText().toString();
        address = address_ET.getText().toString();
        certificate = certificate_ET.getText().toString();
        diabetes_type = String.valueOf(spinner_type.getSelectedItem());
        // flag to check invalid input
        boolean error = false;


        //format of phone num should be like 9665xxxxxxxx
        String regexStr = "^(9665)?[0-9]{12}$";


        if (Password.length() > 20 || Password.isEmpty()) {
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



        if (!error) {

            //define new object for register_with_db class
            save_changes obj = new save_changes();
            //execute obj object of "register_with_db" class with 8 attributes
            obj.execute(Password, name, mobile, address, id, certificate, diabetes_type);



        }

    }

    /* this is helper class allows you to perform background operations and publish results
     on the UI thread without having to manipulate threads and/or handlers*/

    class save_changes extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            // receiving attributes
            String password = params[0];
            String name = params[1];
            String mob = params[2];
            String address = params[3];
            String id = params[4];
            String certifi = params[5];
            String dia_type = params[6];

            //define c1 as object of connect class
            connect c1 =new connect();

            // prepare parameters to sending through register.php file
            String urlParams
                    = "a=" + username
                    + "&b=" + password
                    + "&c=" + name
                    + "&d=" + mob
                    + "&e=" + address
                    + "&f=" + id
                    + "&g=" + certifi
                    + "&h=" + dia_type
                    + "&i=" + user_type;

            String return_msg = c1.make_connect("update_user.php",urlParams);


            return return_msg;// will send to onPostExecute function below
        }

        @Override
        protected void onPostExecute(String s)
        {
            String coming_msg;

            try {


                if(s.equals("")){ // means there is no error

                    coming_msg="Successful Updated";
                    Toast.makeText(getApplicationContext(), coming_msg, Toast.LENGTH_LONG).show();
                }
                else {
                    JSONObject root = new JSONObject(s);
                    coming_msg = root.getString("message");

                    Toast.makeText(getApplicationContext(), coming_msg, Toast.LENGTH_LONG).show();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    public void delete(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(Edit_profileActivity.this, R.style.MyDialogTheme);
        builder.setTitle("Warning!!");
        builder.setMessage("This will suspend your account permanently");

        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new delete_account(Edit_profileActivity.this,username, user_type).execute();
                Toast.makeText(getApplicationContext(), "Account has been deleted Successfully", Toast.LENGTH_LONG).show();


                startActivity(new Intent(getApplicationContext(), MainActivity.class));



            }
        });


        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });


        AlertDialog diag = builder.create();

        diag.show();


    }

}
