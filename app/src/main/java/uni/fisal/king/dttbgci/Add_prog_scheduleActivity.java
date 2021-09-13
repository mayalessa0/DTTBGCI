package uni.fisal.king.dttbgci;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Collections;

public class Add_prog_scheduleActivity extends AppCompatActivity {

    AutoCompleteTextView diabetic, program;
    EditText program_date;
    String diabetic_id, program_id, pro_date;
    ArrayList<String> program_list, diabetic_list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_prog_schedule);

        diabetic = findViewById(R.id.autoCompleteTextView2);
        program = findViewById(R.id.AutoCompleteTextView3);
        program_date = findViewById(R.id.editText2);

        new get_all_diabetics(getApplicationContext()).execute();

        global_variables globalVariable = (global_variables) getApplicationContext();

        diabetic_list= globalVariable.get_list();
        set_ArrayAdapter(diabetic_list, diabetic);

        get_all_programs obj = new get_all_programs();
        obj.execute();
    }

    @SuppressLint("ClickableViewAccessibility")
    public void set_ArrayAdapter (final ArrayList<String> list, final AutoCompleteTextView text){

        Collections.sort(list);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>
                (Add_prog_scheduleActivity.this, android.R.layout.simple_spinner_item, list);

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
                set_ArrayAdapter(program_list, program);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void Save(View view) {

        pro_date = program_date.getText().toString().trim();
        diabetic_id = diabetic.getText().toString().trim();
        program_id = program.getText().toString().trim();


        if(diabetic_id.isEmpty() || program_id.isEmpty() || pro_date.isEmpty() ){

            Toast.makeText(getBaseContext(), "Please fill all fields", Toast.LENGTH_LONG).show();

        }
        else {

            if(diabetic_list.contains(diabetic_id) && program_list.contains(program_id)){
                Save_schedule obj = new Save_schedule();
                obj.execute();
            }

            else{

                Toast.makeText(getBaseContext(), "Diabetic ID or program ID are not correct", Toast.LENGTH_LONG).show();

            }

        }
    }

    private class Save_schedule extends AsyncTask<Void,Void,String> {



        @Override
        protected String doInBackground(Void... params) {

            connect c1 =new connect();
            String urlParams =  "a=" + diabetic_id +
                                "&b=" + program_id +
                                "&c=" + pro_date;
            String return_msg = c1.make_connect("save_program_schedule.php",urlParams);
            return return_msg;
        }

        @Override
        protected void onPostExecute(String responseBody)
        {

            if(responseBody.isEmpty()){
                Toast.makeText(getBaseContext(), "Failed to save schedule", Toast.LENGTH_LONG).show();

            }
            else{
                Toast.makeText(getBaseContext(), responseBody, Toast.LENGTH_LONG).show();
                Send_notification obj = new Send_notification();
                obj.execute();
            }
        }
    }
    // send notification to user who has new program diet
    private class Send_notification extends AsyncTask<Void,Void,String> {



        @Override
        protected String doInBackground(Void... params) {

            connect c1 =new connect();
            String message = "There is a new program diet assigned for you";
            String title = "Program Diet";
            String urlParams =  "a=" + diabetic_id +
                    "&b=" + message +
                    "&c=" + title;
            String return_msg = c1.make_connect("fcm/send_program_notification.php",urlParams);
            return return_msg;
        }

        @Override
        protected void onPostExecute(String responseBody)
        {

            if(responseBody.isEmpty()){
                Toast.makeText(getBaseContext(), "Failed to notify the diabetic", Toast.LENGTH_LONG).show();

            }
            else{
                Toast.makeText(getBaseContext(), responseBody, Toast.LENGTH_LONG).show();
            }
            Intent intent = new Intent(Add_prog_scheduleActivity.this, HomeActivity.class);
            startActivity(intent);
        }
    }

}
