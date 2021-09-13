package uni.fisal.king.dttbgci;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static uni.fisal.king.dttbgci.Constants.FIRST_COLUMN;
import static uni.fisal.king.dttbgci.Constants.SECOND_COLUMN;
import static uni.fisal.king.dttbgci.Constants.THIRD_COLUMN;

public class List_of_programsActivity extends AppCompatActivity {

    ListView listView;
    private ArrayList<HashMap<String, String>> list;
    String programs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_programs);


        programs = getIntent().getStringExtra("programs");
        listView = findViewById(R.id.listView4);
        // call function populateList
        populateList();

        // after
        final ListViewAdapters adapter = new ListViewAdapters(List_of_programsActivity.this, list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub

                HashMap<String ,String> hashmap= list.get(position);
                final String ID = hashmap.get(FIRST_COLUMN);

                Intent i = new Intent(getApplicationContext(), ViewProgramActivity.class);
                i.putExtra("program_id",ID);
                startActivity(i);

            }
        });
    }


    // This is private function that used internally to fill the ListView.
    private void populateList() {

        list = new ArrayList<>();
        Log.d("programs",programs);
        String type, description, ID;

        try {
            JSONObject json = new JSONObject(programs);
            JSONArray jArray = json.getJSONArray("posts");
            int length = jArray.length();

            //loop to get all json objects from data json array
            HashMap temp = new HashMap();
            temp.put(FIRST_COLUMN, "Program ID");
            temp.put(SECOND_COLUMN, "Diet type");
            temp.put(THIRD_COLUMN, "Description");

            list.add(temp);


            for (int i = 0; i < length; i++)
            {
                HashMap temp2 = new HashMap();
                JSONObject e = jArray.getJSONObject(i);
                String s = e.getString("post");
                JSONObject jObject = new JSONObject(s);
                ID = jObject.getString("program_id");
                temp2.put(FIRST_COLUMN, ID);
                type = jObject.getString("type");
                temp2.put(SECOND_COLUMN, type);
                description = jObject.getString("description");
                temp2.put(THIRD_COLUMN, description);
                list.add(temp2);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
