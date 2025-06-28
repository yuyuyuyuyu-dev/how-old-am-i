package dev.yuyuyuyuyu.howoldami.ui.howOldAmI

import androidx.compose.runtime.Composable
import com.slack.circuit.runtime.presenter.Presenter

class HowOldAmIPresenter : Presenter<HowOldAmIScreen.State> {
    @Composable
    override fun present(): HowOldAmIScreen.State {
        return HowOldAmIScreen.State
    }
}
