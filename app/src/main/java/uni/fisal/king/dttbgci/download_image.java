package uni.fisal.king.dttbgci;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class download_image extends AsyncTask<Void,Void,String> {

    Context c;
    String program_ID;
    ImageView[] img;

    public download_image(Context c, String program_ID, ImageView[] img) {
        this.c = c;
        this.program_ID = program_ID;
        this.img = img;

    }
    @Override
    protected String doInBackground(Void... params) {

        connect c1 =new connect();
        String urlParams
                ="a=" + program_ID;

        String return_msg = c1.make_connect("download_images.php",urlParams);
        return return_msg;
    }

    @Override
    protected void onPostExecute(String responseBody)
    {
        super.onPostExecute(responseBody);
        //reset image array
        global_variables globalVariable = (global_variables) c;
        for (int i=0; i<4; i++) {
            globalVariable.set_images(null, i);
        }

        try {
            JSONObject jsonObj = new JSONObject(responseBody);
            JSONArray jsonarr = jsonObj.getJSONArray("images");
            Boolean status = jsonObj.getBoolean("error");
            if (!status) {
                for (int i = 0; i < jsonarr.length(); i++) {
                    JSONObject item1 = jsonarr.getJSONObject(i);
                    String image_position = item1.getString("image_position");
                    String image_id = item1.getString("image_id");
                    Picasso.get().load(image_position).into(img[Integer.parseInt(image_id)]);

                    //save image URL to global class which has get/set function
                    globalVariable.set_images(image_position, Integer.parseInt(image_id));
                }
            }

        }

        catch (JSONException e) {
            e.printStackTrace();
        }
    }



}

