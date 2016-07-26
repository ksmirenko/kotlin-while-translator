import org.junit.Test
import kotlin.test.assertEquals

class ParserTest {
    @Test fun `expr - simple`() {
        val parser = Parser("1 + 2")
        assertEquals(BinOp('+', Const(1), Const(2)), parser.parseExpr())
    }

    @Test fun `expr - consts and all arithmetic`() {
        val parser = Parser("1 + 2 * 3 - 20 % 7 / 2")
        assertEquals(
            BinOp(
                '-',
                BinOp(
                    '+',
                    Const(1),
                    BinOp('*', Const(2), Const(3))),
                BinOp(
                    '/',
                    BinOp('%', Const(20), Const(7)),
                    Const(2)
                )

            ),
            parser.parseExpr()
        )
    }
}