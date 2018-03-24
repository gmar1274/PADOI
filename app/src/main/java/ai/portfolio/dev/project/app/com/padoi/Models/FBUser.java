package ai.portfolio.dev.project.app.com.padoi.Models;

/**
 * Created by gabe on 3/13/2018.
 * Object from FB rest graph API response.
 * This entire app will only user FB credentials: id, image_url, full name
 */

public class FBUser {

    private String id,image_url,full_name;
   public FBUser(){

   }
   public FBUser(String id,String image_url, String full_name){
       this.full_name=full_name;
        this.id = id;
        this.image_url=image_url;
   }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }
}
