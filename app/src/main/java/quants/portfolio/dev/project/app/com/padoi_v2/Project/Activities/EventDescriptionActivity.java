package quants.portfolio.dev.project.app.com.padoi_v2.Project.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import quants.portfolio.dev.project.app.com.padoi_v2.R;

public class EventDescriptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_description);
        Toolbar toolbar = findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
        this.getSupportActionBar().setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
