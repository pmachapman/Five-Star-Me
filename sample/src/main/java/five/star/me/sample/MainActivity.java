package five.star.me.sample;

import android.app.Activity;
import android.os.Bundle;

import five.star.me.FiveStarMe;
import hotchemi.android.rate.sample.R;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FiveStarMe.with(this)
                .setInstallDays(0) // default 10, 0 means install day.
                .setLaunchTimes(4) // default 10 times.
                .setDebug(true) // default false.
                .monitor();

        FiveStarMe.showRateDialogIfMeetsConditions(this);
    }

}