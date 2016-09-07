import java.io.InputStream
import java.util.*

/**
 * Implementation of IIntProvider based on java.util.Scanner
 */
class ScannerIntProvider(stream : InputStream) : IIntProvider {
    val scanner : Scanner

    init {
        scanner = Scanner(stream)
    }

    override fun nextInt() = scanner.nextInt()
}