package io.github.yukoba.howoldami.ui.features.thirdpartylicenses.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.text.style.TextDecoration
import io.github.yukoba.howoldami.ui.features.thirdpartylicenses.types.ThirdPartyLicense

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ThirdPartyLicensesScreen(
    thirdPartyLicenses: List<ThirdPartyLicense>,
    modifier: Modifier = Modifier,
    uriHandler: UriHandler,
) {
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        for (thirdPartyLicense in thirdPartyLicenses) {
            ListItem(
                text = { Text(thirdPartyLicense.libraryName) },
                secondaryText = {
                    Column {
                        Text("")

                        thirdPartyLicense.website?.let {
                            Text("Website")
                            Text(
                                text = it,
                                textDecoration = TextDecoration.Underline,
                                modifier = Modifier.clickable {
                                    uriHandler.openUri(it)
                                }
                            )
                        }

                        Text("")

                        Text("License: ${thirdPartyLicense.licenseName}")
                        Text(
                            text = thirdPartyLicense.licenseUrl,
                            textDecoration = TextDecoration.Underline,
                            modifier = Modifier.clickable {
                                uriHandler.openUri(thirdPartyLicense.licenseUrl)
                            }
                        )
                    }
                },
            )

            Divider()
        }
    }
}