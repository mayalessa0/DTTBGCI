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

public class View_sugarLevelActivity extends AppCompatActivity {

    String schedule_id, user_type;
    TextView  diabetic_idTV, dateTV, sugar_levelTV, average, error_msg;
    Button edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_sugar_level);

        schedule_id = getIntent().getStringExtra("schedule id");

        diabetic_idTV = findViewById(R.id.TextView2);
        dateTV = findViewById(R.id.TextView4);
        sugar_levelTV = findViewById(R.id.TextView3);
        average = findViewById(R.id.textView27);
        error_msg = findViewById(R.id.textView36);
        edit  = findViewById(R.id.button20);

        //disappeared update button when user is doctor.
        global_variables globalVariable = (global_variables) getApplicationContext();
        user_type = globalVariable.get_usertype();
        if(user_type.equals("doctor")){
            edit.setVisibility(View.INVISIBLE);
        }



        get_sched obj = new get_sched();
        obj.execute();

    }

    public void Calculate_average(View view) {
        //reset fields
        error_msg.setText("");
        average.setText("");
        get_average obj = new get_average();
        obj.execute();
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
                    diabetic_idTV.setText(diabetic_id);
                    date = jObject.getString("date");
                    dateTV.setText(date);
                    sugar_level = jObject.getString("sugar_level");
                    sugar_levelTV.setText(sugar_level);

                }

                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class get_average extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            connect c1 = new connect();
            String diabetic = diabetic_idTV.getText().toString().trim();

            String urlParams
                    = "a=" + diabetic;

            String return_msg = c1.make_connect("calculate_average.php", urlParams);


            return return_msg;
        }

        @Override
        protected void onPostExecute(String s) {
            if (!s.equals("")) { // means there is no error
                average.setText(s+" mmol/L");
            } else {

                error_msg.setText(getString(R.string.error_get_average));

            }


        }
    }

    public void edit(View view) {

        Intent i = new Intent(getApplicationContext(), Update_sugar_leveActivity.class);
        i.putExtra("schedule id",schedule_id);
        startActivity(i);
    }

}
