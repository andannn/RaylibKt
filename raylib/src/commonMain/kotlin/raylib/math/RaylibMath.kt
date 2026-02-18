@file:OptIn(ExperimentalForeignApi::class)
@file:Suppress("ktlint:standard:no-wildcard-imports")

package raylib.math

import kotlinx.cinterop.*

typealias Float3 = raylib.interop.float3
typealias Float16 = raylib.interop.float16

typealias Matrix = raylib.interop.Matrix
object MatrixFactory {
    fun identity(): CValue<Matrix> = raylib.interop.MatrixIdentity()
    fun translate(x: Float, y: Float, z: Float): CValue<Matrix> = raylib.interop.MatrixTranslate(x, y, z)
    fun rotate(axis: CValue<Vector3>, angle: Float): CValue<Matrix> = raylib.interop.MatrixRotate(axis, angle)
    fun rotateX(angle: Float): CValue<Matrix> = raylib.interop.MatrixRotateX(angle)
    fun rotateY(angle: Float): CValue<Matrix> = raylib.interop.MatrixRotateY(angle)
    fun rotateZ(angle: Float): CValue<Matrix> = raylib.interop.MatrixRotateZ(angle)
    fun rotateXYZ(angle: CValue<Vector3>): CValue<Matrix> = raylib.interop.MatrixRotateXYZ(angle)
    fun rotateZYX(angle: CValue<Vector3>): CValue<Matrix> = raylib.interop.MatrixRotateZYX(angle)
    fun scale(x: Float, y: Float, z: Float): CValue<Matrix> = raylib.interop.MatrixScale(x, y, z)
    fun frustum(left: Double, right: Double, bottom: Double, top: Double, near: Double, far: Double): CValue<Matrix> = raylib.interop.MatrixFrustum(left, right, bottom, top, near, far)
    fun perspective(fovY: Double, aspect: Double, nearPlane: Double, farPlane: Double): CValue<Matrix> = raylib.interop.MatrixPerspective(fovY, aspect, nearPlane, farPlane)
}
fun CValue<Matrix>.determinant(): Float = raylib.interop.MatrixDeterminant(this)
fun CValue<Matrix>.trace(): Float = raylib.interop.MatrixTrace(this)
fun CValue<Matrix>.transpose(): CValue<Matrix> = raylib.interop.MatrixTranspose(this)
fun CValue<Matrix>.invert(): CValue<Matrix> = raylib.interop.MatrixInvert(this)
fun CValue<Matrix>.add(other: CValue<Matrix>): CValue<Matrix> = raylib.interop.MatrixAdd(this, other)
fun CValue<Matrix>.subtract(other: CValue<Matrix>): CValue<Matrix> = raylib.interop.MatrixSubtract(this, other)
fun CValue<Matrix>.multiply(other: CValue<Matrix>): CValue<Matrix> = raylib.interop.MatrixMultiply(this, other)

