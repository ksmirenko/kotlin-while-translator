/**
 * Provides words from input one by one.
 * This class was implemented just for convenience.
 */
class CodeStream(rawCode : String) {
    private val code : List<String>

    private var codeCursor = 0

    init {
        code = rawCode.split(' ', '\n', '\t').filter { it != "" }
    }

    /**
     * Moves the cursor one symbol back.
     */
    fun rollBack() = --codeCursor

    /**
     * Returns the next element without moving the stream cursor forward.
     */
    fun lookAhead() =
        try {
            code[codeCursor]
        }
        catch (e : ArrayIndexOutOfBoundsException) {
            throw Exception("Unexpected end of stream")
        }

    fun read() =
        try {
            code[codeCursor++]
        }
        catch (e : ArrayIndexOutOfBoundsException) {
            throw Exception("Unexpected end of stream")
        }

    fun isEmpty() = codeCursor >= code.size
}