package uni.fisal.king.dttbgci;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static uni.fisal.king.dttbgci.Constants.FIRST_COLUMN;
import static uni.fisal.king.dttbgci.Constants.SECOND_COLUMN;
import static uni.fisal.king.dttbgci.Constants.THIRD_COLUMN;

public class List_of_schedulesActivity extends AppCompatActivity {

    ListView listView;
    private ArrayList<HashMap<String, String>> list;
    String schedules;
    TextView empty_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_schedules);

        schedules = getIntent().getStringExtra("schedules");

        listView = findViewById(R.id.listView4);
        empty_list = findViewById(R.id.textView34);

        populateList();

        final ListViewAdapters adapter = new ListViewAdapters(List_of_schedulesActivity.this, list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub

                HashMap<String ,String> hashmap= list.get(position);
                final String ID = hashmap.get(FIRST_COLUMN);
                final String type = hashmap.get(SECOND_COLUMN);

                switch (type){

                    case "Appointment":
                        Intent i = new Intent(getApplicationContext(), View_Appt_Activity.class);
                        i.putExtra("schedule id",ID);
                        startActivity(i);
                        break;

                    case "Medication":
                        i = new Intent(getApplicationContext(), View_medicationActivity.class);
                        i.putExtra("schedule id",ID);
                        startActivity(i);
                        break;

                    case "Program":
                        i = new Intent(getApplicationContext(), View_pgrm_sched_Activity.class);
                        i.putExtra("schedule id",ID);
                        startActivity(i);
                        break;

                    case "Sugar Level":
                        i = new Intent(getApplicationContext(), View_sugarLevelActivity.class);
                        i.putExtra("schedule id",ID);
                        startActivity(i);
                        break;
                }


            }
        });
    }


    // This is private function that used internally to fill the ListView.
    private void populateList() {

        list = new ArrayList<>();
        String type, date, ID;

        try {
            JSONObject json = new JSONObject(schedules);
            JSONArray jArray = json.getJSONArray("posts");
            int length = jArray.length();

            if (length == 0){
                empty_list.setText("Unfortunately, There is no schedule ! !");
            }

            else {
                HashMap temp = new HashMap();
                temp.put(FIRST_COLUMN, "Schedule ID");
                temp.put(SECOND_COLUMN, "Schedule Type");
                temp.put(THIRD_COLUMN, "Date");

                list.add(temp);

                //loop to get all json objects from data json array
                for (int i = 0; i < length; i++) {
                    HashMap temp2 = new HashMap();
                    JSONObject e = jArray.getJSONObject(i);
                    String s = e.getString("post");
                    JSONObject jObject = new JSONObject(s);
                    ID = jObject.getString("schedule_id");
                    temp2.put(FIRST_COLUMN, ID);
                    type = jObject.getString("schedule_type");
                    temp2.put(SECOND_COLUMN, type);
                    date = jObject.getString("date");
                    temp2.put(THIRD_COLUMN, date);
                    list.add(temp2);

                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
