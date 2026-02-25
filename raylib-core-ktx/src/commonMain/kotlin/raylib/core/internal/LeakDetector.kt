package raylib.core.internal

import kotlin.experimental.ExperimentalNativeApi
import kotlin.native.ref.createCleaner

class LeakDetector(val name: String) {
    // 当这个对象所在的引用链断开并被 GC 时，执行这里的逻辑
    @OptIn(ExperimentalNativeApi::class)
    private val cleaner = createCleaner(name) {
        println("Runtime Monitor: Object [$it] has been Garbage Collected.")
    }
}