typealias Vector2 = raylib.interop.Vector2
fun CValue<Vector2>.add(other: CValue<Vector2>): CValue<Vector2> = raylib.interop.Vector2Add(this, other)
fun CValue<Vector2>.addValue(value: Float): CValue<Vector2> = raylib.interop.Vector2AddValue(this, value)
fun CValue<Vector2>.subtract(other: CValue<Vector2>): CValue<Vector2> = raylib.interop.Vector2Subtract(this, other)
fun CValue<Vector2>.subtractValue(value: Float): CValue<Vector2> = raylib.interop.Vector2SubtractValue(this, value)
fun CValue<Vector2>.length(): Float = raylib.interop.Vector2Length(this)
fun CValue<Vector2>.lengthSqr(): Float = raylib.interop.Vector2LengthSqr(this)
fun CValue<Vector2>.dot(other: CValue<Vector2>): Float = raylib.interop.Vector2DotProduct(this, other)
fun CValue<Vector2>.distance(other: CValue<Vector2>): Float = raylib.interop.Vector2Distance(this, other)
fun CValue<Vector2>.distanceSqr(other: CValue<Vector2>): Float = raylib.interop.Vector2DistanceSqr(this, other)
fun CValue<Vector2>.angle(other: CValue<Vector2>): Float = raylib.interop.Vector2Angle(this, other)
fun CValue<Vector2>.lineAngle(to: CValue<Vector2>): Float = raylib.interop.Vector2LineAngle(this, to)
fun CValue<Vector2>.scale(factor: Float): CValue<Vector2> = raylib.interop.Vector2Scale(this, factor)
fun CValue<Vector2>.multiply(other: CValue<Vector2>): CValue<Vector2> = raylib.interop.Vector2Multiply(this, other)
fun CValue<Vector2>.divide(other: CValue<Vector2>): CValue<Vector2> = raylib.interop.Vector2Divide(this, other)
fun CValue<Vector2>.negate(): CValue<Vector2> = raylib.interop.Vector2Negate(this)
fun CValue<Vector2>.normalize(): CValue<Vector2> = raylib.interop.Vector2Normalize(this)
fun CValue<Vector2>.transform(mat: CValue<Matrix>): CValue<Vector2> = raylib.interop.Vector2Transform(this, mat)
fun CValue<Vector2>.lerp(to: CValue<Vector2>, amount: Float): CValue<Vector2> = raylib.interop.Vector2Lerp(this, to, amount)
fun CValue<Vector2>.reflect(normal: CValue<Vector2>): CValue<Vector2> = raylib.interop.Vector2Reflect(this, normal)
fun CValue<Vector2>.rotate(angle: Float): CValue<Vector2> = raylib.interop.Vector2Rotate(this, angle)
fun CValue<Vector2>.moveTowards(target: CValue<Vector2>, maxDistance: Float): CValue<Vector2> = raylib.interop.Vector2MoveTowards(this, target, maxDistance)
fun CValue<Vector2>.invert(): CValue<Vector2> = raylib.interop.Vector2Invert(this)
fun CValue<Vector2>.clamp(min: CValue<Vector2>, max: CValue<Vector2>): CValue<Vector2> = raylib.interop.Vector2Clamp(this, min, max)
fun CValue<Vector2>.clampValue(min: Float, max: Float): CValue<Vector2> = raylib.interop.Vector2ClampValue(this, min, max)
fun CValue<Vector2>.equalsTo(other: CValue<Vector2>): Int = raylib.interop.Vector2Equals(this, other)

