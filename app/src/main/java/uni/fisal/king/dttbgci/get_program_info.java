package uni.fisal.king.dttbgci;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class get_program_info extends AsyncTask<Void,Void,String> {

    Context c;
    String ID;
    Spinner spinner_type;
    TextView  TV_description, TV_type;



    public get_program_info(Context c, String ID, Spinner spinner_type, TextView d) {
        this.c = c;
        this.ID = ID;
        this.spinner_type = spinner_type;
        this.TV_description = d;

    }

    public get_program_info(Context c, String ID, TextView TV_type, TextView d) {
        this.c = c;
        this.ID = ID;
        this.TV_type = TV_type;
        this.TV_description = d;

    }
    @Override
    protected String doInBackground(Void... params) {

        String urlParams
                = "a=" + ID;

        connect c1 = new connect();
        String return_msg= c1.make_connect("get_program_info.php", urlParams);
        return return_msg;
    }

    @Override
    protected void onPostExecute(String responseBody)
    {
        super.onPostExecute(responseBody);
        String 	type, description;

        if(responseBody.equals("")){ // means there is an error
            Toast.makeText(c, "failed response from database!!", Toast.LENGTH_LONG).show();
        }
        else
        {
            try {

                JSONObject json = new JSONObject(responseBody);

                JSONArray ja_data = json.getJSONArray("posts");
                JSONObject jObj = ja_data.getJSONObject(0);
                String s = jObj.getString("post");
                JSONObject jObject = new JSONObject(s);
                type = jObject.getString("type");
                if (spinner_type!=null) {
                    int position = 0;
                    if (type.equals("Exercises")) {
                        position = 1;
                    }
                    spinner_type.setSelection(position);
                }
                else{
                    TV_type.setText(type);
                }
                description = jObject.getString("description");
                TV_description.setText(description);

            }

            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



}
