package dev.yuyuyuyuyu.howoldami.ui.thirdpartylicenses.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.text.style.TextDecoration
import dev.yuyuyuyuyu.howoldami.data.types.ThirdPartyLicense

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
                headlineContent = { Text(thirdPartyLicense.libraryName) },
                supportingContent = {
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

            HorizontalDivider()
        }
    }
}
