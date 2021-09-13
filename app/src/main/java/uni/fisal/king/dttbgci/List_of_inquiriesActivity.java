package uni.fisal.king.dttbgci;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class List_of_inquiriesActivity extends AppCompatActivity {

    ListView listView;
    private ArrayList<HashMap<String, String>> list;
    String inquiries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_inquiries);

        inquiries = getIntent().getStringExtra("inquiries");
        listView = findViewById(R.id.listView4);
        populateList();

        final ListViewAdapters adapter = new ListViewAdapters(List_of_inquiriesActivity.this, list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub

                HashMap<String ,String> hashmap= list.get(position);
                final String ID = hashmap.get(FIRST_COLUMN);

                Intent i = new Intent(getApplicationContext(), View_inquiryActivity.class);
                i.putExtra("inquiry id",ID);
                startActivity(i);

            }
        });
    }


    private void populateList() {

        list = new ArrayList<>();
        String type, description, ID;

        try {
            JSONObject json = new JSONObject(inquiries);
            JSONArray jArray = json.getJSONArray("posts");
            int length = jArray.length();

            HashMap temp = new HashMap();
            temp.put(FIRST_COLUMN, "Inquiry ID");
            temp.put(SECOND_COLUMN, "Title");
            temp.put(THIRD_COLUMN, "Date");

            list.add(temp);


            for (int i = 0; i < length; i++)
            {
                HashMap temp2 = new HashMap();
                JSONObject e = jArray.getJSONObject(i);
                String s = e.getString("post");
                JSONObject jObject = new JSONObject(s);
                ID = jObject.getString("inquiry_id");
                temp2.put(FIRST_COLUMN, ID);
                type = jObject.getString("title");
                temp2.put(SECOND_COLUMN, type);
                description = jObject.getString("date");
                temp2.put(THIRD_COLUMN, description);
                list.add(temp2);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
