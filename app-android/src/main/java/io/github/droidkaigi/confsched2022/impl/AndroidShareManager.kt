package io.github.droidkaigi.confsched2022.impl

import android.content.Context
import androidx.core.app.ShareCompat
import io.github.droidkaigi.confsched2022.ui.ShareManager

class AndroidShareManager(private val context: Context) : ShareManager {
    override fun share(text: String) {
        ShareCompat.IntentBuilder(context)
            .setText(text)
            .setType("text/plain")
            .startChooser()
    }
}
