package ai.portfolio.dev.project.app.com.padoi.Models;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by gabe on 3/24/2018.
 */

public class PADOIUser {
    private String id;//user fb_id
    private List<String> likesBandID;//User id of bands user likes/ is connected with
    private  List<String> blocked; //users that are to be blocked
    private List<String> messages;//messages of users
    private Location userLocation;//current geoLocation

    public PADOIUser(){

    }
    public PADOIUser(String id, android.location.Location loc){
        this.id = id;
        this.userLocation = new Location(loc.getLatitude(),loc.getLongitude());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getLikesBandID() {
        return likesBandID;
    }

    public void setLikesBandID(List<String> likesBandID) {
        this.likesBandID = likesBandID;
    }

    public List<String> getBlocked() {
        return blocked;
    }

    public void setBlocked(List<String> blocked) {
        this.blocked = blocked;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    public Location getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(Location userLocation) {
        this.userLocation = userLocation;
    }
    public String toString(){
        String user = new Gson().toJson(this);
        return user;
    }
}
