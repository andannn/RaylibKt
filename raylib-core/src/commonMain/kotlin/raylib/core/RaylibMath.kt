@file:OptIn(ExperimentalForeignApi::class)
@file:Suppress("ktlint:standard:no-wildcard-imports")

package raylib.core

import kotlinx.cinterop.*
import raylib.interop.MatrixAdd
import raylib.interop.MatrixDeterminant
import raylib.interop.MatrixFrustum
import raylib.interop.MatrixIdentity
import raylib.interop.MatrixInvert
import raylib.interop.MatrixMultiply
import raylib.interop.MatrixPerspective
import raylib.interop.MatrixRotate
import raylib.interop.MatrixRotateX
import raylib.interop.MatrixRotateXYZ
import raylib.interop.MatrixRotateY
import raylib.interop.MatrixRotateZ
import raylib.interop.MatrixRotateZYX
import raylib.interop.MatrixScale
import raylib.interop.MatrixSubtract
import raylib.interop.MatrixTrace
import raylib.interop.MatrixTranslate
import raylib.interop.MatrixTranspose
import raylib.interop.Vector2Add
import raylib.interop.Vector2AddValue
import raylib.interop.Vector2Angle
import raylib.interop.Vector2Clamp
import raylib.interop.Vector2ClampValue
import raylib.interop.Vector2Distance
import raylib.interop.Vector2DistanceSqr
import raylib.interop.Vector2Divide
import raylib.interop.Vector2DotProduct
import raylib.interop.Vector2Equals
import raylib.interop.Vector2Invert
import raylib.interop.Vector2Length
import raylib.interop.Vector2LengthSqr
import raylib.interop.Vector2Lerp
import raylib.interop.Vector2LineAngle
import raylib.interop.Vector2MoveTowards
import raylib.interop.Vector2Multiply
import raylib.interop.Vector2Negate
import raylib.interop.Vector2Normalize
import raylib.interop.Vector2Reflect
import raylib.interop.Vector2Rotate
import raylib.interop.Vector2Scale
import raylib.interop.Vector2Subtract
import raylib.interop.Vector2SubtractValue
import raylib.interop.Vector2Transform
import raylib.interop.Vector3Add
import raylib.interop.Vector3AddValue
import raylib.interop.Vector3Angle
import raylib.interop.Vector3Barycenter
import raylib.interop.Vector3Clamp
import raylib.interop.Vector3ClampValue
import raylib.interop.Vector3CrossProduct
import raylib.interop.Vector3Distance
import raylib.interop.Vector3DistanceSqr
import raylib.interop.Vector3Divide
import raylib.interop.Vector3DotProduct
import raylib.interop.Vector3Equals
import raylib.interop.Vector3Invert
import raylib.interop.Vector3Length
import raylib.interop.Vector3LengthSqr
import raylib.interop.Vector3Lerp
import raylib.interop.Vector3Max
import raylib.interop.Vector3Min
import raylib.interop.Vector3Multiply
import raylib.interop.Vector3Negate
import raylib.interop.Vector3Normalize
import raylib.interop.Vector3One
import raylib.interop.Vector3OrthoNormalize
import raylib.interop.Vector3Perpendicular
import raylib.interop.Vector3Project
import raylib.interop.Vector3Reflect
import raylib.interop.Vector3Refract
import raylib.interop.Vector3Reject
import raylib.interop.Vector3RotateByAxisAngle
import raylib.interop.Vector3RotateByQuaternion
import raylib.interop.Vector3Scale
import raylib.interop.Vector3Subtract
import raylib.interop.Vector3SubtractValue
import raylib.interop.Vector3ToFloatV
import raylib.interop.Vector3Transform
import raylib.interop.Vector3Unproject
import raylib.interop.Vector3Zero
import raylib.interop.Vector4
import raylib.interop.float16
import raylib.interop.float3

typealias Float3 = float3
typealias Float16 = float16

