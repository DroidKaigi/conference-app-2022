package io.github.droidkaigi.confsched2022.impl

import android.content.ActivityNotFoundException
import android.content.Context
import android.util.Log
import androidx.core.app.ShareCompat
import io.github.droidkaigi.confsched2022.ui.ShareManager

class AndroidShareManager(private val context: Context) : ShareManager {
    override fun share(text: String) {
        try {
            ShareCompat.IntentBuilder(context)
                .setText(text)
                .setType("text/plain")
                .startChooser()
        } catch (e: ActivityNotFoundException) {
            Log.e("ActivityNotFoundException", "Fail startActivity", e)
        }
    }
}
