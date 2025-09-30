package com.unimib.oases.domain.usecase

import javax.inject.Inject

class ConvertNodeAnswerToStringUseCase @Inject constructor() {

    operator fun invoke(answer: Boolean): String {
        return when (answer) {
            true -> "Yes"
            false -> "No"
        }
    }

}