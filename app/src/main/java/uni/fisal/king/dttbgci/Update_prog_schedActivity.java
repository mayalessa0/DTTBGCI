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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class Update_prog_schedActivity extends AppCompatActivity {

    Button edit_buttom;
    AutoCompleteTextView diabetic_id_ET, program_id_ET;
    EditText program_date_ET;
    String diabetic_id, program_id, program_date , schedule_id;
    ArrayList<String> program_list, diabetic_list, list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_prog_sched);

        schedule_id = getIntent().getStringExtra("schedule id");


        diabetic_id_ET= findViewById(R.id.EditText2);
        program_id_ET = findViewById(R.id.editText3);
        program_date_ET = findViewById(R.id.editText4);

        get_program_sched obj1 = new get_program_sched();
        obj1.execute();



    }




    class get_program_sched extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {

            String urlParams
                    = "a=" + schedule_id
                    + "&b="+ "program";

            connect c1 = new connect();
            String return_msg= c1.make_connect("get_schedule.php", urlParams);
            return return_msg;
        }

        @Override
        protected void onPostExecute(String responseBody)
        {
            String  program_id, diabetic_id, date;

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
                    program_id = jObject.getString("program_id");
                    program_id_ET.setText(program_id);
                    diabetic_id = jObject.getString("diabetic_id");
                    diabetic_id_ET.setText(diabetic_id);
                    date = jObject.getString("date");
                    program_date_ET.setText(date);



                    new get_all_diabetics(getApplicationContext()).execute();

                    global_variables globalVariable = (global_variables) getApplicationContext();

                    diabetic_list= globalVariable.get_list();
                    set_ArrayAdapter(diabetic_list, diabetic_id_ET);

                    get_all_programs obj2 = new get_all_programs();
                    obj2.execute();

                }

                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    public void set_ArrayAdapter (final ArrayList<String> list, final AutoCompleteTextView text){

        Collections.sort(list);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>
                (Update_prog_schedActivity.this, android.R.layout.simple_spinner_item, list);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        text.setThreshold(0);
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


    private class get_all_programs extends AsyncTask<Void,Void,String> {



        @Override
        protected String doInBackground(Void... params) {

            connect c1 =new connect();

            String return_msg = c1.make_connect("get_all_programs.php",null);
            return return_msg;
        }

        @Override
        protected void onPostExecute(String responseBody)
        {

            try {
                JSONObject json = new JSONObject(responseBody);

                JSONArray ja_data = json.getJSONArray("posts");
                final String[] str1 = new String[ja_data.length()];

                for (int i = 0; i < ja_data.length(); i++) {
                    JSONObject jObj = ja_data.getJSONObject(i);
                    String s = jObj.getString("post");
                    JSONObject jObject = new JSONObject(s);
                    String program_id = jObject.getString("program_id");
                    str1[i]= program_id;
                }

                program_list = new ArrayList<String>();

                for(int i=0;i<str1.length;i++)
                {
                    program_list.add(str1[i]);
                }
                list = program_list;
                set_ArrayAdapter(program_list, program_id_ET);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }





    public void update(View view) {

        diabetic_id = diabetic_id_ET.getText().toString().trim();
        program_id = program_id_ET.getText().toString().trim();
        program_date = program_date_ET.getText().toString().trim();


        if(diabetic_id.isEmpty() || program_id.isEmpty() || program_date.isEmpty()){

            Toast.makeText(getBaseContext(), "Please fill all fields", Toast.LENGTH_LONG).show();

        }
        else {

            if(diabetic_list.contains(diabetic_id) && list.contains(program_id)){
                update_schedule obj = new update_schedule();
                obj.execute();
            }

            else{

                Toast.makeText(getBaseContext(), "Diabetic ID or program ID are not correct", Toast.LENGTH_LONG).show();

            }

        }
    }


    private class update_schedule extends AsyncTask<Void,Void,String> {


        @Override
        protected String doInBackground(Void... params) {

            connect c1 =new connect();
            String urlParams =
                    "a="  + diabetic_id +
                    "&b=" + program_id +
                    "&c=" + program_date +
                    "&d=" + schedule_id;
            String return_msg = c1.make_connect("update_prog_sced.php",urlParams);
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
                Intent intent = new Intent(Update_prog_schedActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        }
    }


    public void delete_schedule(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(Update_prog_schedActivity.this, R.style.MyDialogTheme);
        builder.setTitle("Warning!!");
        builder.setMessage("This will delete this schedule permanently");

        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                new delete_schedule(Update_prog_schedActivity.this,schedule_id, "program").execute();
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
