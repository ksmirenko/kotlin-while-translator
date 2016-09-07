import java.io.PrintStream
import java.util.*

class Environment(private val inIntProvider : IIntProvider, private val outPrintStream : PrintStream) {
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
    fun readInt() : Int = inIntProvider.nextInt()

    /**
     * Writes the number to output stream.
     */
    fun writeInt(number : Int) {
        outPrintStream.println(number.toString())
    }

}