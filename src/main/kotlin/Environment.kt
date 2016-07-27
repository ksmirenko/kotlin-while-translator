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
        map.put(name, value)
    }

    /**
     * Reads the next integer from input stream.
     */
    fun readInt() : Int = inScanner.nextInt()

    /**
     * Writes the number to output stream.
     */
    fun writeInt(number : Int) {
        outStream.write(number)
    }

}