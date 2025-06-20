package com.botsi.example

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import androidx.fragment.app.Fragment
import com.botsi.Botsi

class BotsiAppSetupFragment : Fragment() {

    companion object {
        fun newInstance() = BotsiAppSetupFragment()
    }

    private val app: BotsiApp
        get() = requireContext().applicationContext as BotsiApp

    private val storage: BotsiConfigsStorage
        get() = app.botsiStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return ComposeView(context = requireContext()).apply {
            setContent {
                var appKey by remember { mutableStateOf(TextFieldValue(storage.appKey)) }
                var placementId by remember { mutableStateOf(TextFieldValue(storage.placementId)) }
                var paywallId by remember { mutableStateOf(TextFieldValue(storage.paywallId.toString())) }

                var startDestination by remember {
                    mutableStateOf(BotsiFragments.Classic)
                }

                MaterialTheme {
                    Scaffold(
                        modifier = Modifier.navigationBarsPadding(),
                        containerColor = Color.White,
                        topBar = {
                            TopAppBar(
                                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                    containerColor = Color.White
                                ),
                                title = { Text(text = "Setup Botsi App") },
                            )
                        },
                        bottomBar = {
                            Button(
                                modifier = Modifier
                                    .padding(
                                        horizontal = 24.dp,
                                        vertical = 8.dp
                                    )
                                    .fillMaxWidth(),
                                contentPadding = PaddingValues(vertical = 16.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xffFF9542), disabledContainerColor = Color(0xFFFFCDA6)
                                ),
                                onClick = {
                                    storage.appKey = appKey.text
                                    storage.placementId = placementId.text
                                    storage.paywallId = paywallId.text.toLong()

                                    Botsi.activate(
                                        context = requireContext(),
                                        apiKey = app.botsiStorage.appKey,
                                        clearCache = true,
                                    )

                                    (requireActivity() as MainActivity).addFragment(
                                        when (startDestination) {
                                            BotsiFragments.Ui -> BotsiViewFragment.newInstance()
                                            BotsiFragments.Classic -> BotsiFragment.newInstance()
                                        },
                                        true,
                                        true
                                    )
                                },
                            ) {
                                Text(
                                    text = "Start",
                                    style = TextStyle(
                                        fontWeight = FontWeight.Medium
                                    ),
                                    color = Color.White,
                                    fontSize = 16.sp,
                                )
                            }
                        }
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(it)
                                .padding(16.dp)
                        ) {

                            TextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        horizontal = 16.dp,
                                    ),
                                onValueChange = {
                                    appKey = it
                                },
                                label = {
                                    Text(
                                        text = "App key",
                                        style = TextStyle(
                                            fontWeight = FontWeight.Medium
                                        ),
                                    )
                                },
                                value = appKey,
                            )
                            TextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        horizontal = 16.dp,
                                        vertical = 16.dp
                                    ),
                                onValueChange = {
                                    placementId = it
                                },
                                label = {
                                    Text(
                                        text = "Placement id",
                                        style = TextStyle(
                                            fontWeight = FontWeight.Medium
                                        ),
                                    )
                                },
                                value = placementId,
                            )
                            TextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        horizontal = 16.dp,
                                        vertical = 16.dp
                                    ),
                                onValueChange = {
                                    paywallId = it
                                },
                                label = {
                                    Text(
                                        text = "Paywall id",
                                        style = TextStyle(
                                            fontWeight = FontWeight.Medium
                                        ),
                                    )
                                },
                                value = paywallId,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number
                                )
                            )

                            var isDropdownExpanded by remember {
                                mutableStateOf(false)
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    modifier = Modifier
                                        .padding(
                                            vertical = 4.dp,
                                            horizontal = 8.dp,
                                        )
                                        .weight(1f)
                                        .border(
                                            color = Color.Gray,
                                            width = 1.dp,
                                            shape = RoundedCornerShape(4.dp)
                                        )
                                        .padding(start = 8.dp)
                                        .padding(vertical = 4.dp),
                                    text = startDestination.name,
                                )
                                DropdownMenu(
                                    expanded = isDropdownExpanded,
                                    properties = PopupProperties(
                                        dismissOnBackPress = true,
                                        dismissOnClickOutside = true
                                    ),
                                    containerColor = Color.White,
                                    onDismissRequest = { isDropdownExpanded = false },
                                ) {
                                    DropdownMenuItem(
                                        text = { Text(text = "Botsi classic") },
                                        onClick = {
                                            isDropdownExpanded = false
                                            startDestination = BotsiFragments.Classic
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text(text = "Botsi ui") },
                                        onClick = {
                                            isDropdownExpanded = false
                                            startDestination = BotsiFragments.Ui
                                        }
                                    )
                                }

                                Button(
                                    modifier = Modifier.height(28.dp),
                                    contentPadding = PaddingValues(vertical = 0.dp, horizontal = 24.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xffFF9542),
                                        disabledContainerColor = Color(0xFFFFCDA6)
                                    ),
                                    onClick = { isDropdownExpanded = !isDropdownExpanded },
                                ) {
                                    Text(
                                        text = "Choose",
                                        style = TextStyle(
                                            fontWeight = FontWeight.Medium
                                        ),
                                        color = Color.White,
                                        fontSize = 12.sp,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}