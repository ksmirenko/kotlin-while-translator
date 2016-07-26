/**
 * Provides words from input one by one.
 * This class was implemented just for convenience.
 */
class CodeStream(rawCode : String) {
    private val code : List<String>

    private var codeCursor = 0

    init {
        code = rawCode.split(' ')
    }

    /**
     * Moves the cursor one symbol back.
     * Kinda workaround.
     */
    fun rollBack() = --codeCursor

    fun read() =
        try {
            code[codeCursor++]
        }
        catch (e: ArrayIndexOutOfBoundsException) {
            throw Exception("Unexpected end of stream")
        }

    fun isEmpty() = codeCursor >= code.size
}