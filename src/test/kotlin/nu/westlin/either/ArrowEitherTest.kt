package nu.westlin.either

import arrow.core.Either
import arrow.core.getOrElse
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class ArrowEitherTest {

    object KnownError

    @Suppress("IMPLICIT_CAST_TO_ANY")
    @Test
    fun `Either when`() {
        val either: Either<KnownError, Int> = Either.Right(1)

        val result = when(either) {
            is Either.Left -> either.a
            is Either.Right -> either.b
        }
        assertThat(result).isEqualTo(1)
    }

    @Test
    fun `either left`() {
        val eitherLeft = Either.left("left")

        assertThat(eitherLeft.isLeft()).isTrue
        assertThat(eitherLeft.isRight()).isFalse
        assertThat(eitherLeft.fold({ it }, { "right" })).isEqualTo("left")
/*
        val a = when(eitherLeft) {
            is Left -> "L"
            is Right -> "R"
        }
*/
    }

    @Test
    fun `either right`() {
        val eitherRight = Either.right("right")

        assertThat(eitherRight.isLeft()).isFalse
        assertThat(eitherRight.isRight()).isTrue
        assertThat(eitherRight.fold({ "left" }, { it })).isEqualTo("right")
        assertThat(eitherRight.fold({ "left" }, { "right" })).isEqualTo("right")
    }

    @Test
    fun `get or else`() {
        assertThat(Either.right("right").getOrElse {"left"}).isEqualTo("right")
        assertThat(Either.left("left").getOrElse {"not right"}).isEqualTo("not right")
    }

    @Test
    fun `print it`() {
        assertThat(Either.right("right").toString()).isEqualTo("Right(right)")
        assertThat(Either.left("left").toString()).isEqualTo("Left(left)")
    }
}

