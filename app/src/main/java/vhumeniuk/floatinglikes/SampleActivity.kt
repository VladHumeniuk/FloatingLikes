package vhumeniuk.floatinglikes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class SampleActivity : AppCompatActivity() {

    lateinit var likeView: LikeView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        likeView = LikeView(findViewById(R.id.textView))
        likeView.setup()
    }
}