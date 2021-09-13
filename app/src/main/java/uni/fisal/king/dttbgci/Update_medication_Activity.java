package uni.fisal.king.dttbgci;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class Update_medication_Activity extends AppCompatActivity {

    AutoCompleteTextView AC_diabetic_id;
    EditText ETmedication, ETprescription, ETmedication_date, ETmedication_time;
    String schedule_id, diabetic_id, medication, prescription, medication_date, medication_time;
    ArrayList<String> diabetic_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_medication);

        schedule_id = getIntent().getStringExtra("schedule id");

        AC_diabetic_id = findViewById(R.id.autoCompleteTextView4);
        ETmedication = findViewById(R.id.editText7);
        ETprescription = findViewById(R.id.editText4);
        ETmedication_date = findViewById(R.id.editText6);
        ETmedication_time = findViewById(R.id.editText8);



        get_sched obj = new get_sched();
        obj.execute();

    }

    class get_sched extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {

            String urlParams
                    = "a=" + schedule_id
                    + "&b="+ "medication";

            connect c1 = new connect();
            String return_msg= c1.make_connect("get_schedule.php", urlParams);
            return return_msg;
        }

        @Override
        protected void onPostExecute(String responseBody)
        {
            String diabetic_id, medication_name, prescription, date, time, status;

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
                    diabetic_id = jObject.getString("diabetic_id");
                    AC_diabetic_id.setText(diabetic_id);
                    medication_name = jObject.getString("medication_name");
                    ETmedication.setText(medication_name);
                    prescription = jObject.getString("prescription");
                    ETprescription.setText(prescription);
                    date = jObject.getString("date");
                    ETmedication_date.setText(date);
                    time = jObject.getString("time");
                    ETmedication_time.setText(time);


                    new get_all_diabetics(getApplicationContext()).execute();
                    global_variables globalVariable = (global_variables) getApplicationContext();
                    diabetic_list = globalVariable.get_list();
                    set_ArrayAdapter(diabetic_list);

                }

                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void set_ArrayAdapter ( final ArrayList<String> list){

        Collections.sort(list);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>
                (Update_medication_Activity.this, android.R.layout.simple_spinner_item, list);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        AC_diabetic_id.setThreshold(1);
        AC_diabetic_id.setAdapter(dataAdapter);
    }

    public void update(View view) {
        diabetic_id = AC_diabetic_id.getText().toString().trim();
        medication = ETmedication.getText().toString().trim();
        prescription = ETprescription.getText().toString().trim();
        medication_date = ETmedication_date.getText().toString().trim();
        medication_time = ETmedication_time.getText().toString().trim();




        if(diabetic_id.isEmpty() || medication.isEmpty() || prescription.isEmpty()
                || medication_date.isEmpty() || medication_time.isEmpty()){

            Toast.makeText(getBaseContext(), "Please fill all fields", Toast.LENGTH_LONG).show();

        }
        else {

            if(diabetic_list.contains(diabetic_id)){
                update_schedule obj = new update_schedule();
                obj.execute();
            }

            else{

                Toast.makeText(getBaseContext(), "Diabetic ID is not correct", Toast.LENGTH_LONG).show();

            }

        }
    }


    private class update_schedule extends AsyncTask<Void,Void,String> {


        @Override
        protected String doInBackground(Void... params) {

            connect c1 =new connect();
            String urlParams = "a="  + diabetic_id +
                    "&b=" + medication +
                    "&c=" + prescription +
                    "&d=" + medication_date +
                    "&e=" + medication_time+
                    "&f=" + schedule_id;
            String return_msg = c1.make_connect("update_medication.php",urlParams);
            return return_msg;
        }

        @Override
        protected void onPostExecute(String responseBody)
        {

            if(responseBody.isEmpty()){
                Toast.makeText(getBaseContext(), "Failed", Toast.LENGTH_LONG).show();

            }
            else{
                Toast.makeText(getBaseContext(), responseBody, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Update_medication_Activity.this, HomeActivity.class);
                startActivity(intent);
            }
        }
    }


    public void delete_schedule(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(Update_medication_Activity.this, R.style.MyDialogTheme);
        builder.setTitle("Warning!!");
        builder.setMessage("This will delete this schedule permanently");

        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                new delete_schedule(Update_medication_Activity.this,schedule_id, "medication").execute();
                Toast.makeText(getApplicationContext(), "Schedule has been deleted Successfully", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));

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
