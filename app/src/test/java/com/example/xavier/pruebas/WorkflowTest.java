package com.example.xavier.pruebas;

import org.junit.Test;
import org.junit.runner.RunWith;
import android.support.test.runner.AndroidJUnit4;


/**
 * Created by Xavier on 29/07/2016.
 */
@RunWith(AndroidJUnit4.class)
public class WorkflowTest {

    @Test
    public void sayHello(){
        onView(withText("Say hello!")).perform(click());

        onView(withId(R.id.textView)).check(matches(withText("Hello, World!")));
    }

}
