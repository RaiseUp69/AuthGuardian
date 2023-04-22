package com.authguardian.app.extension

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavGraph

object NavigationUtils {

    fun navigate(navController: NavController, direction: NavDirections) {
        navController.currentDestination?.run {
            getAction(direction.actionId)?.let { navAction ->
                val destinationId = navAction.destinationId
                val currentNode: NavGraph? = if (this is NavGraph) this else parent
                if (destinationId != 0 && currentNode?.findNode(destinationId) != null) {
                    navController.navigate(direction)
                }
            }
        }
    }

    fun navigate(navController: NavController, @IdRes resId: Int, args: Bundle?) {
        navController.currentDestination?.run {
            getAction(resId)?.let { navAction ->
                val destinationId = navAction.destinationId
                val currentNode: NavGraph? = if (this is NavGraph) this else parent
                if (destinationId != 0 && currentNode?.findNode(destinationId) != null) {
                    navController.navigate(resId, args)
                }
            }
        }
    }
}