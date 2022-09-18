package io.github.droidkaigi.confsched2022.feature.about

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Train
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.compose.stringResource
import io.github.droidkaigi.confsched2022.designsystem.components.KaigiScaffold
import io.github.droidkaigi.confsched2022.designsystem.components.KaigiTopAppBar
import io.github.droidkaigi.confsched2022.designsystem.theme.KaigiColors
import io.github.droidkaigi.confsched2022.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched2022.strings.Strings

@Composable
fun AboutScreenRoot(
    modifier: Modifier = Modifier,
    showNavigationIcon: Boolean = true,
    onNavigationIconClick: () -> Unit = {},
    onLinkClick: (url: String, packageName: String?) -> Unit = { _, _ -> },
    onStaffListClick: () -> Unit = {},
    versionName: String? = versionName(LocalContext.current)
) {
    About(
        showNavigationIcon,
        onNavigationIconClick,
        onLinkClick,
        onStaffListClick,
        modifier,
        versionName
    )
}

@Composable
fun About(
    showNavigationIcon: Boolean,
    onNavigationIconClick: () -> Unit,
    onLinkClick: (url: String, packageName: String?) -> Unit,
    onStaffListClick: () -> Unit,
    modifier: Modifier = Modifier,
    versionName: String?
) {
    KaigiScaffold(
        topBar = {
            KaigiTopAppBar(
                showNavigationIcon = showNavigationIcon,
                onNavigationIconClick = onNavigationIconClick,
                title = {
                    Text(
                        text = stringResource(Strings.about_top_app_bar_title),
                    )
                },
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = 67.dp,
                        bottom = 75.dp,
                    )
                    .clearAndSetSemantics {
                        contentDescription = "Logo"
                        role = Role.Image
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(40.dp)
            ) {
                Image(
                    modifier = Modifier,
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_2022_logo),
                    contentDescription = "Logo",
                )
                Image(
                    modifier = Modifier,
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_2022_mascot),
                    contentDescription = "Logo",
                )
            }
            Column(
                modifier = Modifier
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 14.dp,
                    ),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Text(
                    style = MaterialTheme.typography.headlineLarge,
                    text = stringResource(Strings.about_title)
                )
                Text(
                    style = TextStyle(
                        fontSize = 16.sp
                    ),
                    text = stringResource(Strings.about_description)
                )
            }

            Row(modifier = Modifier.padding(start = 4.dp, bottom = 22.dp)) {
                ExternalServices.values().forEach { serviceType ->
                    ExternalServiceImage(
                        serviceType = serviceType
                    ) {
                        onLinkClick(serviceType.url, serviceType.packageName)
                    }
                }
            }
            Divider(
                modifier = Modifier
                    .padding(
                        start = 33.dp,
                        end = 33.dp
                    ),
                color = Color(KaigiColors.neutralVariantKeyColor50)
            )
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                val googleMapUrl = "https://goo.gl/maps/NnqJr2zUVdrAJseH7"
                val codeConductUrl = "https://portal.droidkaigi.jp/about/code-of-conduct"
                val context = LocalContext.current
                AuxiliaryInformationRow(
                    imageVector = Icons.Outlined.Train,
                    textRes = Strings.about_access,
                    onClick = {
                        onLinkClick(googleMapUrl, null)
                    }
                )

                AuxiliaryInformationRow(
                    imageVector = Icons.Outlined.Person,
                    textRes = Strings.about_staff,
                    onClick = onStaffListClick
                )

                AuxiliaryInformationRow(
                    imageVector = Icons.Filled.Folder,
                    textRes = Strings.about_license,
                    onClick = {
                        // TODO: Implement license
                    }
                )

                AuxiliaryInformationRow(
                    imageVector = Icons.Filled.DirectionsWalk,
                    textRes = Strings.about_code_conduct,
                    onClick = {
                        CustomTabsIntent.Builder().also { builder ->
                            builder.setShowTitle(true)
                            builder.build().also {
                                it.launchUrl(context, Uri.parse(codeConductUrl))
                            }
                        }
                    }
                )

                val privacyPolicyUrl = "https://portal.droidkaigi.jp/about/privacy"
                AuxiliaryInformationRow(
                    imageVector = Icons.Filled.PrivacyTip,
                    textRes = Strings.about_privacy,
                    onClick = {
                        onLinkClick(privacyPolicyUrl, null)
                    }
                )
            }

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(vertical = 18.dp, horizontal = 32.dp)
                    .fillMaxWidth()
                    .semantics(mergeDescendants = true) {}
            ) {
                Text(
                    text = stringResource(Strings.about_app_version),
                    style = MaterialTheme.typography.bodyMedium
                )
                if (versionName != null) {
                    Text(
                        text = versionName,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}

@Composable
private fun AuxiliaryInformationRow(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    textRes: StringResource,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.padding(start = 20.dp),
            imageVector = imageVector,
            contentDescription = null
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            text = stringResource(textRes)
        )
    }
}

@Composable
private fun ExternalServiceImage(
    serviceType: ExternalServices,
    onClick: () -> Unit,
) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = ImageVector.vectorResource(id = serviceType.iconRes),
            contentDescription = serviceType.contentDescription,
            tint = Color.Unspecified,
        )
    }
}

private fun versionName(context: Context) = runCatching {
    context.packageManager
        .getPackageInfo(
            context.packageName,
            0
        ).versionName
}.getOrNull()

@Preview(showBackground = true)
@Composable
fun AboutPreview() {
    KaigiTheme {
        AboutScreenRoot(versionName = "1.2.3")
    }
}
