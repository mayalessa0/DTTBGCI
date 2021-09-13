package uni.fisal.king.dttbgci;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class delete_schedule extends AsyncTask<Void,Void,String> {

    Context c;
    String schedule_id, scedule_type;

    public delete_schedule(Context c, String schedule_id, String scedule_type) {
        this.c = c;
        this.schedule_id = schedule_id;
        this.scedule_type = scedule_type;

    }
    @Override
    protected String doInBackground(Void... params) {

        connect c1 =new connect();
        String urlParams
                ="a=" + schedule_id +
                "&b=" + scedule_type ;

        String return_msg = c1.make_connect("delete_schedule.php",urlParams);
        return return_msg;
    }

    @Override
    protected void onPostExecute(String s)
    {
        super.onPostExecute(s);
        if(!s.isEmpty()) { // means there is no error

            Toast.makeText(c, s, Toast.LENGTH_LONG).show();
        }


    }
}
