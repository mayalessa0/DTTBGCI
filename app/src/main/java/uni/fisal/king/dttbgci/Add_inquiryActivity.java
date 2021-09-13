package uni.fisal.king.dttbgci;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Add_inquiryActivity extends AppCompatActivity {

    EditText date, title, content;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_inquiry);


        date = findViewById(R.id.date);
        title = findViewById(R.id.title);
        content = findViewById(R.id.content);


        global_variables globalVariable = (global_variables) getApplicationContext();
        username = globalVariable.get_username();
    }

    public void save(View view) {

        String inquiry_date, inquiry_title, inquiry_content;

        inquiry_date = date.getText().toString().trim();
        inquiry_title = title.getText().toString().trim();
        inquiry_content = content.getText().toString().trim();




        if(inquiry_date.isEmpty() || inquiry_title.isEmpty() || inquiry_content.isEmpty() ){

            Toast.makeText(getBaseContext(), "Please fills all fields", Toast.LENGTH_LONG).show();

        }
        else {
            Save_inquiry obj = new Save_inquiry();
            obj.execute(inquiry_date, inquiry_title, inquiry_content);

        }
    }


    private class Save_inquiry extends AsyncTask<String, String, String> {



        @Override
        protected String doInBackground(String... params) {

            connect c1 =new connect();
            String urlParams = "a=" + params[0] + "&b=" + params[1] + "&c=" + params[2] +"&d=" + username ;

            String return_msg = c1.make_connect("save_inquiry.php",urlParams);
            return return_msg;
        }

        @Override
        protected void onPostExecute(String responseBody)
        {

            if(responseBody.isEmpty()){
                Toast.makeText(getBaseContext(), "Failed to add inquiry", Toast.LENGTH_LONG).show();

            }
            else{
                Toast.makeText(getBaseContext(), responseBody, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Add_inquiryActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        }
    }


}
