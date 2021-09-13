package uni.fisal.king.dttbgci;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

public class Edit_programActivity extends AppCompatActivity implements View.OnClickListener{


    Spinner spinner_type;
    EditText ET_description;
    String type, description, ID;
    private Button buttonUpload;

    // array of imageView
    ImageView[] images;

    //Image request code
    private int PICK_IMAGE_REQUEST = 1;

    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;

    //Bitmap to get image from gallery
    private Bitmap bitmap;

    //Uri to store the image uri
    private Uri filePath;
    Uri [] paths;

    // for identify image bottom
    Integer Image_ID;

    //we use date and time to create unique key to named images
    private Calendar calendar;
    private SimpleDateFormat dateFormat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_program);


        //Initializing views
        buttonUpload = findViewById(R.id.button14);
        spinner_type = findViewById(R.id.editText12);
        ET_description = findViewById(R.id.editText14);
        paths = new Uri[4];
        images = new ImageView[4];
        images[0]= findViewById(R.id.imageView);
        images[1] = findViewById(R.id.imageView2);
        images[2] = findViewById(R.id.imageView3);
        images[3] = findViewById(R.id.imageView4);
        ID  = getIntent().getStringExtra("ID");


        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter_type = ArrayAdapter.createFromResource(this,
                R.array.program_type, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter_type.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner_type.setAdapter(adapter_type);

        //Requesting storage permission
        requestStoragePermission();

        //Setting clicklistener
        buttonUpload.setOnClickListener(this);
        images[0].setOnClickListener(this);
        images[1].setOnClickListener(this);
        images[2].setOnClickListener(this);
        images[3].setOnClickListener(this);

        new get_program_info(Edit_programActivity.this, ID, spinner_type, ET_description).execute();
        new download_image(getApplicationContext(), ID, images).execute();
    }


    public void uploadMultipart() {



        description= ET_description.getText().toString();
        type = String.valueOf(spinner_type.getSelectedItem());


        if (description.isEmpty()) {
            // show error message if user does not fill description field
            Toast.makeText(getApplicationContext(),
                    "All fields are mandatory required", Toast.LENGTH_LONG).show();
            return;
        }else {

            Add_programToDB obj = new Add_programToDB();
            obj.execute();


        }
    }




    class Add_programToDB extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            connect c1 =new connect();

            String urlParams
                    = "a=" + description
                    + "&b=" + type
                    + "&c=" + ID;

            String return_msg = c1.make_connect("update_program.php",urlParams);


            return return_msg;
        }

        @Override
        protected void onPostExecute(String s)
        {
            if(!s.equals("")){ // means there is no error


                //we use date and time to create unique key to named images
                calendar = Calendar.getInstance();
                dateFormat = new SimpleDateFormat("MM-dd-yy-HHmmss");
                String random_key = dateFormat.format(calendar.getTime());


                for (int i=0; i<paths.length; i++) {
                    if (paths[i] != null) {
                        String img_name = random_key+"-ID:"+i;
                        Log.d("img_name: ",img_name);
                        Log.d("image_id: ",String.valueOf(i));

                        String path = null;
                        try {
                            //getting the actual path of the image
                            path = getFilePath(getApplicationContext(),paths[i]);

                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }

                        //Uploading code
                        try {
                            String uploadId = UUID.randomUUID().toString();

                            //Creating a multi part request
                            new MultipartUploadRequest(getApplicationContext(), uploadId, Constants.UPDATE_URL)
                                    .addFileToUpload(path, "image") //Adding file
                                    .addParameter("img_name", img_name) //Adding text parameter to the request
                                    .addParameter("program_id", ID)
                                    .addParameter("img_id", String.valueOf(i))
                                    .setNotificationConfig(new UploadNotificationConfig())
                                    .setMaxRetries(2)
                                    .startUpload(); //Starting the upload


                        } catch (Exception exc) {
                            Toast.makeText(getApplicationContext(), exc.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                //go to viewProgramActivity after finish update
                Intent i = new Intent(getApplicationContext(), ViewProgramActivity.class);
                i.putExtra("program_id",ID);
                startActivity(i);
                //show completed message
                Toast.makeText(getApplicationContext(), "Update process has been completed", Toast.LENGTH_LONG).show();


            }
            else{

                Toast.makeText(getApplicationContext(), "Failed!! program is not updated successfully", Toast.LENGTH_LONG).show();


            }

        }

    }

    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }


    //method to show file chooser
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    //handling the image chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            try {
                filePath = data.getData();
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                paths[Image_ID] = filePath;
                images[Image_ID].setImageBitmap(bitmap);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //method to get the file path (as string) from uri
    @SuppressLint("NewApi")
    public static String getFilePath(Context context, Uri uri) throws URISyntaxException {
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                if (!TextUtils.isEmpty(id)) {
                    if (id.startsWith("raw:")) {
                        return id.replaceFirst("raw:", "");
                    }
                    try {
                        uri = ContentUris.withAppendedId(
                                Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                    } catch (NumberFormatException e) {
                        return null;
                    }
                }
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{
                        split[1]
                };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {
                    MediaStore.Images.Media.DATA
            };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    @Override
    public void onClick(View v) {
        if (v == images[0]) {
            Image_ID = 0;
            showFileChooser();
        }
        if (v == images[1]) {
            Image_ID = 1;
            showFileChooser();
        }
        if (v == images[2]) {
            Image_ID = 2;
            showFileChooser();
        }
        if (v == images[3]) {
            Image_ID = 3;
            showFileChooser();
        }
        if (v == buttonUpload) {
            uploadMultipart();
        }
    }

    public void delete_program(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(Edit_programActivity.this, R.style.MyDialogTheme);
        builder.setTitle("Warning!!");
        builder.setMessage("This will delete the Program permanently");

        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                delete obj = new delete();
                obj.execute();

            }
        });


        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });


        AlertDialog diag = builder.create();

        diag.show();
    }

    public class delete extends AsyncTask<Void,Void,String> {


        @Override
        protected String doInBackground(Void... params) {

            connect c1 =new connect();
            String urlParams
                    ="a=" + ID;

            String return_msg = c1.make_connect("delete_program.php",urlParams);
            return return_msg;
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            if(!s.isEmpty()) { // means there is no error

                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(getApplicationContext(), "Program has been deleted Successfully", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            }


        }
    }
}
