package ai.portfolio.dev.project.app.com.padoi.PageViewerAdapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

import ai.portfolio.dev.project.app.com.padoi.Models.BandUser;
import ai.portfolio.dev.project.app.com.padoi.R;

/**
 * Created by gabe on 3/22/2018.
 */

public class SwipePageViewerAdapter extends PagerAdapter {
    private Context context;
    private List<BandUser> bandUsers;
    public  SwipePageViewerAdapter(Context context, List<BandUser> bandUsers){
        this.context=context;
        this.bandUsers = bandUsers;
    }
    @Override
    public int getCount() {
        return this.bandUsers.size();
    }

    /**
     * CASTS VIEW TO THE LAYOUT TYPE OF PAGERVIEW LAYOUT TYPE
     * @param view
     * @param object
     * @return
     */
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view==(LinearLayout)object);
    }

    /**
     * INFLATE the layout for ViewPager to display UI components.
     * @param container
     * @param position
     * @return
     */
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.band_user_layout,container,false);
        //
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        //container.removeView((VIEWPAGER_LAYOUT_TYPE) object);
        container.removeView((LinearLayout)object);
    }
}
