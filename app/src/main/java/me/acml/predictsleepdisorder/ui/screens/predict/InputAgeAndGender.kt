package me.acml.predictsleepdisorder.ui.screens.predict

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.acml.predictsleepdisorder.R
import me.acml.predictsleepdisorder.ui.components.GenderPicker
import me.acml.predictsleepdisorder.ui.components.HorizontalWheel
import me.acml.predictsleepdisorder.ui.theme.PredictSleepDisorderTheme
import me.acml.predictsleepdisorder.ui.theme.backgroundPrimary
import me.acml.predictsleepdisorder.ui.theme.foregroundPrimary


@Composable
fun InputAgeAndGender(
    age: Int,
    onAgeChange: (Int) -> Unit,
    gender: String?,
    onGenderChange: (String) -> Unit = {},
    onNext: () -> Unit = {}
) {
    val ageRanges = (27..59).map { it.toFloat() }.toList()
    val validateField = rememberFieldValidator()

    fun nextStep() {
        validateField(gender) {
            onNext()
        }
    }
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .then(
                Modifier.padding(horizontal = 16.dp)
            )
    ) {
        Text(
            stringResource(R.string.enter_your_age),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(bottom = 30.dp)
                .fillMaxWidth(),
            style = PredictSleepDisorderTheme.typography.titleLarge.copy(
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 45.sp,
                lineHeight = 50.sp,
            )
        )
        HorizontalWheel(
            value = age.toFloat(),
            onValueChange = {
                onAgeChange(it.toInt())
            },
            ranges = ageRanges,
        )
        Text(
            stringResource(R.string.years),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            color = Color.White.copy(alpha = 0.7f),
            style = PredictSleepDisorderTheme.typography.titleSmall
        )
        Spacer(modifier = Modifier.height(30.dp))
        GenderPicker(
            selectedGender = gender,
            onSelectionChange = onGenderChange,
        )
        Spacer(modifier = Modifier.height(40.dp))
        Button(
            onClick = {
                nextStep()
            },
            modifier = Modifier.align(Alignment.End),
            colors = ButtonDefaults.buttonColors(
                containerColor = foregroundPrimary,
                contentColor = backgroundPrimary
            ),
            contentPadding = ButtonDefaults.ButtonWithIconContentPadding
        ) {
            Text(
                stringResource(R.string.next_step),
            )
            Spacer(Modifier.width(8.dp))
            Icon(
                Icons.AutoMirrored.Filled.ArrowForward,
                modifier = Modifier.size(ButtonDefaults.IconSize),
                contentDescription = stringResource(R.string.next_step)
            )
        }

    }
}

@Composable
@Preview
private fun StepOnePreview() {
    InputAgeAndGender(
        age = 30,
        onAgeChange = {},
        gender = "Male",
        onGenderChange = {}
    )
}