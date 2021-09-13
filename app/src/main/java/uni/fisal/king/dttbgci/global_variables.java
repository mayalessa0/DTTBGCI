package uni.fisal.king.dttbgci;

import android.app.Application;
import java.util.ArrayList;


// create a global Class to save global variable in whole application
public class global_variables extends Application {

    private  String username;
    private  String user_type;
    private String[] images_position = new String[4];
    private  ArrayList<String> list = new ArrayList();


    public String get_username() { return username; }

    public  void set_username(String login_username) {
        username = login_username;
    }

    public String get_usertype() {
        return user_type;
    }

    public  void set_usertype(String U_type) {
        user_type = U_type;
    }

    public String[] get_images() { return images_position; }

    public  void set_images(String position, int index) {
        images_position[index] = position;
    }

    public ArrayList<String> get_list() { return list; }

    public  void set_list(String element) {
        list.add(element);
    }

    public void reset_list (){
       list.clear();
    }

}
