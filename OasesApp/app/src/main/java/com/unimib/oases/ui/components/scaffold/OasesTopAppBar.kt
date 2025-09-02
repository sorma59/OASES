package com.unimib.oases.ui.components.scaffold

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.unimib.oases.R
import com.unimib.oases.ui.navigation.Screen
import com.unimib.oases.ui.util.debounce

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OasesTopAppBar(
    screen: Screen,
    onNavigationIconClick: () -> Unit,
    actions: @Composable RowScope.() -> Unit = {}
){

    val handleIconButtonClick =
        if (screen.type == OasesTopAppBarType.BACK) {
            {onNavigationIconClick()}.debounce()
        } else {
            onNavigationIconClick
        }

    CenterAlignedTopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ){
                if(screen.type.showLogo)
                    Image(
                        painter = painterResource(R.drawable.ic_launcher_round),
                        contentDescription = "Icon",
                        modifier = Modifier
                            .size(50.dp)
                            .padding(5.dp)
                    )

                Text(
                    text = screen.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.onPrimary,
            scrolledContainerColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
            titleContentColor = MaterialTheme.colorScheme.onBackground,
            actionIconContentColor = MaterialTheme.colorScheme.onBackground,
        ),
        navigationIcon = {
            IconButton(
                onClick = handleIconButtonClick
            ) {
                Icon(
                    imageVector = screen.type.icon,
                    contentDescription = screen.type.contentDescription
                )
            }
        },
        actions = actions,
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    )

}

enum class OasesTopAppBarType(val icon: ImageVector, val contentDescription: String, val showLogo: Boolean = false){
    MENU(Icons.Default.Menu, "Menu", true),
    BACK(Icons.Default.ArrowBackIosNew, "Go Back")
}