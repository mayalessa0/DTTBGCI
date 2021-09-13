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

public class View_articleActivity extends AppCompatActivity {

    String article_id, username, user_type;
    TextView date_TV, title_TV, content_TV;
    Button edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_article);

        article_id = getIntent().getStringExtra("article id");

        date_TV = findViewById(R.id.date);
        title_TV = findViewById(R.id.title);
        content_TV = findViewById(R.id.content);
        edit = findViewById(R.id.button);

        global_variables globalVariable = (global_variables) getApplicationContext();
        username = globalVariable.get_username();
        user_type = globalVariable.get_usertype();

        if (user_type.equals("diabetic")){
            edit.setVisibility(View.INVISIBLE);
        }

        get_article_info obj = new get_article_info();
        obj.execute();
    }

    class get_article_info extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {

            String urlParams
                    = "a=" + article_id;

            connect c1 = new connect();
            String return_msg= c1.make_connect("get_article_info.php", urlParams);
            return return_msg;
        }

        @Override
        protected void onPostExecute(String responseBody)
        {
            String title, content, date;

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
                    title = jObject.getString("title");
                    title_TV.setText(title);
                    content = jObject.getString("content");
                    content_TV.setText(content);
                    date = jObject.getString("date");
                    date_TV.setText(date);

                }

                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void edit(View view) {

        Intent i = new Intent(getApplicationContext(), Update_articleActivity.class);
        i.putExtra("article id",article_id);
        startActivity(i);
    }
}
