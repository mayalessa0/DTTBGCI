package uni.fisal.king.dttbgci;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static uni.fisal.king.dttbgci.Constants.FIRST_COLUMN;
import static uni.fisal.king.dttbgci.Constants.SECOND_COLUMN;
import static uni.fisal.king.dttbgci.Constants.THIRD_COLUMN;

public class View_commentActivity extends AppCompatActivity {

    ListView listView;
    private ArrayList<HashMap<String, String>> list;
    String Comments, username, user_type;
    TextView error_msg;
    ListViewAdapters adapter;
    int position_at_list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_comment);


        global_variables globalVariable = (global_variables) getApplicationContext();
        username = globalVariable.get_username();
        user_type = globalVariable.get_usertype();



        Comments = getIntent().getStringExtra("comments");
        listView = findViewById(R.id.listView4);
        error_msg = findViewById(R.id.textView37);

        populateList();

        adapter = new ListViewAdapters(View_commentActivity.this, list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                // TODO Auto-generated method stub

                HashMap<String ,String> hashmap= list.get(position);
                final String ID = hashmap.get(FIRST_COLUMN);
                final String writer = hashmap.get(SECOND_COLUMN);

                if (username.equals(writer)) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(View_commentActivity.this, R.style.MyDialogTheme);
                    builder.setTitle("Warning!!");
                    builder.setMessage("This will delete this comment permanently");

                    builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            delete_comment obj = new delete_comment();
                            obj.execute(ID);
                            position_at_list = position;

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

            }
        });
    }


    private void populateList() {

        list = new ArrayList<>();
        String type, description, ID;

        try {
            JSONObject json = new JSONObject(Comments);

            JSONArray jArray = json.getJSONArray("posts");
            int length = jArray.length();

            HashMap temp = new HashMap();
            temp.put(FIRST_COLUMN, "Comment ID");
            temp.put(SECOND_COLUMN, "Writer");
            temp.put(THIRD_COLUMN, "Comment");

            list.add(temp);


            for (int i = 0; i < length; i++) {
                HashMap temp2 = new HashMap();
                JSONObject e = jArray.getJSONObject(i);
                String s = e.getString("post");
                JSONObject jObject = new JSONObject(s);
                ID = jObject.getString("comment_id");
                temp2.put(FIRST_COLUMN, ID);
                type = jObject.getString("writer");
                temp2.put(SECOND_COLUMN, type);
                description = jObject.getString("comment");
                temp2.put(THIRD_COLUMN, description);
                list.add(temp2);

            }


        } catch (JSONException e) {
            error_msg.setText("There is no comment under this inquiry !!");
            e.printStackTrace();
        }

    }


    public class delete_comment extends AsyncTask<String,String,String> {


        @Override
        protected String doInBackground(String... params) {

            connect c1 =new connect();
            String urlParams
                    ="a=" + params[0]
                    +"&b="+ user_type;

            String return_msg = c1.make_connect("delete_comment.php",urlParams);
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

                Toast.makeText(getApplicationContext(), "Comment has been deleted Successfully", Toast.LENGTH_LONG).show();
                list.remove(position_at_list);
                adapter.notifyDataSetChanged();            }


        }
    }

}
