package ai.portfolio.dev.project.app.com.padoi;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ai.portfolio.dev.project.app.com.padoi.Activities.LoginActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {
    @Rule
    public ActivityTestRule<LoginActivity> loginActivityActivityTestRule=new ActivityTestRule<LoginActivity>(LoginActivity.class);
    @Test
    public void onLoginClick() throws Exception{
        onView(withId(R.id.fb_sign_in)).perform(click());
    }
}
