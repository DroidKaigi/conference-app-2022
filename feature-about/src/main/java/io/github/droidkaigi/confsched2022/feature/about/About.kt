package io.github.droidkaigi.confsched2022.feature.about

import android.content.Context
import androidx.annotation.StringRes
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Train
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
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
import io.github.droidkaigi.confsched2022.designsystem.theme.KaigiColors
import io.github.droidkaigi.confsched2022.designsystem.theme.KaigiScaffold
import io.github.droidkaigi.confsched2022.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched2022.designsystem.theme.KaigiTopAppBar
import io.github.droidkaigi.confsched2022.feature.about.R.string

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
                        text = stringResource(id = string.about_top_app_bar_title),
                        style = MaterialTheme.typography.titleLarge,
                    )
                },
            )
        }
    ) {
        Column(
            modifier = modifier.verticalScroll(rememberScrollState())
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
                    text = stringResource(id = string.about_title)
                )
                Text(
                    style = TextStyle(
                        fontSize = 16.sp
                    ),
                    text = stringResource(id = string.about_description)
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
                val context = LocalContext.current
                val googleMapUrl = "https://goo.gl/maps/NnqJr2zUVdrAJseH7"
                AuxiliaryInformationRow(
                    imageVector = Icons.Outlined.Train,
                    textResId = string.about_access,
                    onClick = {
                        onLinkClick(googleMapUrl, null)
                    }
                )

                AuxiliaryInformationRow(
                    imageVector = Icons.Outlined.Person,
                    textResId = string.about_staff,
                    onClick = onStaffListClick
                )

                AuxiliaryInformationRow(
                    imageVector = Icons.Filled.PrivacyTip,
                    textResId = string.about_privacy,
                    onClick = {
                        // TODO: Implement privacy policy
                    }
                )

                AuxiliaryInformationRow(
                    imageVector = Icons.Filled.Folder,
                    textResId = string.about_license,
                    onClick = {
                        // TODO: Implement license
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
                    text = "アプリバージョン",
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
    @StringRes textResId: Int,
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
            text = stringResource(id = textResId)
        )
    }
}

@Composable
private fun ExternalServiceImage(
    serviceType: ExternalServices,
    onClick: () -> Unit,
) {
    Image(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(12.dp)
            .size(24.dp),
        imageVector = ImageVector.vectorResource(id = serviceType.iconRes),
        contentDescription = serviceType.contentDescription,
    )
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
