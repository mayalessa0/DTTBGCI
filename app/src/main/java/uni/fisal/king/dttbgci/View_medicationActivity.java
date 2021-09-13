package uni.fisal.king.dttbgci;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class View_medicationActivity extends AppCompatActivity {

    String schedule_id;
    Button edit_buttom;
    TextView diabetic_idTV, medication_nameTV, prescriptionTV, dateTV, timeTV, statusTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_medication);

        schedule_id = getIntent().getStringExtra("schedule id");

        diabetic_idTV = findViewById(R.id.TextView4);
        medication_nameTV = findViewById(R.id.TextView7);
        prescriptionTV = findViewById(R.id.TextView10);
        dateTV = findViewById(R.id.TextView16);
        timeTV = findViewById(R.id.TextView8);
        statusTV = findViewById(R.id.TextView9);

        get_sched obj = new get_sched();
        obj.execute();

        edit_buttom = findViewById(R.id.button19);

        global_variables globalVariable = (global_variables) getApplicationContext();

        if(globalVariable.get_usertype().equals("diabetic")){
            edit_buttom.setVisibility((View.INVISIBLE));
        }
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
                    diabetic_idTV.setText(diabetic_id);
                    medication_name = jObject.getString("medication_name");
                    medication_nameTV.setText(medication_name);
                    prescription = jObject.getString("prescription");
                    prescriptionTV.setText(prescription);
                    date = jObject.getString("date");
                    dateTV.setText(date);
                    time = jObject.getString("time");
                    timeTV.setText(time);
                    status = jObject.getString("status");
                    statusTV.setText(status);

                }

                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void edit(View view) {

        Intent i = new Intent(getApplicationContext(), Update_medication_Activity.class);
        i.putExtra("schedule id",schedule_id);
        startActivity(i);
    }

}
