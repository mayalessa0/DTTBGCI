package uni.fisal.king.dttbgci;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.util.List;

public class Update_sugar_leveActivity extends AppCompatActivity {

    AutoCompleteTextView diabetic_idAC;
    EditText ET_sugar_level, ET_date;
    String schedule_id, username, user_type, diabetic_id, sugar_level, s_date;
    ArrayList<String> alist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_sugar_leve);

        schedule_id = getIntent().getStringExtra("schedule id");

        diabetic_idAC = findViewById(R.id.autoCompleteTextView2);
        ET_sugar_level = findViewById(R.id.editText3);
        ET_date = findViewById(R.id.editText2);


        global_variables globalVariable = (global_variables) getApplicationContext();
        username = globalVariable.get_username();
        user_type = globalVariable.get_usertype();

        get_sched obj = new get_sched();
        obj.execute();

        if(user_type.equals("diabetic")){
            diabetic_idAC.setText(username);
            diabetic_idAC.setFocusable(false);
        }
        else{

            new get_all_diabetics(getApplicationContext()).execute();
            alist = globalVariable.get_list();
            set_ArrayAdapter(alist);


        }
    }


    class get_sched extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {

            String urlParams
                    = "a=" + schedule_id
                    + "&b="+ "sugar_level";

            connect c1 = new connect();
            String return_msg= c1.make_connect("get_schedule.php", urlParams);
            return return_msg;
        }

        @Override
        protected void onPostExecute(String responseBody)
        {
            String diabetic_id, date, sugar_level;

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
                    diabetic_idAC.setText(diabetic_id);
                    date = jObject.getString("date");
                    ET_date.setText(date);
                    sugar_level = jObject.getString("sugar_level");
                    ET_sugar_level.setText(sugar_level);

                }

                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    public void set_ArrayAdapter ( final List<String> list){

        if(list.isEmpty()){ // means there is an error
            Toast.makeText(getApplicationContext(), "failed response from database!!", Toast.LENGTH_LONG).show();
        }
        else {


            Collections.sort(list);

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                    (getApplicationContext(), android.R.layout.simple_spinner_item, list);

            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            diabetic_idAC.setThreshold(1);
            diabetic_idAC.setAdapter(dataAdapter);

            //if user press on AutoCompleteTextView the dropdown will shown
            diabetic_idAC.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // TODO Auto-generated method stub
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        diabetic_idAC.showDropDown();
                    }

                    return false;
                }
            });


        }

    }
    public void update(View view) {
        diabetic_id = diabetic_idAC.getText().toString().trim();
        sugar_level = ET_sugar_level.getText().toString().trim();
        s_date = ET_date.getText().toString().trim();


        if(diabetic_id.isEmpty() || sugar_level.isEmpty() || s_date.isEmpty() ){

            Toast.makeText(getBaseContext(), "Please fill all fields", Toast.LENGTH_LONG).show();

        }
        else {


            if(alist.contains(diabetic_id) || user_type.equals("diabetic")){
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
                    "&b=" + sugar_level +
                    "&c=" + s_date +
                    "&d=" + schedule_id;
            String return_msg = c1.make_connect("update_sugar_level.php",urlParams);
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
                Intent intent = new Intent(Update_sugar_leveActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        }
    }

    public void delete_schedule(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(Update_sugar_leveActivity.this, R.style.MyDialogTheme);
        builder.setTitle("Warning!!");
        builder.setMessage("This will delete this schedule permanently");

        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                new delete_schedule(Update_sugar_leveActivity.this,schedule_id, "sugar").execute();
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
