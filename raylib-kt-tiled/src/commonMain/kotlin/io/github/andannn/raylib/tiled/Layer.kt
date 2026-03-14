package io.github.andannn.raylib.tiled

import kotlin.Boolean
import kotlin.Double
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class Layer(
  @SerialName(value = "draworder")
  public val draworder: String,
  @SerialName(value = "id")
  public val id: Double,
  @SerialName(value = "name")
  public val name: String,
  @SerialName(value = "objects")
  public val objects: List<Object>,
  @SerialName(value = "opacity")
  public val opacity: Double,
  @SerialName(value = "type")
  public val type: String,
  @SerialName(value = "visible")
  public val visible: Boolean,
  @SerialName(value = "x")
  public val x: Double,
  @SerialName(value = "y")
  public val y: Double,
)
