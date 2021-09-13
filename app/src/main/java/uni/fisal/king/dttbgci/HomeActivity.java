package uni.fisal.king.dttbgci;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;
    String username, user_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        global_variables globalVariable = (global_variables) getApplicationContext();
        username = globalVariable.get_username();
        user_type = globalVariable.get_usertype();


        dl = findViewById(R.id.activity_home);
        t = new ActionBarDrawerToggle(this, dl,R.string.Open, R.string.Close);

        dl.addDrawerListener(t);
        t.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nv = findViewById(R.id.nv);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.view_profile) {

                    Intent i = new Intent(getApplicationContext(),View_profileActivity.class);
                    i.putExtra("past_activity", "HomeActivity");
                    startActivity(i);

                } else if (id == R.id.Search) {

                    if(user_type.equals("doctor")) {

                        Intent i = new Intent(getApplicationContext(), SearchActivity.class);
                        startActivity(i);
                    }
                    else{

                        String msg = "Sorry, you are not authorized to access this function";
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

                    }


                }else if (id == R.id.sign_out) {

                    alert();

                }

                dl.closeDrawer(GravityCompat.START);
                return true;
            }
        });



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(t.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    // we override this function to disable back button for home page on android device
    @Override
    public void onBackPressed() {
    }

    private void alert(){

        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this, R.style.MyDialogTheme);
        builder.setTitle("Warning!!");
        builder.setMessage("this will log out you from your account");

        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //reset username to null
                final global_variables globalVariable = (global_variables) getApplicationContext();
                globalVariable.set_username("");
                // move to login page
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);

            }
        });


        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });


        AlertDialog dialog = builder.create();

        dialog.show();

    }

    public void diet_program(View view){

        if(user_type.equals("doctor")) {

            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this, R.style.MyDialogTheme);
            builder.setTitle("Diet Program");
            builder.setMessage("Please select your prefer action");

            builder.setPositiveButton("Add New program", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Intent i = new Intent(getApplicationContext(), Add_programActivity.class);
                    startActivity(i);

                }
            });

            builder.setNegativeButton("View existing program", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    get_programs obj = new get_programs();
                    obj.execute();


                }
            });


            builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });




            AlertDialog dialog = builder.create();

            dialog.show();
        }
        else{

            get_programs obj = new get_programs();
            obj.execute();


        }

    }

    public void schedule(View view){

        if(user_type.equals("doctor")) {
            //----------------- doctor case --------------------------

            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this, R.style.MyDialogTheme);
            builder.setTitle("Schedules");
            builder.setMessage("Please select your prefer action");

            builder.setPositiveButton("Add New schedule", new DialogInterface.OnClickListener() {
                 CharSequence[] schedule_type = {"Appointment Schedule", "Medication Schedule",  "Program Schedule"};


                @Override
                public void onClick(DialogInterface dialog, int which) {

                    AlertDialog.Builder builder2 = new AlertDialog.Builder(HomeActivity.this, R.style.MyDialogTheme);
                    builder2.setTitle("Schedule Type");
                    builder2.setItems(schedule_type, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which){
                                        case 0:
                                            Intent intent = new Intent(getApplicationContext(), Add_AppointmentActivity.class);
                                            startActivity(intent);
                                            break;
                                        case 1:
                                            intent = new Intent(getApplicationContext(), Add_MedicationActivity.class);
                                            startActivity(intent);
                                            break;
                                        case 2:
                                            intent = new Intent(getApplicationContext(), Add_prog_scheduleActivity.class);
                                            startActivity(intent);
                                            break;

                                    }

                                }
                            });

                    builder2.create().show();

                }
            });

            builder.setNegativeButton("View existing schedules", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    get_schedules obj = new get_schedules();
                    obj.execute();
                }
            });


            builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });




            AlertDialog dialog = builder.create();

            dialog.show();
        }


        //----------------- diabetic case --------------------------
        else{

            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this, R.style.MyDialogTheme);
            builder.setTitle("Schedules");
            builder.setMessage("Please select your prefer action");

            builder.setPositiveButton("Add your sugar level", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Intent intent = new Intent(getApplicationContext(), Add_sugar_levelActivity.class);
                    startActivity(intent);
                }

            });

            builder.setNegativeButton("View your schedules", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    get_schedules obj = new get_schedules();
                    obj.execute();
                }
            });


            builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });




            AlertDialog dialog = builder.create();

            dialog.show();
        }





    }

    public void article(View view){

        if(user_type.equals("doctor")) {
            //----------------- doctor case --------------------------

            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this, R.style.MyDialogTheme);
            builder.setTitle("Article");
            builder.setMessage("Please select your prefer action");

            builder.setPositiveButton("Add New article", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Intent intent = new Intent(getApplicationContext(), Add_articleActivity.class);
                    startActivity(intent);
                }

            });

            builder.setNegativeButton("View existing article", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    get_articles obj = new get_articles();
                    obj.execute();
                }
            });


            builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });




            AlertDialog dialog = builder.create();

            dialog.show();
        }


        //----------------- diabetic case --------------------------
        else{
            get_articles obj = new get_articles();
            obj.execute();

        }

    }

    public void inquiry(View view){


        if(user_type.equals("diabetic")) {
            //----------------- diabetic case --------------------------

            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this, R.style.MyDialogTheme);
            builder.setTitle("Inquiry");
            builder.setMessage("Please select your prefer action");

            builder.setPositiveButton("Add New inquiry", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Intent intent = new Intent(getApplicationContext(), Add_inquiryActivity.class);
                    startActivity(intent);
                }

            });

            builder.setNegativeButton("View existing inquiry", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    get_inquiries obj = new get_inquiries();
                    obj.execute();
                }
            });


            builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });




            AlertDialog dialog = builder.create();

            dialog.show();
        }


        //----------------- doctor case --------------------------
        else{
            get_inquiries obj = new get_inquiries();
            obj.execute();

        }


    }






    class get_programs extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {

            connect c1 = new connect();
            String return_msg= c1.make_connect("get_programs.php", null);
            return return_msg;
        }

        @Override
        protected void onPostExecute(String responseBody)
        {
            if(responseBody.equals("")){ // means there is an error
                Toast.makeText(getApplicationContext(), "Failed from Database side", Toast.LENGTH_LONG).show();
            }
            else
            {
                Intent intent = new Intent(getApplicationContext(), List_of_programsActivity.class);
                intent.putExtra("programs" , responseBody);
                startActivity(intent);

            }
        }
    }

    class get_schedules extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            String urlParams = null;
            if (user_type.equals("diabetic")){

                urlParams = "a=" + username;

            }

            connect c1 = new connect();
            String return_msg= c1.make_connect("get_all_schedules.php", urlParams);
            return return_msg;
        }

        @Override
        protected void onPostExecute(String responseBody)
        {
            if(responseBody.equals("")){ // means there is an error
                Toast.makeText(getApplicationContext(), "Failed from Database side", Toast.LENGTH_LONG).show();
            }
            else
            {
                Intent intent = new Intent(getApplicationContext(), List_of_schedulesActivity.class);
                intent.putExtra("schedules" , responseBody);
                startActivity(intent);

            }
        }
    }

    class get_articles extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {

            connect c1 = new connect();
            String return_msg= c1.make_connect("get_all_articles.php", null);
            return return_msg;
        }

        @Override
        protected void onPostExecute(String responseBody)
        {
            if(responseBody.equals("")){ // means there is an error
                Toast.makeText(getApplicationContext(), "Failed from Database side", Toast.LENGTH_LONG).show();
            }
            else
            {
                Intent intent = new Intent(getApplicationContext(), List_articleActivity.class);
                intent.putExtra("articles" , responseBody);
                startActivity(intent);

            }
        }
    }


    class get_inquiries extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {

            connect c1 = new connect();
            String return_msg= c1.make_connect("get_all_inquiries.php", null);
            return return_msg;
        }

        @Override
        protected void onPostExecute(String responseBody)
        {
            if(responseBody.equals("")){ // means there is an error
                Toast.makeText(getApplicationContext(), "Failed from Database side", Toast.LENGTH_LONG).show();
            }
            else
            {
                Intent intent = new Intent(getApplicationContext(), List_of_inquiriesActivity.class);
                intent.putExtra("inquiries" , responseBody);
                startActivity(intent);

            }
        }
    }

}
