import java.io.OutputStream
import java.util.*

class Environment(private val inScanner : Scanner, private val outStream : OutputStream) {
    private val map = HashMap<String, Int>()

    /**
     * Gets the value of a variable.
     */
    fun getVar(name : String) : Int? = map[name]

    /**
     * Sets the variable [name] to [value].
     */
    fun setVar(name : String, value : Int) {
    }

    /**
     * Reads the next integer from input stream.
     * @return The read integer number or null, if stream is empty.
     */
    fun readInt() : Int? = null

    /**
     * Writes the number to output stream.
     */
    fun writeInt(number : Int) {
        Scanner(System.`in`)

    }

}