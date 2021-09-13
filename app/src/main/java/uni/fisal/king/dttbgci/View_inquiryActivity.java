package uni.fisal.king.dttbgci;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class View_inquiryActivity extends AppCompatActivity {

    String inquiry_id, username, user_type, writer_id;
    TextView date_TV, title_TV, content_TV;
    Button delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_inquiry);

        inquiry_id = getIntent().getStringExtra("inquiry id");

        date_TV = findViewById(R.id.date);
        title_TV = findViewById(R.id.title);
        content_TV = findViewById(R.id.content);
        delete = findViewById(R.id.button);

        global_variables globalVariable = (global_variables) getApplicationContext();
        username = globalVariable.get_username();
        user_type = globalVariable.get_usertype();

        if (user_type.equals("doctor")){
            delete.setVisibility(View.INVISIBLE);
        }

        get_inquiry_info obj = new get_inquiry_info();
        obj.execute();
    }




    class get_inquiry_info extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {

            String urlParams
                    = "a=" + inquiry_id;

            connect c1 = new connect();
            String return_msg= c1.make_connect("get_inquiry_info.php", urlParams);
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
                    writer_id = jObject.getString("writer_id");

                }

                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void view_comment(View view) {

        get_comments obj = new get_comments();
        obj.execute();
    }

    public void add_comment(View view) {

        /*Intent i = new Intent(getApplicationContext(), Add_commentActivity.class);
        i.putExtra("inquiry id",inquiry_id);
        startActivity(i);*/
        AlertDialog.Builder alert = new AlertDialog.Builder(this, R.style.MyDialogTheme);
        final EditText edittext = new EditText(getApplicationContext());
        alert.setTitle("Add your Comment");
        alert.setView(edittext);

        alert.setPositiveButton("Publish", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                String text = edittext.getText().toString();
                if (text.isEmpty()){
                    Toast.makeText(getApplicationContext(), "You should add comment before press publish button", Toast.LENGTH_LONG).show();
                }
                else {
                    save_comment obj = new save_comment();
                    obj.execute(text);
                }
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });

        alert.show();
    }


    public void delete(View view) {

        if (writer_id.equals(username)) {

            AlertDialog.Builder builder = new AlertDialog.Builder(View_inquiryActivity.this, R.style.MyDialogTheme);
            builder.setTitle("Warning!!");
            builder.setMessage("This will delete this inquiry permanently");

            builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    delete_inquiry obj = new delete_inquiry();
                    obj.execute();

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
        else{
            Toast.makeText(getApplicationContext(), "cannot delete this inquiry! you should be the writer of this one", Toast.LENGTH_LONG).show();
        }
    }



    public class delete_inquiry extends AsyncTask<Void,Void,String> {


        @Override
        protected String doInBackground(Void... params) {

            connect c1 =new connect();
            String urlParams
                    ="a=" + inquiry_id;

            String return_msg = c1.make_connect("delete_inquiry.php",urlParams);
            return return_msg;
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            if(!s.isEmpty()) { // means there is no error

                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(getApplicationContext(), "Inquiry has been deleted Successfully", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            }


        }
    }


    class get_comments extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {

            connect c1 = new connect();
            String urlParams = "a=" + inquiry_id;

            String return_msg= c1.make_connect("list_of_comments.php", urlParams);
            return return_msg;
        }

        @Override
        protected void onPostExecute(String responseBody)
        {
            if(responseBody.equals("")){ // means there is an error
                Toast.makeText(getApplicationContext(), "Failed from Database side", Toast.LENGTH_LONG).show();
            }
            else
            {
                Intent intent = new Intent(getApplicationContext(), View_commentActivity.class);
                intent.putExtra("comments" , responseBody);
                startActivity(intent);

            }
        }
    }


    public class save_comment extends AsyncTask<String,Void,String> {


        @Override
        protected String doInBackground(String... params) {

            connect c1 =new connect();
            String urlParams
                    ="a=" + inquiry_id
                    +"&b=" + username
                    +"&c=" + user_type
                    +"&d=" + params[0];

            String return_msg = c1.make_connect("save_comment.php",urlParams);
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
