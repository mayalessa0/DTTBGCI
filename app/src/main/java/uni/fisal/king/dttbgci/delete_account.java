package uni.fisal.king.dttbgci;


import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;


public class delete_account extends AsyncTask<Void,Void,String> {

    Context c;
    String user, type;

    public delete_account(Context c, String user, String type) {
        this.c = c;
        this.user = user;
        this.type = type;

    }
    @Override
    protected String doInBackground(Void... params) {

        connect c1 =new connect();
        String urlParams
                ="a=" + user +
                "&b=" + type ;

        String return_msg = c1.make_connect("delete_account.php",urlParams);
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
