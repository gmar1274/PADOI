package ai.portfolio.dev.project.app.com.padoi.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by gabe on 3/19/2018.
 */

public class BandUser implements Parcelable{
    private String id, band_id, image_url, name;
    private Location location;
    private boolean isVisible;
    private String user_since;

    protected BandUser(Parcel in) {
        id = in.readString();
        band_id = in.readString();
        image_url = in.readString();
        name = in.readString();
        isVisible = in.readByte() != 0;
        user_since = in.readString();
    }

    public static final Creator<BandUser> CREATOR = new Creator<BandUser>() {
        @Override
        public BandUser createFromParcel(Parcel in) {
            return new BandUser(in);
        }

        @Override
        public BandUser[] newArray(int size) {
            return new BandUser[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getBand_id() {
        return band_id;
    }

    public void setBand_id(String band_id) {
        this.band_id = band_id;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public String getUser_since() {
        return user_since;
    }

    public void setUser_since(String user_since) {
        this.user_since = user_since;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
    public String toString(){
        return "BandUser:{id:"+id+", name:"+ name +", image_url:"+this.image_url +", "+location+"}";
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(band_id);
        dest.writeString(image_url);
        dest.writeString(name);
        dest.writeByte((byte) (isVisible ? 1 : 0));
        dest.writeString(user_since);
    }
}
