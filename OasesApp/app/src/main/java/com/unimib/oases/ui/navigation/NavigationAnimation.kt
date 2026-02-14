package com.unimib.oases.ui.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically

sealed interface NavigationAnimation {
    val enter: EnterTransition
    val exit: ExitTransition
    val popEnter: EnterTransition
    val popExit: ExitTransition

    data object Push: NavigationAnimation {
        override val enter = slideInHorizontally(initialOffsetX = { it }) + fadeIn()
        override val exit = slideOutHorizontally(targetOffsetX = { -it / 3 }) + fadeOut()
        override val popEnter = slideInHorizontally(initialOffsetX = { -it / 3 }) + fadeIn()
        override val popExit = slideOutHorizontally(targetOffsetX = { it }) + fadeOut()
    }

    data object Modal: NavigationAnimation {
        override val enter = slideInVertically(initialOffsetY = { it }) + fadeIn()
        override val exit = fadeOut()
        override val popEnter = fadeIn()
        override val popExit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
    }
}