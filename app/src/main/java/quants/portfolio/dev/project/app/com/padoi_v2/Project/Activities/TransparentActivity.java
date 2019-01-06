package quants.portfolio.dev.project.app.com.padoi_v2.Project.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import quants.portfolio.dev.project.app.com.padoi_v2.R;

public class TransparentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transparent);
        Toolbar toolbar = findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
        this.getSupportActionBar().setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onActionHomePressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void onActionHomePressed(){
        supportFinishAfterTransition();
    }
}
