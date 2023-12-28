class LiveVarsAnalyzer(programCfg: ProgramCfg) {
    private val lattice = MapLattice<CfgNode, Set<Expr.Variable>>(BooleanLattice())
    private val cfgNodes = programCfg.getNodes()

    fun getUnusedAssignments(): Set<CfgNode> {
        val liveVars = simpleFixpointSolver()
        return cfgNodes.filter { node ->
            node.data is Stmt.Declaration && !join(node, liveVars).contains(node.data.name)
        }.toSet()
    }

    private fun transfer(node: CfgNode, state: Set<Expr.Variable>): Set<Expr.Variable> {
        return when (node.data) {
            is Stmt.Declaration ->
                state - node.data.name + node.data.expr.getIds()
            is Stmt.If ->
                state + node.data.cond.getIds()
            is Stmt.While ->
                state + node.data.cond.getIds()
            else -> state
        }
    }

    private fun simpleFixpointSolver(): Map<CfgNode, Set<Expr.Variable>> {
        var x = lattice.bottom
        var t: Map<CfgNode, Set<Expr.Variable>>
        do {
            t = x
            x = func(x)
        } while (x != t)
        return x
    }

    private fun func(state: Map<CfgNode, Set<Expr.Variable>>): Map<CfgNode, Set<Expr.Variable>> {
        return cfgNodes.fold(lattice.bottom) { acc, cur ->
            acc + (cur to transfer(cur, join(cur, state)))
        }
    }


    private fun join(node: CfgNode, state: Map<CfgNode, Set<Expr.Variable>>): Set<Expr.Variable> {
        val states = node.succ.map { state[it] }
        return states.fold(lattice.sublattice.bottom) { acc, cur ->
            lattice.sublattice.lub(acc, cur ?: lattice.sublattice.bottom)
        }
    }

}