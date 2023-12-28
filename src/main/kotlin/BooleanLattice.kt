class BooleanLattice<R>: Lattice<Set<R>> {
    override val bottom: Set<R> = emptySet()

    override fun lub(x: Set<R>, y: Set<R>): Set<R> {
        return x + y
    }
}