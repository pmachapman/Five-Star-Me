package five.star.me;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import java.util.Date;

import static five.star.me.PreferenceHelper.getInstallDate;
import static five.star.me.PreferenceHelper.getIsAgreeShowDialog;
import static five.star.me.PreferenceHelper.getLaunchTimes;
import static five.star.me.PreferenceHelper.getRemindInterval;
import static five.star.me.PreferenceHelper.isFirstLaunch;
import static five.star.me.PreferenceHelper.setInstallDate;

public final class FiveStarMe {

    private static FiveStarMe singleton;

    private final Context context;

    private final DialogOptions options = new DialogOptions();

    private int installDate = 10;

    private int launchTimes = 10;

    private int remindInterval = 1;

    private boolean isDebug = false;

    private FiveStarMe(Context context) {
        this.context = context.getApplicationContext();
    }

    public static FiveStarMe with(Context context) {
        if (singleton == null) {
            synchronized (FiveStarMe.class) {
                if (singleton == null) {
                    singleton = new FiveStarMe(context);
                }
            }
        }
        return singleton;
    }

    public static boolean showRateDialogIfMeetsConditions(Activity activity) {
        boolean isMeetsConditions = singleton.isDebug || singleton.shouldShowRateDialog();
        if (isMeetsConditions) {
            singleton.showRateDialog(activity);
        }
        return isMeetsConditions;
    }

    private static boolean isOverDate(long targetDate, int threshold) {
        return new Date().getTime() - targetDate >= threshold * 24 * 60 * 60 * 1000;
    }

    public FiveStarMe setLaunchTimes(int launchTimes) {
        this.launchTimes = launchTimes;
        return this;
    }

    public FiveStarMe setInstallDays(int installDate) {
        this.installDate = installDate;
        return this;
    }

    @Deprecated
    public FiveStarMe setRemindInterval(int remindInterval) {
        this.remindInterval = remindInterval;
        return this;
    }

    @Deprecated
    public FiveStarMe setShowLaterButton(boolean isShowNeutralButton) {
        options.setShowNeutralButton(isShowNeutralButton);
        return this;
    }

    @Deprecated
    public FiveStarMe setShowNeverButton(boolean isShowNeverButton) {
        options.setShowNegativeButton(isShowNeverButton);
        return this;
    }

    @Deprecated
    public FiveStarMe setShowTitle(boolean isShowTitle) {
        options.setShowTitle(isShowTitle);
        return this;
    }

    public FiveStarMe clearAgreeShowDialog() {
        PreferenceHelper.setAgreeShowDialog(context, true);
        return this;
    }

    @Deprecated
    public FiveStarMe clearSettingsParam() {
        PreferenceHelper.setAgreeShowDialog(context, true);
        PreferenceHelper.clearSharedPreferences(context);
        return this;
    }


    private FiveStarMe setAgreeShowDialog(boolean clear) {
        PreferenceHelper.setAgreeShowDialog(context, clear);
        return this;
    }

    @Deprecated
    public FiveStarMe setView(View view) {
        options.setView(view);
        return this;
    }

    @Deprecated
    public FiveStarMe setOnClickButtonListener(OnClickButtonListener listener) {
        options.setListener(listener);
        return this;
    }

    @Deprecated
    public FiveStarMe setTitle(int resourceId) {
        options.setTitleResId(resourceId);
        return this;
    }

    @Deprecated
    public FiveStarMe setTitle(String title) {
        options.setTitleText(title);
        return this;
    }

    @Deprecated
    public FiveStarMe setMessage(int resourceId) {
        options.setMessageResId(resourceId);
        return this;
    }

    @Deprecated
    public FiveStarMe setMessage(String message) {
        options.setMessageText(message);
        return this;
    }

    @Deprecated
    public FiveStarMe setTextRateNow(int resourceId) {
        options.setTextPositiveResId(resourceId);
        return this;
    }

    @Deprecated
    public FiveStarMe setTextRateNow(String positiveText) {
        options.setPositiveText(positiveText);
        return this;
    }

    @Deprecated
    public FiveStarMe setTextLater(int resourceId) {
        options.setTextNeutralResId(resourceId);
        return this;
    }

    @Deprecated
    public FiveStarMe setTextLater(String neutralText) {
        options.setNeutralText(neutralText);
        return this;
    }

    @Deprecated
    public FiveStarMe setTextNever(int resourceId) {
        options.setTextNegativeResId(resourceId);
        return this;
    }

    @Deprecated
    public FiveStarMe setTextNever(String negativeText) {
        options.setNegativeText(negativeText);
        return this;
    }

    @Deprecated
    public FiveStarMe setCancelable(boolean cancelable) {
        options.setCancelable(cancelable);
        return this;
    }

    @Deprecated
    public FiveStarMe setStoreType(StoreType appstore) {
        options.setStoreType(appstore);
        return this;
    }

    public void monitor() {
        if (isFirstLaunch(context)) {
            setInstallDate(context);
        }
        PreferenceHelper.setLaunchTimes(context, getLaunchTimes(context) + 1);
    }

    public void showRateDialog(Activity activity) {
        if (!activity.isFinishing()) {
            //Display Dialog
            InAppReview instance = new InAppReview(activity);
            instance.startRequest();

            setAgreeShowDialog(false);
        }
    }

    public boolean shouldShowRateDialog() {
        return getIsAgreeShowDialog(context) &&
                isOverLaunchTimes() &&
                isOverInstallDate() &&
                isOverRemindDate();
    }

    private boolean isOverLaunchTimes() {
        return getLaunchTimes(context) >= launchTimes;
    }

    private boolean isOverInstallDate() {
        return isOverDate(getInstallDate(context), installDate);
    }

    private boolean isOverRemindDate() {
        return isOverDate(getRemindInterval(context), remindInterval);
    }

    public boolean isDebug() {
        return isDebug;
    }

    public FiveStarMe setDebug(boolean isDebug) {
        this.isDebug = isDebug;
        return this;
    }

}
