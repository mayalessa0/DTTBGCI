package uni.fisal.king.dttbgci;

import static uni.fisal.king.dttbgci.Constants.FIRST_COLUMN;
import static uni.fisal.king.dttbgci.Constants.FOURTH_COLUMN;
import static uni.fisal.king.dttbgci.Constants.SECOND_COLUMN;
import static uni.fisal.king.dttbgci.Constants.THIRD_COLUMN;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;


public class ListViewAdapters extends BaseAdapter {

    public ArrayList<HashMap<String, String>> list;
    Activity activity;
    TextView txtFirst;
    TextView txtSecond;
    TextView txtThird;
    TextView txtFourth;
    public ListViewAdapters(Activity activity, ArrayList<HashMap<String, String>> list){
        super();
        this.activity=activity;
        this.list=list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub



        LayoutInflater inflater=activity.getLayoutInflater();
        View view;

        view=inflater.inflate(R.layout.culmn_row, null);

        txtFirst= view.findViewById(R.id.first);
        txtSecond= view.findViewById(R.id.second);
        txtThird= view.findViewById(R.id.third);
        txtFourth= view.findViewById(R.id.fourth);


        HashMap<String, String> map=list.get(position);
        txtFirst.setText(map.get(FIRST_COLUMN));
        txtSecond.setText(map.get(SECOND_COLUMN));
        txtThird.setText(map.get(THIRD_COLUMN));
        txtFourth.setText(map.get(FOURTH_COLUMN));

        // make first row bold and has green color
        if (position == 0) {
            txtFirst.setTypeface(null, Typeface.BOLD);
            txtFirst.setTextColor(Color.parseColor("#009688"));
            txtSecond.setTypeface(null, Typeface.BOLD);
            txtSecond.setTextColor(Color.parseColor("#009688"));
            txtThird.setTypeface(null, Typeface.BOLD);
            txtThird.setTextColor(Color.parseColor("#009688"));
            txtFourth.setTypeface(null, Typeface.BOLD);
            txtFourth.setTextColor(Color.parseColor("#009688"));

        }

        return view;
    }

}
