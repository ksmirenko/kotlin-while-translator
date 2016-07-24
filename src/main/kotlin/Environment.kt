class Environment {
    /**
     * Gets the value of a variable.
     */
    fun getVar(name : String) : Int? = null

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
    }

}