typealias Matrix = raylib.interop.Matrix
object MatrixFactory {
    fun identity(): CValue<Matrix> = MatrixIdentity()
    fun translate(x: Float, y: Float, z: Float): CValue<Matrix> = MatrixTranslate(x, y, z)
    fun rotate(axis: CValue<Vector3>, angle: Float): CValue<Matrix> = MatrixRotate(axis, angle)
    fun rotateX(angle: Float): CValue<Matrix> = MatrixRotateX(angle)
    fun rotateY(angle: Float): CValue<Matrix> = MatrixRotateY(angle)
    fun rotateZ(angle: Float): CValue<Matrix> = MatrixRotateZ(angle)
    fun rotateXYZ(angle: CValue<Vector3>): CValue<Matrix> = MatrixRotateXYZ(angle)
    fun rotateZYX(angle: CValue<Vector3>): CValue<Matrix> = MatrixRotateZYX(angle)
    fun scale(x: Float, y: Float, z: Float): CValue<Matrix> = MatrixScale(x, y, z)
    fun frustum(left: Double, right: Double, bottom: Double, top: Double, near: Double, far: Double): CValue<Matrix> =
        MatrixFrustum(left, right, bottom, top, near, far)
    fun perspective(fovY: Double, aspect: Double, nearPlane: Double, farPlane: Double): CValue<Matrix> =
        MatrixPerspective(fovY, aspect, nearPlane, farPlane)
}
fun CValue<Matrix>.determinant(): Float = MatrixDeterminant(this)
fun CValue<Matrix>.trace(): Float = MatrixTrace(this)
fun CValue<Matrix>.transpose(): CValue<Matrix> = MatrixTranspose(this)
fun CValue<Matrix>.invert(): CValue<Matrix> = MatrixInvert(this)
fun CValue<Matrix>.add(other: CValue<Matrix>): CValue<Matrix> = MatrixAdd(this, other)
fun CValue<Matrix>.subtract(other: CValue<Matrix>): CValue<Matrix> = MatrixSubtract(this, other)
fun CValue<Matrix>.multiply(other: CValue<Matrix>): CValue<Matrix> = MatrixMultiply(this, other)

typealias Vector2 = raylib.interop.Vector2
fun Vector2(x: Float = 0f, y: Float = 0f): CValue<Vector2> = cValue {
    this.x = x
    this.y = y
}
fun CValue<Vector2>.add(other: CValue<Vector2>): CValue<Vector2> = Vector2Add(this, other)
fun CValue<Vector2>.addValue(value: Float): CValue<Vector2> = Vector2AddValue(this, value)
fun CValue<Vector2>.subtract(other: CValue<Vector2>): CValue<Vector2> = Vector2Subtract(this, other)
fun CValue<Vector2>.subtractValue(value: Float): CValue<Vector2> = Vector2SubtractValue(this, value)
fun CValue<Vector2>.length(): Float = Vector2Length(this)
fun CValue<Vector2>.lengthSqr(): Float = Vector2LengthSqr(this)
fun CValue<Vector2>.dot(other: CValue<Vector2>): Float = Vector2DotProduct(this, other)
fun CValue<Vector2>.distance(other: CValue<Vector2>): Float = Vector2Distance(this, other)
fun CValue<Vector2>.distanceSqr(other: CValue<Vector2>): Float = Vector2DistanceSqr(this, other)
fun CValue<Vector2>.angle(other: CValue<Vector2>): Float = Vector2Angle(this, other)
fun CValue<Vector2>.lineAngle(to: CValue<Vector2>): Float = Vector2LineAngle(this, to)
fun CValue<Vector2>.scale(factor: Float): CValue<Vector2> = Vector2Scale(this, factor)
fun CValue<Vector2>.multiply(other: CValue<Vector2>): CValue<Vector2> = Vector2Multiply(this, other)
fun CValue<Vector2>.divide(other: CValue<Vector2>): CValue<Vector2> = Vector2Divide(this, other)
fun CValue<Vector2>.negate(): CValue<Vector2> = Vector2Negate(this)
fun CValue<Vector2>.normalize(): CValue<Vector2> = Vector2Normalize(this)
fun CValue<Vector2>.transform(mat: CValue<Matrix>): CValue<Vector2> = Vector2Transform(this, mat)
fun CValue<Vector2>.lerp(to: CValue<Vector2>, amount: Float): CValue<Vector2> =
    Vector2Lerp(this, to, amount)
fun CValue<Vector2>.reflect(normal: CValue<Vector2>): CValue<Vector2> = Vector2Reflect(this, normal)
fun CValue<Vector2>.rotate(angle: Float): CValue<Vector2> = Vector2Rotate(this, angle)
fun CValue<Vector2>.moveTowards(target: CValue<Vector2>, maxDistance: Float): CValue<Vector2> =
    Vector2MoveTowards(this, target, maxDistance)
fun CValue<Vector2>.invert(): CValue<Vector2> = Vector2Invert(this)
fun CValue<Vector2>.clamp(min: CValue<Vector2>, max: CValue<Vector2>): CValue<Vector2> =
    Vector2Clamp(this, min, max)
fun CValue<Vector2>.clampValue(min: Float, max: Float): CValue<Vector2> =
    Vector2ClampValue(this, min, max)
fun CValue<Vector2>.equalsTo(other: CValue<Vector2>): Int = Vector2Equals(this, other)

