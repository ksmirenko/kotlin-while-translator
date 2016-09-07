import java.io.PrintStream

class ListIntProvider(streamList : List<Int>) : IIntProvider {
    private val streamListIterator : Iterator<Int>

    init {
        streamListIterator = streamList.iterator()
    }

    constructor() : this(emptyList())

    override fun nextInt() = streamListIterator.next()
}

class StringPrintStream() : PrintStream(System.out) {
    val outStringBuilder = StringBuilder()

    override fun print(number : Int) {
        outStringBuilder.append(number)
    }

    override fun print(string : String) {
        outStringBuilder.append(string)
    }

    override fun println(number : Int) {
        outStringBuilder.append("${number.toString()}\n")
    }

    override fun println(string : String) {
        outStringBuilder.append("$string\n")
    }

    fun getContents() = outStringBuilder.toString()
}