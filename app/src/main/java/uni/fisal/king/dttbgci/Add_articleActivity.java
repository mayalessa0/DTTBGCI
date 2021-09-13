package uni.fisal.king.dttbgci;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Add_articleActivity extends AppCompatActivity {

    EditText date, title, content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_article);

        date = findViewById(R.id.date);
        title = findViewById(R.id.title);
        content = findViewById(R.id.content);
    }

    public void save(View view) {

        String article_date, article_title, article_content;

        article_date = date.getText().toString().trim();
        article_title = title.getText().toString().trim();
        article_content = content.getText().toString().trim();




        if(article_date.isEmpty() || article_title.isEmpty() || article_content.isEmpty() ){

            Toast.makeText(getBaseContext(), "Please fills all fields", Toast.LENGTH_LONG).show();

        }
        else {
            Save_article obj = new Save_article();
            obj.execute(article_date, article_title, article_content);

        }
    }


    private class Save_article extends AsyncTask<String, String, String> {



        @Override
        protected String doInBackground(String... params) {

            connect c1 =new connect();
            String urlParams = "a=" + params[0] + "&b=" + params[1] + "&c=" + params[2] ;

            String return_msg = c1.make_connect("save_article.php",urlParams);
            return return_msg;
        }

        @Override
        protected void onPostExecute(String responseBody)
        {

            if(responseBody.isEmpty()){
                Toast.makeText(getBaseContext(), "Failed to add article", Toast.LENGTH_LONG).show();

            }
            else{
                Toast.makeText(getBaseContext(), responseBody, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Add_articleActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        }
    }


}
