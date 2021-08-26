package vhumeniuk.floatinglikes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class SampleActivity : AppCompatActivity() {

    lateinit var likeView: LikeView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        likeView = LikeView(findViewById(R.id.textView))
        likeView.setup(emoji = "\uD83E\uDD0D",
            delay = 2000L,
            randomize = false,
            size = 20f,
            translateDuration = 1500L,
            floatHeight = 1000)
    }
}