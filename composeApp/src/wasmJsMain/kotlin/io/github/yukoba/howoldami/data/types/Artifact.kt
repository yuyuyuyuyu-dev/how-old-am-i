package io.github.yukoba.howoldami.data.types

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class Artifact(
    val name: String,
    val spdxLicenses: List<License>? = null,
    val unknownLicenses: List<License>? = null,
    val scm: Scm,
)

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class License(
    val name: String,
    val url: String,
)

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class Scm(
    val url: String,
)