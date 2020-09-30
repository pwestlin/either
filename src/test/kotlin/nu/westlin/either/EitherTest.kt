package nu.westlin.either

import nu.westlin.either.EitherTest.Error.DivisionByZeroError
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

@Suppress("NonAsciiCharacters")
internal class EitherTest {

    @Test
    fun `either left`() {
        val eitherLeft = Either.left("left")

        assertThat(eitherLeft.isLeft).isTrue
        assertThat(eitherLeft.isRight).isFalse
        assertThat(eitherLeft.fold({ "left" }, { "right" })).isEqualTo("left")
    }

    @Test
    fun `either right`() {
        val eitherRight = Either.right("right")

        assertThat(eitherRight.isLeft).isFalse
        assertThat(eitherRight.isRight).isTrue
        assertThat(eitherRight.fold({ "left" }, { "right" })).isEqualTo("right")
        assertThat(eitherRight.fold({ "left" }, { "right" })).isEqualTo("right")
    }

    @Test
    fun `when either`() {
        val either = Either.left("left")
        val result = when (either) {
            is Left -> either.a
            is Right -> either.b
        }
        assertThat(result).isEqualTo("left")
    }

    @Test
    fun `get or else`() {
        assertThat(Either.right("right").getOrElse { "left" }).isEqualTo("right")
        assertThat(Either.left("left").getOrElse { "right" }).isEqualTo("right")
    }

    @Test
    fun `print it`() {
        assertThat(Either.right("right").toString()).isEqualTo("Right(right)")
        assertThat(Either.left("left").toString()).isEqualTo("Left(left)")
    }

    sealed class Error(val message: String) {
        object DivisionByZeroError : Error("/ by zero")
    }

    fun dividera(täljare: Int, nämnare: Int): Either<Error, Int> {
        return when (nämnare) {
            0 -> Either.left(DivisionByZeroError)
            else -> Either.right(täljare / nämnare)
        }
    }

    @Test
    fun `8 dividerat med 2`() {
        val result: Either<Error, Int> = dividera(8, 2)
        assertThat(result.isRight).isTrue
        assertThat(result.getOrElse { -1 }).isEqualTo(4)
    }

    @Test
    fun `8 dividerat med 0`() {
        val result: Either<Error, Int> = dividera(8, 0)
        assertThat(result.isLeft).isTrue
        assertThat(result.getOrElse { -1 }).isEqualTo(-1)
    }

    @Test
    fun `fold result`() {
        assertThat(dividera(10, 5).fold({ "Du kan inte dividera med noll fattaru väl!" }, { "fem" })).isEqualTo("fem")
        assertThat(
            dividera(1, 0).fold(
                { "Du kan inte dividera med noll fattaru väl!" },
                { "fem" })
        ).isEqualTo("Du kan inte dividera med noll fattaru väl!")
    }

    @Test
    fun `fold result med de faktiska värdena`() {
        assertThat(dividera(10, 5).fold({ it }, { it })).isEqualTo(2)
        assertThat(dividera(1, 0).fold({ it }, { it })).isEqualTo(DivisionByZeroError)
    }
}

