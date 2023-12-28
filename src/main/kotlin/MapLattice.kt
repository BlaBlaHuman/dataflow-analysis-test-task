class MapLattice<R, L>(val sublattice: Lattice<L>): Lattice<Map<R, L>> {
    override val bottom: Map<R, L> = emptyMap<R, L>().withDefault { sublattice.bottom }

    override fun lub(x: Map<R, L>, y: Map<R, L>): Map<R, L> {
        return x.keys.fold(y) { acc, key ->
            acc + (key to sublattice.lub(x.getValue(key), y.getValue(key)))
        }.withDefault { sublattice.bottom }
    }
}