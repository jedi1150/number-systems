package ru.sandello.binaryconverter.repository

import android.app.Activity
import android.content.Context
import com.google.android.play.core.review.ReviewException
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.review.model.ReviewErrorCode
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ReviewManager @Inject constructor(@ApplicationContext context: Context) {
    private val manager = ReviewManagerFactory.create(context)

    fun requestReview(activity: Activity) {
        val request = manager.requestReviewFlow()
        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                manager.launchReviewFlow(activity, task.result)
            } else {
                @ReviewErrorCode val reviewErrorCode = (task.exception as ReviewException).errorCode
                Firebase.crashlytics.log("requestReview error: $reviewErrorCode")
            }
        }
    }

}