typealias Vector3 = raylib.interop.Vector3
object Vector3Factory {
    fun zero(): CValue<Vector3> = raylib.interop.Vector3Zero()
    fun one(): CValue<Vector3> = raylib.interop.Vector3One()
}
fun CValue<Vector3>.add(other: CValue<Vector3>): CValue<Vector3> = raylib.interop.Vector3Add(this, other)
fun CValue<Vector3>.addValue(value: Float): CValue<Vector3> = raylib.interop.Vector3AddValue(this, value)
fun CValue<Vector3>.subtract(other: CValue<Vector3>): CValue<Vector3> = raylib.interop.Vector3Subtract(this, other)
fun CValue<Vector3>.subtractValue(value: Float): CValue<Vector3> = raylib.interop.Vector3SubtractValue(this, value)
fun CValue<Vector3>.scale(scalar: Float): CValue<Vector3> = raylib.interop.Vector3Scale(this, scalar)
fun CValue<Vector3>.multiply(other: CValue<Vector3>): CValue<Vector3> = raylib.interop.Vector3Multiply(this, other)
fun CValue<Vector3>.cross(other: CValue<Vector3>): CValue<Vector3> = raylib.interop.Vector3CrossProduct(this, other)
fun CValue<Vector3>.perpendicular(): CValue<Vector3> = raylib.interop.Vector3Perpendicular(this)
fun CValue<Vector3>.length(): Float = raylib.interop.Vector3Length(this)
fun CValue<Vector3>.lengthSqr(): Float = raylib.interop.Vector3LengthSqr(this)
fun CValue<Vector3>.dotProduct(target: CValue<Vector3>): Float = raylib.interop.Vector3DotProduct(this, target)
fun CValue<Vector3>.distance(target: CValue<Vector3>): Float = raylib.interop.Vector3Distance(this, target)
fun CValue<Vector3>.distanceSqr(target: CValue<Vector3>): Float = raylib.interop.Vector3DistanceSqr(this, target)
fun CValue<Vector3>.angle(target: CValue<Vector3>): Float = raylib.interop.Vector3Angle(this, target)
fun CValue<Vector3>.negate(): CValue<Vector3> = raylib.interop.Vector3Negate(this)
fun CValue<Vector3>.divide(target: CValue<Vector3>): CValue<Vector3> = raylib.interop.Vector3Divide(this, target)
fun CValue<Vector3>.normalize(): CValue<Vector3> = raylib.interop.Vector3Normalize(this)
fun CValue<Vector3>.project(target: CValue<Vector3>): CValue<Vector3> = raylib.interop.Vector3Project(this, target)
fun CValue<Vector3>.reject(target: CValue<Vector3>): CValue<Vector3> = raylib.interop.Vector3Reject(this, target)
fun CValue<Vector3>.transform(mat: CValue<Matrix>): CValue<Vector3> = raylib.interop.Vector3Transform(this, mat)
fun CValue<Vector3>.rotateByQuaternion(q: CValue<Quaternion>): CValue<Vector3> = raylib.interop.Vector3RotateByQuaternion(this, q)
fun CValue<Vector3>.rotateByAxisAngle(axis: CValue<Vector3>, angle: Float): CValue<Vector3> = raylib.interop.Vector3RotateByAxisAngle(this, axis, angle)
fun CValue<Vector3>.lerp(to: CValue<Vector3>, amount: Float): CValue<Vector3> = raylib.interop.Vector3Lerp(this, to, amount)
fun CValue<Vector3>.reflect(normal: CValue<Vector3>): CValue<Vector3> = raylib.interop.Vector3Reflect(this, normal)
fun CValue<Vector3>.min(other: CValue<Vector3>): CValue<Vector3> = raylib.interop.Vector3Min(this, other)
fun CValue<Vector3>.max(other: CValue<Vector3>): CValue<Vector3> = raylib.interop.Vector3Max(this, other)
fun CValue<Vector3>.barycenter(a: CValue<Vector3>, b: CValue<Vector3>, c: CValue<Vector3>): CValue<Vector3> = raylib.interop.Vector3Barycenter(this, a, b, c)
fun CValue<Vector3>.unproject(projection: CValue<Matrix>, view: CValue<Matrix>): CValue<Vector3> = raylib.interop.Vector3Unproject(this, projection, view)
fun CValue<Vector3>.toFloatV(): CValue<Float3> = raylib.interop.Vector3ToFloatV(this)
fun CValue<Vector3>.invert(): CValue<Vector3> = raylib.interop.Vector3Invert(this)
fun CValue<Vector3>.clamp(min: CValue<Vector3>, max: CValue<Vector3>): CValue<Vector3> = raylib.interop.Vector3Clamp(this, min, max)
fun CValue<Vector3>.clampValue(min: Float, max: Float): CValue<Vector3> = raylib.interop.Vector3ClampValue(this, min, max)
fun CValue<Vector3>.equalsTo(other: CValue<Vector3>): Int = raylib.interop.Vector3Equals(this, other)
fun CValue<Vector3>.refract(n: CValue<Vector3>, r: Float): CValue<Vector3> = raylib.interop.Vector3Refract(this, n, r)
fun CValues<Vector3>?.orthoNormalize(v2: CValuesRef<Vector3>?) = raylib.interop.Vector3OrthoNormalize(this, v2)

typealias Vector4 = raylib.interop.Vector4
typealias Quaternion = raylib.interop.Vector4
