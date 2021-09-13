package uni.fisal.king.dttbgci;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class ViewProgramActivity extends AppCompatActivity implements View.OnClickListener{


    TextView TV_type, TVdesc;
    Button edit_buttom;
    ImageView[] images;

    String ID, user_type;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_program);

        global_variables globalVariable = (global_variables) getApplicationContext();
        user_type = globalVariable.get_usertype();

        TV_type = findViewById(R.id.TVtype);
        TVdesc = findViewById(R.id.TVdesc);
        edit_buttom = findViewById(R.id.button1);

        images = new ImageView[4];
        images[0] = findViewById(R.id.imageView);
        images[1] = findViewById(R.id.imageView2);
        images[2] = findViewById(R.id.imageView3);
        images[3] = findViewById(R.id.imageView4);



        ID  = getIntent().getStringExtra("program_id");

        new get_program_info(ViewProgramActivity.this, ID, TV_type, TVdesc).execute();

        new download_image(getApplicationContext(), ID, images).execute();

        images[0].setOnClickListener(this);
        images[1].setOnClickListener(this);
        images[2].setOnClickListener(this);
        images[3].setOnClickListener(this);

        if (user_type.equals("diabetic")){

            edit_buttom.setVisibility(View.INVISIBLE);

        }

    }




    public void Edit(View view) {

        Intent i = new Intent(getApplicationContext(), Edit_programActivity.class);
        i.putExtra("ID",ID);
        startActivity(i);
    }


    @Override
    public void onClick(View v) {
        final global_variables globalVariable = (global_variables) getApplicationContext();
        String[] imageURL = globalVariable.get_images();

        if (v == images[0]) {
            if (imageURL[0]!=null){
                Intent intent = new Intent(getApplicationContext(), View_ImageActivity.class);
                intent.putExtra("image_id", 0);
                startActivity(intent);
            }
        }
        if (v == images[1]) {
            if (imageURL[1]!=null){
                Intent intent = new Intent(getApplicationContext(), View_ImageActivity.class);
                intent.putExtra("image_id", 1);
                startActivity(intent);
            }
        }
        if (v == images[2]) {
            if (imageURL[2]!=null){
                Intent intent = new Intent(getApplicationContext(), View_ImageActivity.class);
                intent.putExtra("image_id", 2);
                startActivity(intent);
            }
        }
        if (v == images[3]) {
            if (imageURL[3]!=null){
                Intent intent = new Intent(getApplicationContext(), View_ImageActivity.class);
                intent.putExtra("image_id", 3);
                startActivity(intent);
            }
        }




    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);

    }

}
