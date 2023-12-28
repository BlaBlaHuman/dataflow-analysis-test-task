interface Lattice<R> {
    val bottom: R

    fun lub(x: R, y: R): R
}