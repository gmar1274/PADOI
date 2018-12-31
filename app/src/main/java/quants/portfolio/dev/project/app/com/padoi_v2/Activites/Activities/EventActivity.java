package quants.portfolio.dev.project.app.com.padoi_v2.Activites.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import quants.portfolio.dev.project.app.com.padoi_v2.Activites.Models.Event;
import quants.portfolio.dev.project.app.com.padoi_v2.R;

public class EventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Event event = getIntent().getParcelableExtra(Event.TAG);
        TextView tv = findViewById(R.id.textView);
        tv.setText(event.getId());
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                // ProjectsActivity is my 'home' activity
                super. onBackPressed();
                return true;
        }
        return (super.onOptionsItemSelected(menuItem));
    }
}
