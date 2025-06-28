package dev.yuyuyuyuyu.howoldami.di

import com.slack.circuit.foundation.Circuit
import dev.yuyuyuyuyu.howoldami.ui.howOldAmI.HowOldAmI
import dev.yuyuyuyuyu.howoldami.ui.howOldAmI.HowOldAmIPresenter
import dev.yuyuyuyuyu.howoldami.ui.howOldAmI.HowOldAmIScreen
import dev.yuyuyuyuyu.howoldami.ui.openSourceLicenseList.OpenSourceLicenseList
import dev.yuyuyuyuyu.howoldami.ui.openSourceLicenseList.OpenSourceLicenseListPresenter
import dev.yuyuyuyuyu.howoldami.ui.openSourceLicenseList.OpenSourceLicenseListScreen
import org.koin.dsl.module

val uiModule = module {
    single {
        Circuit.Builder()
            .addUi<HowOldAmIScreen, HowOldAmIScreen.State> { _, modifier ->
                HowOldAmI(modifier = modifier)
            }
            .addPresenter<HowOldAmIScreen, HowOldAmIScreen.State>(HowOldAmIPresenter())

            .addUi<OpenSourceLicenseListScreen, OpenSourceLicenseListScreen.State> { _, modifier ->
                OpenSourceLicenseList(modifier = modifier)
            }
            .addPresenter<OpenSourceLicenseListScreen, OpenSourceLicenseListScreen.State>(OpenSourceLicenseListPresenter())

            .build()
    }
}
