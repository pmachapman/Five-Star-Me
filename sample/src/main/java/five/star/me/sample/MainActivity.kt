package five.star.me.sample

import android.app.Activity
import android.os.Bundle
import five.star.me.FiveStarMe.Companion.showRateDialogIfMeetsConditions
import five.star.me.FiveStarMe.Companion.with
import hotchemi.android.rate.sample.R

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        with(this)
                .setInstallDays(0) // default 10, 0 means install day.
                .setLaunchTimes(4) // default 10 times.
                .setDebug(true) // default false.
                .monitor()
        showRateDialogIfMeetsConditions(this)
    }
}