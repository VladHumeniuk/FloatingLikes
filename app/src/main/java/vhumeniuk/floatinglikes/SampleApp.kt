package vhumeniuk.floatinglikes

import android.app.Application
import android.util.Log
import androidx.core.provider.FontRequest
import androidx.emoji.text.EmojiCompat
import androidx.emoji.text.FontRequestEmojiCompatConfig

class SampleApp: Application() {

    override fun onCreate() {
        super.onCreate()
//        initEmojiSupport()
    }

    private fun initEmojiSupport() {
        val fontRequest = FontRequest(
            "com.google.android.gms.fonts",
            "com.google.android.gms",
            "Noto Color Emoji Compat",
            R.array.com_google_android_gms_fonts_certs)

        val config = FontRequestEmojiCompatConfig(applicationContext, fontRequest)
            .setReplaceAll(true)
            .registerInitCallback(object : EmojiCompat.InitCallback() {
                override fun onInitialized() {
                    Log.d("EmojiCompat", "Initialized")
                }
                override fun onFailed(throwable: Throwable?) {
                    Log.d("EmojiCompat", "Failed to initialize")
                }
            })
        EmojiCompat.init(config)
    }
}