package uni.fisal.king.dttbgci;


import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

//this class for establish new connection to server for specific file
public class connect {

    public String make_connect(String file_name,String msg){
        String data = "";
        int tmp;
//        Log.d("parameters",msg);

        try {
            URL url = new URL("http://uniproject.co/"+file_name);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setDoOutput(true);
            OutputStream os = httpURLConnection.getOutputStream();

            if (msg != null){
                os.write(msg.getBytes());
            }
            int statusCode =httpURLConnection.getResponseCode();
            InputStream error_msg = httpURLConnection.getErrorStream();
            Log.d("status",String.valueOf(statusCode));
            Log.d("err_msg",String.valueOf(error_msg));

            os.flush();
            os.close();

            InputStream is = httpURLConnection.getInputStream();
            int length = 0;

            while ((tmp = is.read()) != -1) {
                data += (char) tmp;
                length++;
            }
            Log.d("Stream length",String.valueOf(length));
            Log.d("coming response",data);

            is.close();
            httpURLConnection.disconnect();
            return data;

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "Exception: " + e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
            return "Exception: " + e.getMessage();
        }
    }

}
