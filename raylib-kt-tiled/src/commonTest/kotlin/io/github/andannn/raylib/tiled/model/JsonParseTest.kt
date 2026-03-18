/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.tiled.model

import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class JsonParseTest {
    private val json = Json { ignoreUnknownKeys = true }
    @Test
    fun jsonParseTest_parse_map() {
        val decoded = Json.decodeFromString<TileMap>(
            """
            { "compressionlevel":4,
             "height":20,
             "infinite":false,
             "layers":[],
             "nextlayerid":5,
             "nextobjectid":2,
             "orientation":"orthogonal",
             "renderorder":"right-down",
             "tiledversion":"1.12.0",
             "tileheight":16,
             "tilesets":[
                    {
                     "firstgid":1,
                     "source":"Confetti (16x16).tsj"
                    }, 
                    {
                     "firstgid":7,
                     "source":"Terrain (16x16).tsj"
                    }],
             "tilewidth":16,
             "type":"map",
             "version":"1.10",
             "width":30
            }
        """.trimIndent()
        )
        assertEquals(30, decoded.width)
        assertEquals(20, decoded.height)
        assertEquals(16, decoded.tileHeight)
        assertEquals(16, decoded.tileWidth)
    }

    @Test
    fun jsonParseTest_parse_tiled_layer() {
        val decode = Json.decodeFromString<List<Layer>>(
            """
[
  {
    "id": 8,
    "layers": [
      {
        "draworder": "topdown",
        "id": 9,
        "name": "ObjectLayer",
        "objects": [
          {
            "height": 27.5904353157572,
            "id": 3,
            "name": "Object1",
            "opacity": 1,
            "rotation": 0,
            "type": "",
            "visible": true,
            "width": 30.0429184549356,
            "x": 28.8166768853464,
            "y": 20.0286123032904
          }
        ],
        "opacity": 1,
        "type": "objectgroup",
        "visible": true,
        "x": 0,
        "y": 0
      },
      {
        "compression": "gzip",
        "data": "H4sIAAAAAAAAE2NgGAWjAD9ghOKBANS2G2YePgwD6HxKAKnmkKoeGyDH\/aSqxwZINYNU9bgAsf4lVh2pAF8aQhenBaCXPaNgFIwC4gAAmas7JmAJAAA=",
        "encoding": "base64",
        "height": 20,
        "id": 5,
        "name": "Tile2",
        "opacity": 1,
        "type": "tilelayer",
        "visible": true,
        "width": 30,
        "x": 0,
        "y": 0
      },
      {
        "compression": "gzip",
        "data": "H4sIAAAAAAAAE+3NIQ4AAAjDQML\/H00QMwS9mZ6saRUA6RsM9pn6JvD1SH55e+md+uM3w8IHdWAJAAA=",
        "encoding": "base64",
        "height": 20,
        "id": 6,
        "name": "Tile1",
        "opacity": 1,
        "type": "tilelayer",
        "visible": true,
        "width": 30,
        "x": 0,
        "y": 0
      }
    ],
    "name": "Group",
    "opacity": 1,
    "type": "group",
    "visible": true,
    "x": 0,
    "y": 0
  },
  {
    "compression": "gzip",
    "data": "H4sIAAAAAAAAE+3PSwrAMAgE0FDo\/a9cXASGQfFTwUXyIAttOtOulfM4czfJtzqs\/V9erve8KpLLd3jOir4fvYdeOIzzeN6sfRT2a1naTlj7DO\/\/uUPbVUkO9uM37B48XTqzMiZ6JzrFab1isvu6qj7TbopcYAkAAA==",
    "encoding": "base64",
    "height": 20,
    "id": 10,
    "name": "Tile3",
    "opacity": 1,
    "type": "tilelayer",
    "visible": true,
    "width": 30,
    "x": 0,
    "y": 0
  },
  {
    "draworder": "topdown",
    "id": 11,
    "name": "ObjectLayerOutSide",
    "objects": [
      {
        "height": 30.0429184549356,
        "id": 6,
        "name": "Object2",
        "opacity": 1,
        "rotation": 0,
        "type": "",
        "visible": true,
        "width": 34.3347639484979,
        "x": 94.6249744533006,
        "y": 17.9848763539751
      },
      {
        "height": 0,
        "id": 7,
        "name": "",
        "opacity": 1,
        "rotation": 0,
        "type": "",
        "visible": true,
        "width": 0.408747189863064,
        "x": 50.071530758226,
        "y": -19.2111179235643
      }
    ],
    "opacity": 1,
    "type": "objectgroup",
    "visible": true,
    "x": 0,
    "y": 0
  }
]
        """.trimIndent()
        )
        val layer = assertIs<GroupLayer>(decode.first())
        assertEquals(3, layer.layers.size)
        val child1 = assertIs<ObjectGroupLayer>(layer.layers.first()).also {
            assertEquals("ObjectLayer", it.name)
            assertEquals("Object1", it.objects[0].name)
            assertEquals(28.8166768853464, it.objects[0].x)
        }

        val child2 = assertIs<TileLayer>(layer.layers[1])
        val child3 = assertIs<TileLayer>(layer.layers[2])
        println("${child3.data}")
    }

    @Test
    fun jsonParseTest_parse_csv_layer_data() {
        val decode = Json.decodeFromString<TileMap>(
            """
            { "compressionlevel":4,
             "height":20,
             "infinite":false,
             "layers":[
                    {
                     "data":[2684354570, 10, 1610612746, 3221225482, 1073741834, 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
                     "height":20,
                     "id":12,
                     "name":"Tile Layer 1",
                     "opacity":1,
                     "type":"tilelayer",
                     "visible":true,
                     "width":30,
                     "x":0,
                     "y":0
                    }],
             "nextlayerid":13,
             "nextobjectid":8,
             "orientation":"orthogonal",
             "renderorder":"right-down",
             "tiledversion":"1.12.0",
             "tileheight":16,
             "tilesets":[
                    {
                     "firstgid":1,
                     "source":"Confetti (16x16).tsj"
                    }, 
                    {
                     "firstgid":7,
                     "source":"Terrain (16x16).tsj"
                    }],
             "tilewidth":16,
             "type":"map",
             "version":"1.10",
             "width":30
            }
        """.trimIndent()
        )

        val tileLayer = assertIs<TileLayer>(decode.layers[0])
        tileLayer.gidArray.also {
            it[0].also {
                assertEquals(10, it.tileId)
                assertEquals(true, it.flipHorizontal)
            }
            it[1].also {
                assertEquals(10, it.tileId)
                assertEquals(false, it.flipHorizontal)
            }
            it[2].also {
                println(it)
                assertEquals(10, it.tileId)
                assertEquals(false, it.flipHorizontal)
                assertEquals(true, it.flipDiagonal)
                assertEquals(true, it.flipVertical)
            }
        }
    }

    @Test
    fun jsonParseTest_parse_tile_object() {
        val decode = json.decodeFromString<List<Object>>(
            """
                [
                  {
                    "height": 121.684108055488,
                    "id": 12,
                    "name": "Arrrr",
                    "opacity": 1,
                    "rotation": 0,
                    "type": "",
                    "visible": true,
                    "width": 126.551472377707,
                    "x": 64.8981909629269,
                    "y": 29.2041859333171
                  },
                  {
                    "height": 0,
                    "id": 14,
                    "name": "StartPoint",
                    "opacity": 1,
                    "point": true,
                    "rotation": 0,
                    "type": "",
                    "visible": true,
                    "width": 0,
                    "x": 279.873448527622,
                    "y": 186.582299018415
                  },
                  {
                    "height": 0,
                    "id": 16,
                    "name": "Polygen",
                    "opacity": 1,
                    "polygon": [
                      {
                        "x": -25.820170109356,
                        "y": 51.6403402187121
                      },
                      {
                        "x": 107.077764277035,
                        "y": 0
                      },
                      {
                        "x": 48.6026731470231,
                        "y": 104.799513973269
                      }
                    ],
                    "rotation": 0,
                    "type": "",
                    "visible": true,
                    "width": 0,
                    "x": 529.313487241798,
                    "y": 228.584447144593
                  },
                  {
                    "height": 18.84375,
                    "id": 17,
                    "name": "",
                    "opacity": 1,
                    "rotation": 0,
                    "text": {
                      "text": "Hello World",
                      "wrap": true
                    },
                    "type": "",
                    "visible": true,
                    "width": 80.234375,
                    "x": 316.808693423451,
                    "y": 243.463908718105
                  },
                  {
                    "capsule": true,
                    "height": 157.958687727825,
                    "id": 18,
                    "name": "Capsel",
                    "opacity": 1,
                    "rotation": 0,
                    "type": "",
                    "visible": true,
                    "width": 72.1445929526124,
                    "x": 148.845686512758,
                    "y": 233.900364520049
                  },
                  {
                    "ellipse": true,
                    "height": 109.356014580802,
                    "id": 24,
                    "name": "Eclipse",
                    "opacity": 1,
                    "rotation": 0,
                    "type": "",
                    "visible": true,
                    "width": 72.9040097205346,
                    "x": 372.114216281896,
                    "y": 8.35358444714459
                  },
                  {
                    "height": 0,
                    "id": 25,
                    "name": "PolygenLines",
                    "opacity": 1,
                    "polyline": [
                      {
                        "x": 0,
                        "y": 0
                      },
                      {
                        "x": -66.8286755771567,
                        "y": 22.0230862697448
                      },
                      {
                        "x": -78.2199270959902,
                        "y": 119.228432563791
                      },
                      {
                        "x": -15.9477521263669,
                        "y": 78.2199270959903
                      }
                    ],
                    "rotation": 0,
                    "type": "",
                    "visible": true,
                    "width": 0,
                    "x": 536.148238153098,
                    "y": 62.2721749696233
                  },
                  {
                    "gid": 1,
                    "height": 16,
                    "id": 32,
                    "name": "TileObj",
                    "opacity": 1,
                    "rotation": 0,
                    "type": "",
                    "visible": true,
                    "width": 16,
                    "x": 296.869823283421,
                    "y": 102.878366833041
                  }
                ]
            """.trimIndent()
        )

        assertEquals(8, decode.size, "Should parse exactly 8 objects")

        val rectObj = assertIs<RectObject>(decode[0])
        assertEquals(12, rectObj.id)
        assertEquals("Arrrr", rectObj.name)
        assertEquals(126.551472377707, rectObj.width)

        val pointObj = assertIs<PointObject>(decode[1])
        assertEquals(14, pointObj.id)
        assertEquals("StartPoint", pointObj.name)
        assertTrue(pointObj.point)

        val polygonObj = assertIs<PolygonObject>(decode[2])
        assertEquals(16, polygonObj.id)
        assertEquals("Polygen", polygonObj.name)
        assertEquals(3, polygonObj.polygon.size, "Polygon should have 3 points")
        assertEquals(-25.820170109356, polygonObj.polygon[0].x)

        val textObj = assertIs<TextObject>(decode[3])
        assertEquals(17, textObj.id)
        assertEquals("Hello World", textObj.text.text)
        assertTrue(textObj.text.wrap)

        val capsuleObj = assertIs<CapsuleObject>(decode[4])
        assertEquals(18, capsuleObj.id)
        assertEquals("Capsel", capsuleObj.name)
        assertTrue(capsuleObj.capsule)

        val ellipseObj = assertIs<EllipseObject>(decode[5])
        assertEquals(24, ellipseObj.id)
        assertEquals("Eclipse", ellipseObj.name)
        assertTrue(ellipseObj.ellipse)

        val polylineObj = assertIs<PolylineObject>(decode[6])
        assertEquals(25, polylineObj.id)
        assertEquals("PolygenLines", polylineObj.name)
        assertEquals(4, polylineObj.polyline.size, "Polyline should have 4 points")

        val tileObj = assertIs<TileObject>(decode[7])
        assertEquals(32, tileObj.id)
        assertEquals("TileObj", tileObj.name)
        assertEquals(1, tileObj.gid)
    }
}
