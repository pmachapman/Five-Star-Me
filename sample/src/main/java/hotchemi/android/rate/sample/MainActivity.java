package hotchemi.android.rate.sample;

import android.app.Activity;
import android.os.Bundle;

import hotchemi.android.rate.AppRate;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppRate.with(this)
                .setInstallDays(0) // default 10, 0 means install day.
                .setLaunchTimes(4) // default 10 times.
                .setDebug(true) // default false.
                .monitor();

        AppRate.showRateDialogIfMeetsConditions(this);
    }

}