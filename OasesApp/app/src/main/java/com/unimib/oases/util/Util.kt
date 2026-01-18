package com.unimib.oases.util

inline fun <A, B, R> let2(
    a: A?,
    b: B?,
    block: (A, B) -> R
): R? =
    if (a != null && b != null) block(a, b) else null