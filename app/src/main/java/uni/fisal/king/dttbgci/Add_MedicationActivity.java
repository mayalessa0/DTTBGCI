package uni.fisal.king.dttbgci;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class Add_MedicationActivity extends AppCompatActivity {

    AutoCompleteTextView text;
    EditText ETmedication, ETprescription, ETmedication_date, ETmedication_time;
    String diabetic_id, medication, prescription, medication_date, medication_time;
    ArrayList<String> diabetic_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medication);

        text = findViewById(R.id.autoCompleteTextView4);
        ETmedication = findViewById(R.id.editText7);
        ETprescription = findViewById(R.id.editText4);
        ETmedication_date = findViewById(R.id.editText6);
        ETmedication_time = findViewById(R.id.editText8);



        new get_all_diabetics(getApplicationContext()).execute();

        global_variables globalVariable = (global_variables) getApplicationContext();


        diabetic_list = globalVariable.get_list();
        set_ArrayAdapter(diabetic_list);
    }

    public void set_ArrayAdapter ( final ArrayList<String> list){

        Collections.sort(list);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>
                (Add_MedicationActivity.this, android.R.layout.simple_spinner_item, list);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        text.setThreshold(1);
        text.setAdapter(dataAdapter);
    }

    public void Save(View view) {

        diabetic_id = text.getText().toString().trim();
        medication = ETmedication.getText().toString().trim();
        prescription = ETprescription.getText().toString().trim();
        medication_date = ETmedication_date.getText().toString().trim();
        medication_time = ETmedication_time.getText().toString().trim();



        if(diabetic_id.isEmpty() || medication.isEmpty() || prescription.isEmpty() ||
                medication_date.isEmpty() || medication_time.isEmpty() ){

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


    private class Save_schedule extends AsyncTask<Void,Void,String> {



        @Override
        protected String doInBackground(Void... params) {

            connect c1 =new connect();
            String urlParams =
                    "a=" + diabetic_id +
                    "&b=" + medication +
                    "&c=" + prescription +
                    "&d=" + medication_date +
                    "&e=" + medication_time;

            String return_msg = c1.make_connect("save_medication_schedule.php",urlParams);
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
                Intent intent = new Intent(Add_MedicationActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        }
    }


}
