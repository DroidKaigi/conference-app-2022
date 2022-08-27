package io.github.droidkaigi.confsched2022.feature.contributors

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import io.github.droidkaigi.confsched2022.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched2022.feature.contributors.ContributorsUiModel.ContributorsState.Loaded

@Composable
fun ContributorsScreenRoot(modifier: Modifier = Modifier) {
    val viewModel = hiltViewModel<ContributorsViewModel>()
    val uiModel by viewModel.uiModel
    Contributors(uiModel, modifier)
}

@Composable
fun Contributors(
    uiModel: ContributorsUiModel,
    modifier: Modifier = Modifier,
) {
    if (uiModel.contributorsState !is Loaded) {
        CircularProgressIndicator()
        return
    }
    val contributors = uiModel.contributorsState.contributors

    LazyColumn {
        items(items = contributors, key = { it.id }) { contributor ->
            Text(contributor.name)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ContributorsPreview() {
    KaigiTheme {
        ContributorsScreenRoot()
    }
}
