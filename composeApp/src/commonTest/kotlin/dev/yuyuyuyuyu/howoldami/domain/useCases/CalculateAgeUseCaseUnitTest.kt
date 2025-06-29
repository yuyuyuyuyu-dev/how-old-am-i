package dev.yuyuyuyuyu.howoldami.domain.useCases

import kotlin.test.Test

class CalculateAgeUseCaseUnitTest {
    private val useCase = CalculateAgeUseCase()

    @Test
    fun `invoke() should return the correct age`() {
        val actual = useCase.invoke(
            year = 1970,
            month = 1,
            day = 1,
        )

        println("age: $actual")
    }
}
