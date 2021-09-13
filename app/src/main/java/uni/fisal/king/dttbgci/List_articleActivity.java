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

public class List_articleActivity extends AppCompatActivity {

    ListView listView;
    private ArrayList<HashMap<String, String>> list;
    String articles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_article);

        articles = getIntent().getStringExtra("articles");
        listView = findViewById(R.id.listView4);
        // call function populateList
        populateList();

        // after
        final ListViewAdapters adapter = new ListViewAdapters(List_articleActivity.this, list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub

                HashMap<String ,String> hashmap= list.get(position);
                final String ID = hashmap.get(FIRST_COLUMN);

                Intent i = new Intent(getApplicationContext(), View_articleActivity.class);
                i.putExtra("article id",ID);
                startActivity(i);

            }
        });
    }


    private void populateList() {

        list = new ArrayList<>();
        String type, description, ID;

        try {
            JSONObject json = new JSONObject(articles);
            JSONArray jArray = json.getJSONArray("posts");
            int length = jArray.length();

            //loop to get all json objects from data json array
            HashMap temp = new HashMap();
            temp.put(FIRST_COLUMN, "Article ID");
            temp.put(SECOND_COLUMN, "Title");
            temp.put(THIRD_COLUMN, "Date");

            list.add(temp);


            for (int i = 0; i < length; i++)
            {
                HashMap temp2 = new HashMap();
                JSONObject e = jArray.getJSONObject(i);
                String s = e.getString("post");
                JSONObject jObject = new JSONObject(s);
                ID = jObject.getString("article_id");
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
