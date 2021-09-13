package uni.fisal.king.dttbgci;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class Update_appt_Activity extends AppCompatActivity {

    AutoCompleteTextView text;
    EditText ET_doctor_name, appt_date, appt_time;
    String schedule_id, username, diabetic_id, pro_date, pro_time;
    ArrayList<String>  diabetic_list;
    global_variables globalVariable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_appt);

        schedule_id = getIntent().getStringExtra("schedule id");


        text = findViewById(R.id.autoCompleteTextView3);
        ET_doctor_name = findViewById(R.id.EditText2);
        appt_date = findViewById(R.id.editText);
        appt_time = findViewById(R.id.editText5);


        new get_all_diabetics(getApplicationContext()).execute();

        globalVariable = (global_variables) getApplicationContext();
        username = globalVariable.get_username();

        ET_doctor_name.setText(username);
        ET_doctor_name.setFocusable(false);


        get_Appt_sched obj = new get_Appt_sched();
        obj.execute();
    }

    @SuppressLint("ClickableViewAccessibility")
    public void set_ArrayAdapter ( final ArrayList<String> list){

        Collections.sort(list);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>
                (Update_appt_Activity.this, android.R.layout.simple_spinner_item, list);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        text.setThreshold(1);
        text.setAdapter(dataAdapter);

        //if user press on AutoCompleteTextView the dropdown will shown
        text.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    text.showDropDown();
                }

                return false;
            }
        });


    }


    class get_Appt_sched extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {

            String urlParams
                    = "a=" + schedule_id
                    + "&b="+ "appointment";

            connect c1 = new connect();
            String return_msg= c1.make_connect("get_schedule.php", urlParams);
            return return_msg;
        }

        @Override
        protected void onPostExecute(String responseBody)
        {
            String diabetic_id, doctor_id, date, time, status;

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
                    text.setText(diabetic_id);
                    date = jObject.getString("date");
                    appt_date.setText(date);
                    time = jObject.getString("time");
                    appt_time.setText(time);

                    diabetic_list = globalVariable.get_list();
                    set_ArrayAdapter(diabetic_list);

                }

                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public void update(View view) {

        diabetic_id = text.getText().toString().trim();
        pro_date = appt_date.getText().toString().trim();
        pro_time = appt_time.getText().toString().trim();



        if(diabetic_id.isEmpty() || pro_date.isEmpty() || pro_time.isEmpty()){

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
                    "&b=" + username +
                    "&c=" + pro_date +
                    "&d=" + pro_time +
                    "&e=" + schedule_id;
            String return_msg = c1.make_connect("update_appt.php",urlParams);
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
                Intent intent = new Intent(Update_appt_Activity.this, HomeActivity.class);
                startActivity(intent);
            }
        }
    }

    public void delete_schedule(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(Update_appt_Activity.this, R.style.MyDialogTheme);
        builder.setTitle("Warning!!");
        builder.setMessage("This will delete this schedule permanently");

        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                new delete_schedule(Update_appt_Activity.this,schedule_id, "appointment").execute();
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
