package dev.yuyuyuyuyu.howoldami.ui.openSourceLicenseList

import androidx.compose.runtime.Composable
import com.slack.circuit.runtime.presenter.Presenter

class OpenSourceLicenseListPresenter : Presenter<OpenSourceLicenseListScreen.State> {
    @Composable
    override fun present(): OpenSourceLicenseListScreen.State {
        return OpenSourceLicenseListScreen.State
    }
}
