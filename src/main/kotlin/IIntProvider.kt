/**
 * Provides integers in a stream. Is used by Environment.
 */
interface IIntProvider {
    fun nextInt() : Int
}