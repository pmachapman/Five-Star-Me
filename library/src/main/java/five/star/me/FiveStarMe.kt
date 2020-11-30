package five.star.me

import android.app.Activity
import android.content.Context
import android.view.View
import java.util.*

class FiveStarMe private constructor(context: Context) {
    private val context: Context
    private val options = DialogOptions()
    private var installDate = 10
    private var launchTimes = 10
    private var remindInterval = 1
    private var callback: (() -> Unit)? = null
    var isDebug = false
        private set

    fun setLaunchTimes(launchTimes: Int): FiveStarMe {
        this.launchTimes = launchTimes
        return this
    }

    fun setInstallDays(installDate: Int): FiveStarMe {
        this.installDate = installDate
        return this
    }

    /**
     * The callback is invoked when the Review Prompt is displayed.
     * Use setCallback for analytics purposes.
     */
    fun setCallback(callback: () -> Unit): FiveStarMe {
        this.callback = callback
        return this
    }

    @Deprecated("")
    fun setRemindInterval(remindInterval: Int): FiveStarMe {
        this.remindInterval = remindInterval
        return this
    }

    @Deprecated("")
    fun setShowLaterButton(isShowNeutralButton: Boolean): FiveStarMe {
        options.setShowNeutralButton(isShowNeutralButton)
        return this
    }

    @Deprecated("")
    fun setShowNeverButton(isShowNeverButton: Boolean): FiveStarMe {
        options.setShowNegativeButton(isShowNeverButton)
        return this
    }

    @Deprecated("")
    fun setShowTitle(isShowTitle: Boolean): FiveStarMe {
        options.setShowTitle(isShowTitle)
        return this
    }

    fun clearAgreeShowDialog(): FiveStarMe {
        PreferenceHelper.setAgreeShowDialog(context, true)
        return this
    }

    @Deprecated("")
    fun clearSettingsParam(): FiveStarMe {
        PreferenceHelper.setAgreeShowDialog(context, true)
        PreferenceHelper.clearSharedPreferences(context)
        return this
    }

    private fun setAgreeShowDialog(clear: Boolean): FiveStarMe {
        PreferenceHelper.setAgreeShowDialog(context, clear)
        return this
    }

    @Deprecated("")
    fun setView(view: View?): FiveStarMe {
        options.view = view
        return this
    }

    @Deprecated("")
    fun setOnClickButtonListener(listener: OnClickButtonListener?): FiveStarMe {
        options.listener = listener
        return this
    }

    @Deprecated("")
    fun setTitle(resourceId: Int): FiveStarMe {
        options.titleResId = resourceId
        return this
    }

    @Deprecated("")
    fun setTitle(title: String?): FiveStarMe {
        options.setTitleText(title)
        return this
    }

    @Deprecated("")
    fun setMessage(resourceId: Int): FiveStarMe {
        options.messageResId = resourceId
        return this
    }

    @Deprecated("")
    fun setMessage(message: String?): FiveStarMe {
        options.setMessageText(message)
        return this
    }

    @Deprecated("")
    fun setTextRateNow(resourceId: Int): FiveStarMe {
        options.textPositiveResId = resourceId
        return this
    }

    @Deprecated("")
    fun setTextRateNow(positiveText: String?): FiveStarMe {
        options.setPositiveText(positiveText)
        return this
    }

    @Deprecated("")
    fun setTextLater(resourceId: Int): FiveStarMe {
        options.textNeutralResId = resourceId
        return this
    }

    @Deprecated("")
    fun setTextLater(neutralText: String?): FiveStarMe {
        options.setNeutralText(neutralText)
        return this
    }

    @Deprecated("")
    fun setTextNever(resourceId: Int): FiveStarMe {
        options.textNegativeResId = resourceId
        return this
    }

    @Deprecated("")
    fun setTextNever(negativeText: String?): FiveStarMe {
        options.setNegativeText(negativeText)
        return this
    }

    @Deprecated("")
    fun setCancelable(cancelable: Boolean): FiveStarMe {
        options.cancelable = cancelable
        return this
    }

    @Deprecated("")
    fun setStoreType(appstore: StoreType?): FiveStarMe {
        options.storeType = appstore
        return this
    }

    fun monitor() {
        if (PreferenceHelper.isFirstLaunch(context)) {
            PreferenceHelper.setInstallDate(context)
        }
        PreferenceHelper.setLaunchTimes(context, PreferenceHelper.getLaunchTimes(context) + 1)
    }

    fun showRateDialog(activity: Activity) {
        if (!activity.isFinishing) {
            //Display Dialog
            val instance = InAppReview(activity)
            instance.startRequest()
            setAgreeShowDialog(false)
            callback?.invoke()
        }
    }

    fun shouldShowRateDialog(): Boolean {
        return PreferenceHelper.getIsAgreeShowDialog(context) &&
                isOverLaunchTimes &&
                isOverInstallDate &&
                isOverRemindDate
    }

    private val isOverLaunchTimes: Boolean
        private get() = PreferenceHelper.getLaunchTimes(context) >= launchTimes
    private val isOverInstallDate: Boolean
        private get() = isOverDate(PreferenceHelper.getInstallDate(context), installDate)
    private val isOverRemindDate: Boolean
        private get() = isOverDate(PreferenceHelper.getRemindInterval(context), remindInterval)

    fun setDebug(isDebug: Boolean): FiveStarMe {
        this.isDebug = isDebug
        return this
    }

    companion object {
        private var singleton: FiveStarMe? = null

        @JvmStatic
        fun with(context: Context): FiveStarMe {
            if (singleton == null) {
                synchronized(FiveStarMe::class.java) {
                    if (singleton == null) {
                        singleton = FiveStarMe(context)
                    }
                }
            }
            return singleton!!
        }

        @JvmStatic
        fun showRateDialogIfMeetsConditions(activity: Activity): Boolean {
            val isMeetsConditions = singleton!!.isDebug || singleton!!.shouldShowRateDialog()
            if (isMeetsConditions) {
                singleton!!.showRateDialog(activity)
            }
            return isMeetsConditions
        }

        private fun isOverDate(targetDate: Long, threshold: Int): Boolean {
            return Date().time - targetDate >= threshold * 24 * 60 * 60 * 1000
        }
    }

    init {
        this.context = context.applicationContext
    }
}