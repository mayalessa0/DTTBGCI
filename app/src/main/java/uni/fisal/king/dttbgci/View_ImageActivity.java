package uni.fisal.king.dttbgci;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;

public class View_ImageActivity extends AppCompatActivity {
    ImageView imgV;
    int id;
    String[] imageURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);
        imgV = findViewById(R.id.imageView6);
        id =  getIntent().getIntExtra("image_id",0);
        final global_variables globalVariable = (global_variables) getApplicationContext();
        imageURL = globalVariable.get_images();
        Picasso.get().load(imageURL[id]).into(imgV);

    }
}
