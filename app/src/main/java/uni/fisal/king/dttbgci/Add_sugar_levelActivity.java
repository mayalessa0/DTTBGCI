package uni.fisal.king.dttbgci;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Add_sugar_levelActivity extends AppCompatActivity {
    AutoCompleteTextView text;
    EditText ET_sugar_level, ET_date;
    String username, user_type, diabetic_id, sugar_level, s_date;
    ArrayList<String> alist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sugar_level);

        text = findViewById(R.id.autoCompleteTextView2);
        ET_sugar_level = findViewById(R.id.editText3);
        ET_date = findViewById(R.id.editText2);


        global_variables globalVariable = (global_variables) getApplicationContext();
        username = globalVariable.get_username();
        user_type = globalVariable.get_usertype();

        if(user_type.equals("diabetic")){
            text.setText(username);
            text.setFocusable(false);
        }
        else{

            new get_all_diabetics(getApplicationContext()).execute();
            alist = globalVariable.get_list();
            set_ArrayAdapter(alist);


        }

    }

    public void set_ArrayAdapter ( final List<String> list){

        if(list.isEmpty()){ // means there is an error
            Toast.makeText(getApplicationContext(), "failed response from database!!", Toast.LENGTH_LONG).show();
        }
        else {


            Collections.sort(list);

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                    (getApplicationContext(), android.R.layout.simple_spinner_item, list);

            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            text.setThreshold(1);
            text.setAdapter(dataAdapter);


        }

    }
    public void Save(View view) {

        diabetic_id = text.getText().toString().trim();
        sugar_level = ET_sugar_level.getText().toString().trim();
        s_date = ET_date.getText().toString().trim();




        if(diabetic_id.isEmpty() || sugar_level.isEmpty() || s_date.isEmpty() ){

            Toast.makeText(getBaseContext(), "Please fills all fields", Toast.LENGTH_LONG).show();

        }
        else {
            Save_schedule obj = new Save_schedule();
            obj.execute();

        }
    }


    private class Save_schedule extends AsyncTask<Void,Void,String> {



        @Override
        protected String doInBackground(Void... params) {

            connect c1 =new connect();
            String urlParams =
                    "a=" + diabetic_id +
                    "&b=" + sugar_level +
                    "&c=" + s_date ;

            String return_msg = c1.make_connect("save_sugar_level.php",urlParams);
            return return_msg;
        }

        @Override
        protected void onPostExecute(String responseBody)
        {

            if(responseBody.isEmpty()){
                Toast.makeText(getBaseContext(), "Failed", Toast.LENGTH_LONG).show();

            }
            else{
                Toast.makeText(getBaseContext(), responseBody, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Add_sugar_levelActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        }
    }


}
