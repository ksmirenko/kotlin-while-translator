import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

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

    @Test fun `expr - vars and binary`() {
        val parser = Parser("var1 ~ 5 | var2b < 6 & x > 3 ! 1")
        assertEquals(
            BinOp(
                '|',
                BinOp('~', Var("var1"), Const(5)),
                BinOp(
                    '&',
                    BinOp('<', Var("var2b"), Const(6)),
                    BinOp(
                        '!',
                        BinOp('>', Var("x"), Const(3)),
                        Const(1)
                    )
                )
            ),
            parser.parseExpr()
        )
    }

    @Test fun `expr - vars, negative numbers, brackets`() {
        val parser = Parser("-51 - ( 2 * ( 1 - -4 ) / ( aa & b ) )")
        assertEquals(
            BinOp(
                '-',
                Const(-51),
                BinOp(
                    '/',
                    BinOp(
                        '*',
                        Const(2),
                        BinOp('-', Const(1), Const(-4))
                    ),
                    BinOp('&', Var("aa"), Var("b"))
                )
            ),
            parser.parseExpr()
        )
    }

    @Test fun `stmt - skip`() {
        val parser = Parser("SKIP")
        assertEquals(Skip, parser.parseStmt())
    }

    @Test fun `stmt - do not process unexpected tokens at the end`() {
        val parser = Parser("SKIP 42")
        assertEquals(Skip, parser.parseStmt())
    }

    @Test fun `stmt - assign`() {
        assertEquals(
            Assign("a", Const(1)),
            Parser("a := 1").parseStmt()
        )
    }

    @Test fun `stmt - if (simple)`() {
        val parser = Parser("IF 5 > 2 { SKIP } { SKIP }")
        assertEquals(If(BinOp('>', Const(5), Const(2)), Skip, Skip), parser.parseStmt())
    }

    @Test fun `stmt - while (simple)`() {
        val parser = Parser("WHILE 2 > 5 { SKIP }")
        assertEquals(While(BinOp('>', Const(2), Const(5)), Skip), parser.parseStmt())
    }

    @Test fun `stmt - seq`() {
        assertEquals(Seq(Skip, Seq(Skip, Write(Var("a")))), Parser("SKIP ; SKIP ; WRITE a").parseStmt())
    }

    @Test fun `stmt - if and while inside if`() {
        val parser = Parser("IF 1 ! ( 5 < 4 ) { A := 5 - 1 } { WHILE B { READ A } }")
        assertEquals(
            If(
                BinOp('!', Const(1), BinOp('<', Const(5), Const(4))),
                Assign("A", BinOp('-', Const(5), Const(1))),
                While(
                    Var("B"),
                    Read("A")
                )
            ),
            parser.parseStmt()
        )
    }

    @Test fun `stmt - read, write, assign, while (complex body)`() {
        val parser = Parser(
            """READ n ;
            f := 1 ;
            WHILE n > 0 {
            f := f * n ;
            n := n - 1
            } ;
            WRITE f""")
        assertEquals(
            Seq(
                Read("n"),
                Seq(
                    Assign("f", Const(1)),
                    Seq(
                        While(
                            BinOp('>', Var("n"), Const(0)),
                            Seq(
                                Assign("f", BinOp('*', Var("f"), Var("n"))),
                                Assign("n", BinOp('-', Var("n"), Const(1)))
                            )
                        ),
                        Write(Var("f"))
                    )
                )
            ),
            parser.parseStmt()
        )
    }

    @Test fun `stmt - error - semicolon`() {
        val parser = Parser("SKIP ;")
        assertFailsWith<IllegalArgumentException> { parser.parseStmt() }
    }

    @Test fun `stmt - error - unexpected var name`() {
        val parser = Parser("SKIP ; WASISTDAS")
        assertFailsWith<IllegalArgumentException> { parser.parseStmt() }
    }

    @Test fun `stmt - error - illegal var name`() {
        assertFailsWith<IllegalArgumentException> { Parser("1fas := 0").parseStmt() }
        assertFailsWith<IllegalArgumentException> { Parser("sdf_ := 0").parseStmt() }
    }
}