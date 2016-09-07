import org.junit.Test
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/**
 * Tests interpretation of statements.
 */
class ExecTest {
    @Test fun `exec - skip`() {
        val code = "SKIP"
        val intProvider = ListIntProvider()
        val outStream = StringPrintStream()
        val environment = Environment(intProvider, outStream)
        Parser(code).parseStmt().exec(environment)
        assertEquals("", outStream.getContents())
    }

    @Test fun `exec - seq, assign, write`() {
        val code = "A := 5 ; WRITE A"
        val intProvider = ListIntProvider()
        val outStream = StringPrintStream()
        val environment = Environment(intProvider, outStream)
        Parser(code).parseStmt().exec(environment)
        assertEquals("5\n", outStream.getContents())
        assertEquals(5, environment.getVar("A"))
    }

    @Test fun `exec - seq, read, assign, write`() {
        val code = "READ A ; WRITE A ; A := A - 6 ; WRITE A"
        val intProvider = ListIntProvider(listOf(5))
        val outStream = StringPrintStream()
        val environment = Environment(intProvider, outStream)
        Parser(code).parseStmt().exec(environment)
        assertEquals("5\n-1\n", outStream.getContents())
        assertEquals(-1, environment.getVar("A"))
    }

    @Test fun `exec - err - unexpected end of stream`() {
        val code = "READ A ; READ B"
        val intProvider = ListIntProvider(listOf(0))
        val outStream = StringPrintStream()
        val environment = Environment(intProvider, outStream)
        assertFailsWith<NoSuchElementException> { Parser(code).parseStmt().exec(environment) }
    }
}