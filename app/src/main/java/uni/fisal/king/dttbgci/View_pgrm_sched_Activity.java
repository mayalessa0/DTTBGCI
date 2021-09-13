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

public class View_pgrm_sched_Activity extends AppCompatActivity {

    String schedule_id;
    Button edit_buttom;
    TextView  program_idTV, diabetic_idTV, dateTV, statusTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pgrm_sched);

        schedule_id = getIntent().getStringExtra("schedule id");

        program_idTV = findViewById(R.id.TextView3);
        diabetic_idTV = findViewById(R.id.TextView2);
        dateTV = findViewById(R.id.TextView5);
        statusTV = findViewById(R.id.textView31);

        get_program_sched obj = new get_program_sched();
        obj.execute();

        // disappeared edit button if user type is diabetic
        edit_buttom = findViewById(R.id.button13);

        global_variables globalVariable = (global_variables) getApplicationContext();

        if(globalVariable.get_usertype().equals("diabetic")){
            edit_buttom.setVisibility((View.INVISIBLE));
        }
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
            String  program_id, diabetic_id, date, status;

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
                    program_idTV.setText(program_id);
                    diabetic_id = jObject.getString("diabetic_id");
                    diabetic_idTV.setText(diabetic_id);
                    date = jObject.getString("date");
                    dateTV.setText(date);
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

        Intent i = new Intent(getApplicationContext(), Update_prog_schedActivity.class);
        i.putExtra("schedule id",schedule_id);
        startActivity(i);
    }

}