typealias Vector3 = raylib.interop.Vector3
object Vector3Factory {
    fun zero(): CValue<Vector3> = Vector3Zero()
    fun one(): CValue<Vector3> = Vector3One()
}
fun CValue<Vector3>.add(other: CValue<Vector3>): CValue<Vector3> = Vector3Add(this, other)
fun CValue<Vector3>.addValue(value: Float): CValue<Vector3> = Vector3AddValue(this, value)
fun CValue<Vector3>.subtract(other: CValue<Vector3>): CValue<Vector3> = Vector3Subtract(this, other)
fun CValue<Vector3>.subtractValue(value: Float): CValue<Vector3> = Vector3SubtractValue(this, value)
fun CValue<Vector3>.scale(scalar: Float): CValue<Vector3> = Vector3Scale(this, scalar)
fun CValue<Vector3>.multiply(other: CValue<Vector3>): CValue<Vector3> = Vector3Multiply(this, other)
fun CValue<Vector3>.cross(other: CValue<Vector3>): CValue<Vector3> = Vector3CrossProduct(this, other)
fun CValue<Vector3>.perpendicular(): CValue<Vector3> = Vector3Perpendicular(this)
fun CValue<Vector3>.length(): Float = Vector3Length(this)
fun CValue<Vector3>.lengthSqr(): Float = Vector3LengthSqr(this)
fun CValue<Vector3>.dotProduct(target: CValue<Vector3>): Float = Vector3DotProduct(this, target)
fun CValue<Vector3>.distance(target: CValue<Vector3>): Float = Vector3Distance(this, target)
fun CValue<Vector3>.distanceSqr(target: CValue<Vector3>): Float = Vector3DistanceSqr(this, target)
fun CValue<Vector3>.angle(target: CValue<Vector3>): Float = Vector3Angle(this, target)
fun CValue<Vector3>.negate(): CValue<Vector3> = Vector3Negate(this)
fun CValue<Vector3>.divide(target: CValue<Vector3>): CValue<Vector3> = Vector3Divide(this, target)
fun CValue<Vector3>.normalize(): CValue<Vector3> = Vector3Normalize(this)
fun CValue<Vector3>.project(target: CValue<Vector3>): CValue<Vector3> = Vector3Project(this, target)
fun CValue<Vector3>.reject(target: CValue<Vector3>): CValue<Vector3> = Vector3Reject(this, target)
fun CValue<Vector3>.transform(mat: CValue<Matrix>): CValue<Vector3> = Vector3Transform(this, mat)
fun CValue<Vector3>.rotateByQuaternion(q: CValue<Quaternion>): CValue<Vector3> =
    Vector3RotateByQuaternion(this, q)
fun CValue<Vector3>.rotateByAxisAngle(axis: CValue<Vector3>, angle: Float): CValue<Vector3> =
    Vector3RotateByAxisAngle(this, axis, angle)
fun CValue<Vector3>.lerp(to: CValue<Vector3>, amount: Float): CValue<Vector3> =
    Vector3Lerp(this, to, amount)
fun CValue<Vector3>.reflect(normal: CValue<Vector3>): CValue<Vector3> = Vector3Reflect(this, normal)
fun CValue<Vector3>.min(other: CValue<Vector3>): CValue<Vector3> = Vector3Min(this, other)
fun CValue<Vector3>.max(other: CValue<Vector3>): CValue<Vector3> = Vector3Max(this, other)
fun CValue<Vector3>.barycenter(a: CValue<Vector3>, b: CValue<Vector3>, c: CValue<Vector3>): CValue<Vector3> =
    Vector3Barycenter(this, a, b, c)
fun CValue<Vector3>.unproject(projection: CValue<Matrix>, view: CValue<Matrix>): CValue<Vector3> =
    Vector3Unproject(this, projection, view)
fun CValue<Vector3>.toFloatV(): CValue<Float3> = Vector3ToFloatV(this)
fun CValue<Vector3>.invert(): CValue<Vector3> = Vector3Invert(this)
fun CValue<Vector3>.clamp(min: CValue<Vector3>, max: CValue<Vector3>): CValue<Vector3> =
    Vector3Clamp(this, min, max)
fun CValue<Vector3>.clampValue(min: Float, max: Float): CValue<Vector3> =
    Vector3ClampValue(this, min, max)
fun CValue<Vector3>.equalsTo(other: CValue<Vector3>): Int = Vector3Equals(this, other)
fun CValue<Vector3>.refract(n: CValue<Vector3>, r: Float): CValue<Vector3> =
    Vector3Refract(this, n, r)
fun CValues<Vector3>?.orthoNormalize(v2: CValuesRef<Vector3>?) = Vector3OrthoNormalize(this, v2)

typealias Vector4 = Vector4
typealias Quaternion = Vector4
