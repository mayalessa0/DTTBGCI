package uni.fisal.king.dttbgci.Firebase_notification;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import uni.fisal.king.dttbgci.R;

public class FcmInstanceIdService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh()
    {
        String recent_token = FirebaseInstanceId.getInstance().getToken();
        SharedPreferences sharedPreferences = getApplicationContext()
                .getSharedPreferences(getString(R.string.FCM_PREF), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.FCM_TOKEN), recent_token);
        editor.commit();

    }
}
