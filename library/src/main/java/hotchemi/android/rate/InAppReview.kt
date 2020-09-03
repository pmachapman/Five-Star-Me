package hotchemi.android.rate

import android.app.Activity
import android.util.Log
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory

class InAppReview(private val activity: Activity) {
    private val manager: ReviewManager = ReviewManagerFactory.create(activity)
    private lateinit var reviewInfo: ReviewInfo

    fun startRequest() {
        val requestFlow = manager.requestReviewFlow()
        requestFlow.addOnCompleteListener { request ->
            if (request.isSuccessful) {
                reviewInfo = request.result
                launchFlow()
            } else {
                val exception = request.exception
                Log.e("In-AppReview-Excep", exception.toString())
            }
        }
    }

    private fun launchFlow() {
        val flow = manager.launchReviewFlow(activity, reviewInfo)
        flow.addOnCompleteListener { _ ->
            // The flow has finished. The API does not indicate whether the user
            // reviewed or not, or even whether the review dialog was shown. Thus, no
            // matter the result, we continue our app flow.
        }
    }

}