package dev.yuyuyuyuyu.howoldami.di

import dev.yuyuyuyuyu.howoldami.ui.howOldAmI.HowOldAmIViewModel
import dev.yuyuyuyuyu.howoldami.ui.howOldAmI.HowOldAmIViewModelImpl
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides

@Component
abstract class AppComponent {
    abstract val howOldAmIViewModel: HowOldAmIViewModelImpl

    @Provides
    fun provideHowOldAmIViewModel(impl: HowOldAmIViewModelImpl): HowOldAmIViewModel = impl
}
