package dev.yuyuyuyuyu.howoldami.di

import dev.yuyuyuyuyu.howoldami.ui.howOldAmI.HowOldAmIViewModel
import dev.yuyuyuyuyu.howoldami.ui.howOldAmI.HowOldAmIViewModelImpl
import kotlinx.datetime.TimeZone
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.KmpComponentCreate
import me.tatarka.inject.annotations.Provides
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Component
abstract class AppComponent(
    @get:Provides val clock: Clock,
    @get:Provides val timeZone: TimeZone,
) {
    abstract val howOldAmIViewModel: HowOldAmIViewModelImpl

    @Provides
    fun provideHowOldAmIViewModel(impl: HowOldAmIViewModelImpl): HowOldAmIViewModel = impl
}

@OptIn(ExperimentalTime::class)
@KmpComponentCreate
expect fun createAppComponent(
    clock: Clock,
    timeZone: TimeZone,
): AppComponent
