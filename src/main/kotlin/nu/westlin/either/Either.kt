package nu.westlin.either

sealed class Either<out L, out R> {

    abstract val isRight: Boolean
    abstract val isLeft: Boolean

    fun <T> fold(leftFunction: (L) -> T, rightFunction: (R) -> T): T {
        return when(this) {
            is Left -> leftFunction(a)
            is Right -> rightFunction(b)
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <B> getOrElse(default: () -> B): B =
        (this as Either<*, B>).fold({ default() }, { (this as Right).b as B })

    companion object {
        fun <L> left(left: L): Either<L, Nothing> = Left(left)
        fun <R> right(right: R): Either<Nothing, R> = Right(right)
    }
}

data class Left<T>(val a: T) : Either<T, Nothing>() {
    override val isRight: Boolean
        get() = false
    override val isLeft: Boolean
        get() = true

    override fun toString(): String {
        return "Left($a)"
    }
}

data class Right<T>(val b: T) : Either<Nothing, T>() {
    override val isRight: Boolean
        get() = true
    override val isLeft: Boolean
        get() = false

    override fun toString(): String {
        return "Right($b)"
    }
}