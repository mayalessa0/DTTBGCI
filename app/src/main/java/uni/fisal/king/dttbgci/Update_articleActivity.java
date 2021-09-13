package uni.fisal.king.dttbgci;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Update_articleActivity extends AppCompatActivity {

    EditText date_ET, title_ET, content_ET;
    String article_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_article);

        article_id = getIntent().getStringExtra("article id");


        date_ET = findViewById(R.id.date);
        title_ET = findViewById(R.id.title);
        content_ET = findViewById(R.id.content);

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
                    title_ET.setText(title);
                    content = jObject.getString("content");
                    content_ET.setText(content);
                    date = jObject.getString("date");
                    date_ET.setText(date);

                }

                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void save(View view) {

        String article_date, article_title, article_content;

        article_date = date_ET.getText().toString().trim();
        article_title = title_ET.getText().toString().trim();
        article_content = content_ET.getText().toString().trim();




        if(article_date.isEmpty() || article_title.isEmpty() || article_content.isEmpty() ){

            Toast.makeText(getBaseContext(), "Please fills all fields", Toast.LENGTH_LONG).show();

        }
        else {
            update_article obj = new update_article();
            obj.execute(article_date, article_title, article_content);

        }
    }

    private class update_article extends AsyncTask<String, String, String> {



        @Override
        protected String doInBackground(String... params) {

            connect c1 =new connect();
            String urlParams = "a=" + params[0] + "&b=" + params[1] + "&c=" + params[2] + "&d=" + article_id;

            String return_msg = c1.make_connect("update_article.php",urlParams);
            return return_msg;
        }

        @Override
        protected void onPostExecute(String responseBody)
        {

            if(responseBody.isEmpty()){
                Toast.makeText(getBaseContext(), "Failed to update article", Toast.LENGTH_LONG).show();

            }
            else{
                Toast.makeText(getBaseContext(), responseBody, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Update_articleActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        }
    }

    public void delete(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(Update_articleActivity.this, R.style.MyDialogTheme);
        builder.setTitle("Warning!!");
        builder.setMessage("This will delete this article permanently");

        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                delete_article obj = new delete_article();
                obj.execute();
                Toast.makeText(getApplicationContext(), "article has been deleted Successfully", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));

            }
        });


        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });


        AlertDialog diag = builder.create();

        diag.show();
    }

    public class delete_article extends AsyncTask<Void,Void,String> {


        @Override
        protected String doInBackground(Void... params) {

            connect c1 =new connect();
            String urlParams
                    ="a=" + article_id;

            String return_msg = c1.make_connect("delete_article.php",urlParams);
            return return_msg;
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            if(!s.isEmpty()) { // means there is no error

                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }


        }
    }


}
