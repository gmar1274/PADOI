package quants.portfolio.dev.project.app.com.padoi_v2.Project.TEST;

import java.util.ArrayList;

import quants.portfolio.dev.project.app.com.padoi_v2.Project.Models.Event;

public class TEST {
    public static ArrayList<String> generateTestString(int size){
        ArrayList<String> data = new ArrayList<>();
        for (int i=0;i<size;++i){
            data.add("Test string: "+i);
        }
        return data;
    }

    public static ArrayList<Event> generateTestEvents(int size) {
        ArrayList<Event> data = new ArrayList<>();
        for (int i=0;i<size;++i){
            data.add(new Event("Test String: "+i,String.valueOf(i)));
        }
        return data;
    }
}
