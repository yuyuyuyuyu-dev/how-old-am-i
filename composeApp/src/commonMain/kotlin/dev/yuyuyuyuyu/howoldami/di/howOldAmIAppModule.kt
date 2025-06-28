package dev.yuyuyuyuyu.howoldami.di

import org.koin.dsl.module

val howOldAmIAppModule = module {
    includes(uiModule, domainModule)
}
