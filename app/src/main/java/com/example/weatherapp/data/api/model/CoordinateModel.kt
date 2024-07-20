package com.example.weatherapp.data.api.model

import kotlinx.serialization.Serializable

@Serializable
data class Coordinate(
    val response: Response
)

@Serializable
data class Response(
    val GeoObjectCollection: GeoObjectCollection
)

@Serializable
data class GeoObjectCollection(
    val featureMember: List<FeatureMember>
)

@Serializable
data class FeatureMember(
    val GeoObject: GeoObject
)

@Serializable
data class GeoObject(
    val Point: Point,
    val name: String,
    val metaDataProperty: MetaDataProperty
)

@Serializable
data class MetaDataProperty(
    val GeocoderMetaData: GeocoderMetaData
)

@Serializable
data class GeocoderMetaData(
    val text: String
)



@Serializable
data class Locality(
    val LocalityName: String
)

@Serializable
data class Point(
    val pos: String
)