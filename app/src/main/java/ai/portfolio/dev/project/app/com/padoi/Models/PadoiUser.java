package ai.portfolio.dev.project.app.com.padoi.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import java.util.Collections;
import java.util.List;

import ai.portfolio.dev.project.app.com.padoi.Utils.PADOI;

/**
 * This is a model class for PADOI firebase object.
 * This class will contain fields such as:
 * 1) user band following
 * 2) other users current user wants to block
 * 3) user current location
 * 4) messages between users
 * 5) unique PADOI id.
 */

public class PadoiUser implements Parcelable {
    private String id;//user fb_id
    private List<String> likesBandID;//User id of bands user likes/ is connected with
    private List<String> blocked; //users that are to be blocked
    private List<String> messages;//messages of users
    private Location userLocation;//current geoLocation
    private boolean isActive;
    private String name;
    private String image_url;
    /**
     * Initialize a new user for firebase
     * @param currentUser firebase user
     */
    public PadoiUser(FirebaseUser currentUser){
            this.id = currentUser.getUid();
            this.userLocation = null;
            this.likesBandID = Collections.emptyList();
            this.blocked =Collections.emptyList();
            this.messages = Collections.emptyList();
            this.isActive = true;
            this.name = currentUser.getDisplayName();
            this.image_url = PADOI.image_url(currentUser.getPhotoUrl());//custom image size
    }

    protected PadoiUser(Parcel in) {
        id = in.readString();
        likesBandID = in.createStringArrayList();
        blocked = in.createStringArrayList();
        messages = in.createStringArrayList();
        isActive = in.readByte() != 0;
        name = in.readString();
        image_url = in.readString();
    }

    public static final Creator<PadoiUser> CREATOR = new Creator<PadoiUser>() {
        @Override
        public PadoiUser createFromParcel(Parcel in) {
            return new PadoiUser(in);
        }

        @Override
        public PadoiUser[] newArray(int size) {
            return new PadoiUser[size];
        }
    };

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeStringList(likesBandID);
        dest.writeStringList(blocked);
        dest.writeStringList(messages);
        dest.writeByte((byte) (isActive ? 1 : 0));
        dest.writeString(name);
        dest.writeString(image_url);
    }

}
