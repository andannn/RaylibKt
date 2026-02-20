## Simple Kotlin Native wrapper for [raylib](https://github.com/raysan5/raylib)

### For android build

Mark main function as extern with name "raylib_android_main"
```kotlin
@CName(externName = "raylib_android_main")
fun main() {
    
}
```