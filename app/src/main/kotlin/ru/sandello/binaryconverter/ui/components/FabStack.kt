package ru.sandello.binaryconverter.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.sandello.binaryconverter.R
import ru.sandello.binaryconverter.ui.theme.NumberSystemsTheme

@Composable
fun FabStack(
    modifier: Modifier = Modifier,
    isClearFabVisible: Boolean,
    isExplanationFabVisible: Boolean,
    onClearClicked: () -> Unit,
    onExplanationClicked: () -> Unit,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End,
    ) {
        AnimatedVisibility(
            visible = isClearFabVisible,
            enter = scaleIn(transformOrigin = TransformOrigin(0f, 0f)) + fadeIn() + expandIn(expandFrom = Alignment.TopStart),
            exit = scaleOut(transformOrigin = TransformOrigin(0f, 0f)) + fadeOut() + shrinkOut(shrinkTowards = Alignment.TopStart),
        ) {
            SmallFloatingActionButton(
                onClick = onClearClicked,
                modifier = Modifier.padding(vertical = 8.dp),
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 0.dp),
            ) {
                Icon(painter = painterResource(R.drawable.ic_close), contentDescription = stringResource(R.string.clear_values))
            }
        }

        AnimatedVisibility(
            visible = isExplanationFabVisible,
            enter = scaleIn(transformOrigin = TransformOrigin(0f, 0f)) + fadeIn() + expandIn(expandFrom = Alignment.TopStart),
            exit = scaleOut(transformOrigin = TransformOrigin(0f, 0f)) + fadeOut() + shrinkOut(shrinkTowards = Alignment.TopStart),
        ) {
            ExtendedFloatingActionButton(
                text = { Text(text = stringResource(id = R.string.explanation), fontWeight = FontWeight.Normal) },
                icon = { Icon(painter = painterResource(R.drawable.ic_explanation), contentDescription = stringResource(id = R.string.explanation)) },
                onClick = onExplanationClicked,
                modifier = Modifier.padding(vertical = 8.dp),
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 0.dp),
            )
        }
    }
}

@Preview
@Composable
private fun FabStackPreview() {
    NumberSystemsTheme {
        FabStack(
            isClearFabVisible = true,
            isExplanationFabVisible = true,
            onClearClicked = {},
            onExplanationClicked = {},
        )
    }
}

@Preview
@Composable
private fun FabStackClearOnlyPreview() {
    NumberSystemsTheme {
        FabStack(
            isClearFabVisible = true,
            isExplanationFabVisible = false,
            onClearClicked = {},
            onExplanationClicked = {},
        )
    }
}
