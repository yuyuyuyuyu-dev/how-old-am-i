package dev.yuyuyuyuyu.howoldami.di

import dev.yuyuyuyuyu.howoldami.domain.useCases.CalculateAgeUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val domainModule = module {
    singleOf(::CalculateAgeUseCase)
}
