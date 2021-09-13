package uni.fisal.king.dttbgci;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Add_AppointmentActivity extends AppCompatActivity {

    AutoCompleteTextView text;
    EditText ET_doctor_name, program_date, program_time;
    String username, diabetic_id, pro_date, pro_time;
    ArrayList<String>  diabetic_list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_appointment);

        text = findViewById(R.id.autoCompleteTextView3);
        ET_doctor_name = findViewById(R.id.EditText2);
        program_date = findViewById(R.id.editText);
        program_time = findViewById(R.id.editText5);


        new get_all_diabetics(getApplicationContext()).execute();

        global_variables globalVariable = (global_variables) getApplicationContext();
        username = globalVariable.get_username();

        ET_doctor_name.setText(username);
        ET_doctor_name.setFocusable(false);

        diabetic_list = globalVariable.get_list();
        set_ArrayAdapter(diabetic_list);
    }

    public void set_ArrayAdapter ( final ArrayList<String> list){

        Collections.sort(list);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>
                (Add_AppointmentActivity.this, android.R.layout.simple_spinner_item, list);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        text.setThreshold(1);
        text.setAdapter(dataAdapter);




    }

    private class Save_schedule extends AsyncTask<Void,Void,String> {



        @Override
        protected String doInBackground(Void... params) {

            connect c1 =new connect();
            String urlParams =
                    "a=" + diabetic_id +
                    "&b=" + username +
                    "&c=" + pro_date +
                    "&d=" + pro_time;

            String return_msg = c1.make_connect("save_appointment_schedule.php",urlParams);
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
                Intent intent = new Intent(Add_AppointmentActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        }
    }

    public void Save(View view) {

        diabetic_id = text.getText().toString().trim();
        pro_date = program_date.getText().toString().trim();
        pro_time = program_time.getText().toString().trim();


        if(diabetic_id.isEmpty() || pro_date.isEmpty() || pro_time.isEmpty() ){

            Toast.makeText(getBaseContext(), "Please fills all fields", Toast.LENGTH_LONG).show();

        }
        else {

            if(diabetic_list.contains(diabetic_id)){
                Save_schedule obj = new Save_schedule();
                obj.execute();
            }

            else{

                Toast.makeText(getBaseContext(), "Diabetic ID is not correct", Toast.LENGTH_LONG).show();

            }

        }
    }

}
