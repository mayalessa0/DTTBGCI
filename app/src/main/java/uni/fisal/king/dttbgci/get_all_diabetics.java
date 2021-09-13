package uni.fisal.king.dttbgci;

import android.content.Context;
import android.os.AsyncTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class get_all_diabetics extends AsyncTask<Void,Void,String> {

    Context c;

    public get_all_diabetics(Context c) {
        this.c = c;

    }



    @Override
    protected String doInBackground(Void... params) {

        connect c1 =new connect();

        String return_msg = c1.make_connect("get_all_diabetics.php",null);
        return return_msg;
    }

    @Override
    protected void onPostExecute(String responseBody)
    {

        try {
            JSONObject json = new JSONObject(responseBody);

            JSONArray ja_data = json.getJSONArray("posts");


            global_variables globalVariable = (global_variables) c;
            globalVariable.reset_list();


            for (int i = 0; i < ja_data.length(); i++) {
                JSONObject jObj = ja_data.getJSONObject(i);
                String s = jObj.getString("post");
                JSONObject jObject = new JSONObject(s);
                String diabetic = jObject.getString("diabetic_id");
                globalVariable.set_list(diabetic);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


}
