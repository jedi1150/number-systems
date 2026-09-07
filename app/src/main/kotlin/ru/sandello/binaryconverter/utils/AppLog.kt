package ru.sandello.binaryconverter.utils

import android.util.Log
import ru.sandello.binaryconverter.BuildConfig

internal object AppLog {
    var delegate: Delegate = Android

    fun d(tag: String, message: String) {
        delegate.d(tag, message)
    }

    fun w(tag: String, message: String) {
        delegate.w(tag, message)
    }

    fun e(tag: String, message: String, throwable: Throwable? = null) {
        delegate.e(tag, message, throwable)
    }

    interface Delegate {
        fun d(tag: String, message: String)

        fun w(tag: String, message: String)

        fun e(tag: String, message: String, throwable: Throwable?)
    }

    object Android : Delegate {
        override fun d(tag: String, message: String) {
            if (BuildConfig.DEBUG) Log.d(tag, message)
        }

        override fun w(tag: String, message: String) {
            Log.w(tag, message)
        }

        override fun e(tag: String, message: String, throwable: Throwable?) {
            if (throwable != null) {
                Log.e(tag, message, throwable)
            } else {
                Log.e(tag, message)
            }
        }
    }

    object NoOp : Delegate {
        override fun d(tag: String, message: String) = Unit

        override fun w(tag: String, message: String) = Unit

        override fun e(tag: String, message: String, throwable: Throwable?) = Unit
    }
}
