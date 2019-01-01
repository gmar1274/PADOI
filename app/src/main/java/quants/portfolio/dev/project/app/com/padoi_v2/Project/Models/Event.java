package quants.portfolio.dev.project.app.com.padoi_v2.Project.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Event implements Parcelable {
    public static final String TAG=Event.class.toString();
    private String name,id;
    private Act act;
    private Venue venue;
    public Event(){

    }
    public Event(String name, String id){
        this.name=name;
        this.id = id;
    }

    protected Event(Parcel in) {
        name = in.readString();
        id = in.readString();
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public Act getAct() {
        return act;
    }

    public Venue getVenue() {
        return venue;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(id);
    }
}
