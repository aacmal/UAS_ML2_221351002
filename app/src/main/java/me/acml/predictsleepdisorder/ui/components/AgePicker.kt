package me.acml.predictsleepdisorder.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.abs

@Composable
fun AgePicker(
    value: Int,
    onValueChange: (Int) -> Unit,
) {
    val ageRange = (27..59).toList()
    val state = rememberLazyListState()
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val itemWidthDp = 70.dp
    val padding = (screenWidth - itemWidthDp) / 2

    var isInternalUpdate by remember { mutableStateOf(false) }

    var centerIndex by remember { mutableIntStateOf(ageRange.indexOf(value)) }

    val currentCenterIndex by remember {
        derivedStateOf {
            val layoutInfo = state.layoutInfo
            if (layoutInfo.visibleItemsInfo.isEmpty()) return@derivedStateOf centerIndex

            val center = layoutInfo.viewportStartOffset + (layoutInfo.viewportSize.width / 2)

            layoutInfo.visibleItemsInfo.minByOrNull { item ->
                val itemCenter = item.offset + (item.size / 2)
                abs(itemCenter - center)
            }?.index ?: centerIndex
        }
    }

    LaunchedEffect(currentCenterIndex) {
        centerIndex = currentCenterIndex
    }

    LaunchedEffect(state.isScrollInProgress) {
        if (!state.isScrollInProgress && !isInternalUpdate) {
            val centerAge = ageRange.getOrNull(centerIndex)
            if (centerAge != null && centerAge != value) {
                isInternalUpdate = true
                onValueChange(centerAge)
                isInternalUpdate = false
            }
        }
    }

    // Auto scroll ke selected value hanya ketika value berubah dari external
    LaunchedEffect(value) {
        if (!isInternalUpdate) {
            val index = ageRange.indexOf(value)
            if (index != -1) {
                state.animateScrollToItem(index, scrollOffset = 45)
            }
        }
    }

    Box(
        modifier = Modifier.wrapContentHeight()
    ) {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            state = state,
            flingBehavior = rememberSnapFlingBehavior(
                lazyListState = state,
                snapPosition = SnapPosition.Center
            ),
            contentPadding = PaddingValues(horizontal = padding)
        ) {
            items(ageRange.size) { index ->
                val age = ageRange[index]
                val isCentered = index == centerIndex

                // Animasi scale berdasarkan posisi center real-time
                val scale by animateFloatAsState(
                    targetValue = if (isCentered) 1.5f else 0.8f,
                    animationSpec = tween(300, easing = FastOutSlowInEasing),
                    label = "scale_animation"
                )

                Box(
                    modifier = Modifier
                        .width(itemWidthDp)
                        .scale(scale),
                ) {
                    Text(
                        text = age.toString(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        style = MaterialTheme.typography.headlineMedium,
                        color = if (isCentered)
                            Color.White
                        else
                            Color.White.copy(alpha = 0.6f),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // Center indicator box
        Box(
            modifier = Modifier
                .width(itemWidthDp + 8.dp)
                .height(60.dp)
                .align(Alignment.Center)
                .border(
                    width = 1.5.dp,
                    color = Color.White,
                    shape = RoundedCornerShape(8.dp)
                )
                .background(
                    color = Color.White.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(8.dp)
                )
        )

        // Left gradient overlay
        Box(
            modifier = Modifier
                .width(padding)
                .height(60.dp)
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            Color.Transparent
                        )
                    )
                )
                .align(Alignment.CenterStart)
        )

        // Right gradient overlay
        Box(
            modifier = Modifier
                .width(padding)
                .height(60.dp)
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color.Transparent,
                            MaterialTheme.colorScheme.primary
                        )
                    )
                )
                .align(Alignment.CenterEnd)
        )
    }
}

@Preview
@Composable
private fun AgePickerPreview() {
    AgePicker(
        value = 30,
        onValueChange = {}
    )
}