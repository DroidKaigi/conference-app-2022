package io.github.droidkaigi.confsched2022.feature.about

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched2022.designsystem.theme.KaigiScaffold
import io.github.droidkaigi.confsched2022.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched2022.designsystem.theme.KaigiTopAppBar

@Composable
fun AboutScreenRoot(
    modifier: Modifier = Modifier,
    onNavigationIconClick: () -> Unit = {}
) {
    About(onNavigationIconClick, modifier)
}

@Composable
fun About(
    onNavigationIconClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    KaigiScaffold(
        topBar = {
            KaigiTopAppBar(onNavigationIconClick = onNavigationIconClick)
        }
    ) {
        Column(
            modifier = modifier.verticalScroll(rememberScrollState())
        ) {
            val context = LocalContext.current

            Row(
                Modifier.padding(
                    start = 17.dp,
                    top = 24.dp,
                    bottom = 32.dp
                ),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ExternalServiceImage(
                    context = context,
                    serviceType = ExternalServices.Twitter()
                )
                ExternalServiceImage(
                    context = context,
                    serviceType = ExternalServices.YouTube()
                )
                ExternalServiceImage(
                    context = context,
                    serviceType = ExternalServices.Medium()
                )
            }
        }
    }
}

@Composable
private fun ExternalServiceImage(
    context: Context,
    serviceType: ExternalServices,
) {
    Image(
        modifier = Modifier
            .size(24.dp)
            .clickable {
                navigateToExternalServices(
                    context = context,
                    serviceType = serviceType
                )
            },
        imageVector = ImageVector.vectorResource(id = serviceType.iconRes),
        contentDescription = serviceType.contentDescription,
    )
}

private fun navigateToExternalServices(
    context: Context,
    serviceType: ExternalServices,
) {
    try {
        Intent(Intent.ACTION_VIEW).also {
            it.setPackage(serviceType.packageName)
            it.data = Uri.parse(serviceType.url)
            context.startActivity(it)
        }
    } catch (e: ActivityNotFoundException) {
        navigateToCustomTab(
            url = serviceType.url,
            context = context,
        )
    }
}

//Custom Tab
private fun navigateToCustomTab(url: String, context: Context) {
    val uri = Uri.parse(url)
    CustomTabsIntent.Builder().also { builder ->
        builder.setShowTitle(true)
        builder.build().also {
            it.launchUrl(context, uri)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AboutPreview() {
    KaigiTheme {
        AboutScreenRoot()
    }
}
