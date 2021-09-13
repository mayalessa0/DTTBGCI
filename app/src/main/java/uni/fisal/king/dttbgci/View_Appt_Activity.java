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

public class View_Appt_Activity extends AppCompatActivity {

    String schedule_id;
    Button edit_buttom;
    TextView diabetic_idTV, doctor_idTV, dateTV, timeTV, statusTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_appt);

        schedule_id = getIntent().getStringExtra("schedule id");

        diabetic_idTV = findViewById(R.id.TextView3);
        doctor_idTV = findViewById(R.id.TextView2);
        dateTV = findViewById(R.id.TextView);
        timeTV = findViewById(R.id.TextView5);
        statusTV = findViewById(R.id.textView33);


        get_Appt_sched obj = new get_Appt_sched();
        obj.execute();

        // disappeared edit button if user type is diabetic
        edit_buttom = findViewById(R.id.button18);

        global_variables globalVariable = (global_variables) getApplicationContext();

        if(globalVariable.get_usertype().equals("diabetic")){
            edit_buttom.setVisibility((View.INVISIBLE));
        }

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
                    diabetic_idTV.setText(diabetic_id);
                    doctor_id = jObject.getString("doctor_id");
                    doctor_idTV.setText(doctor_id);
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

        Intent i = new Intent(getApplicationContext(), Update_appt_Activity.class);
        i.putExtra("schedule id",schedule_id);
        startActivity(i);
    }
}
