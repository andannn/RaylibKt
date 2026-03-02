package raylib.core

import kotlinx.cinterop.CValue
import kotlinx.cinterop.CValues

import raylib.interop.*

fun CValue<Matrix>.determinant(): Float = MatrixDeterminant(this)
fun CValue<Matrix>.trace(): Float = MatrixTrace(this)
fun CValue<Matrix>.transpose(): CValue<Matrix> = MatrixTranspose(this)
fun CValue<Matrix>.invert(): CValue<Matrix> = MatrixInvert(this)
fun CValue<Matrix>.add(other: CValue<Matrix>): CValue<Matrix> = MatrixAdd(this, other)
fun CValue<Matrix>.subtract(other: CValue<Matrix>): CValue<Matrix> = MatrixSubtract(this, other)
fun CValue<Matrix>.multiply(other: CValue<Matrix>): CValue<Matrix> = MatrixMultiply(this, other)


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
