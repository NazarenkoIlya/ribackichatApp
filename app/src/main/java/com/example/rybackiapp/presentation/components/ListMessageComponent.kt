package com.example.rybackiapp.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rybackiapp.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListMessageComponentView(
    message: String = "",
    exitBottomSheet: () -> Unit = {},
    applyBottomSheet: () -> Unit = {},
    applyListMessage: (String) -> Unit = {},
) {
    val (initialTitle, initialList) = remember(message) {
        message.fromMessageString()
    }
    val circlePlusIcon = painterResource(id = R.drawable.ic_plus_circle)
    val crossCircleIcon = painterResource(id = R.drawable.ic_cross_circle)
    var title by rememberSaveable { mutableStateOf(initialTitle) }
    val items = rememberSaveable {
        mutableStateListOf(*initialList.toTypedArray())
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {

        OutlinedTextField(
            value = title,
            onValueChange = { newValue ->
                title = newValue
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
                .background(
                    Color.White,
                    shape = RoundedCornerShape(16.dp)
                ),
            shape = RoundedCornerShape(16.dp),
            placeholder = {
                Text("Promising part")
            },
        )
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier.heightIn(max = 500.dp)
        ) {
            itemsIndexed(items = items) { index, item ->

                OutlinedTextField(
                    value = items[index],
                    onValueChange = { newValue ->
                        items[index] = newValue
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 12.dp)
                        .background(
                            Color.White,
                            shape = RoundedCornerShape(16.dp)
                        ),
                    shape = RoundedCornerShape(16.dp),
                    placeholder = { Text("Item") },
                    prefix = { Text((index + 1).toString() + ".") },
                    trailingIcon = {
                        Row {
                            TooltipBoxComponent(
                                text = "Add item",
                                content = {
                                    IconButton(onClick = { items.add(index+1, "") }) {
                                        Icon(
                                            circlePlusIcon,
                                            contentDescription = "Add List Message"
                                        )
                                    }
                                }
                            )
                            if (index != 0) {
                                TooltipBoxComponent(
                                    text = "Delete item",
                                    content = {
                                        IconButton(onClick = { items.removeAt(index) }) {
                                            Icon(
                                                crossCircleIcon,
                                                contentDescription = "Add List Message"
                                            )
                                        }
                                    }
                                )
                            }

                        }
                    }
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = { exitBottomSheet() },
                modifier = Modifier.weight(0.5f),
            ) {
                Text("Cancel")
            }
            OutlinedButton(
                onClick = {
                    applyBottomSheet.invoke()
                    applyListMessage(items.toMessageString(title))
                },
                modifier = Modifier.weight(0.5f),
            ) {
                Text("Apply")
            }
        }
    }
}

fun <T> List<T>.toMessageString(text: String, startFrom: Int = 1): String {
    return "\u2060$text:\u2060\n" + this.mapIndexed { index, item ->
        "${index + startFrom}.\u2063 $item\u2063"
    }.joinToString("\n")
}

fun String.fromMessageString(): Pair<String, List<String>> {
    if (this.isBlank()) {
        return "" to listOf("")
    }

    if (!this.contains("\u2060")) {
        val lines = this.lines()
        val title = lines.firstOrNull()?.removeSuffix(":") ?: ""
        val items = lines.drop(1).map { line ->
            val regex = """(\d+)\. (.*)""".toRegex()
            regex.find(line)?.groupValues?.get(2) ?: line
        }.ifEmpty { listOf("") }
        return title to items
    }

    val withoutFirstMarker = this.removePrefix("\u2060")
    val parts = withoutFirstMarker.split("\u2060\n")


    val title = parts.getOrNull(0)?.dropLast(1)?.takeIf { it.isNotBlank() } ?: ""


    val itemsPart = parts.getOrNull(1) ?: ""
    val items = if (itemsPart.isBlank()) {
        listOf("")
    } else {
        itemsPart.split("\n").mapNotNull { line ->
            val regex = """(\d+)\.\u2063 (.*)\u2063""".toRegex()
            val match = regex.find(line)
            when {
                match != null -> match.groupValues.getOrNull(2)
                line.isNotBlank() -> line
                else -> null
            }
        }.ifEmpty { listOf("") }
    }

    return title to items
}

@Composable
@Preview(showBackground = true)
fun ListMessageComponentPreview() {
    ListMessageComponentView()
}