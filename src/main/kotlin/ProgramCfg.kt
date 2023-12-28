class ProgramCfg(val entryNodes: MutableSet<CfgNode>, val exitNodes: MutableSet<CfgNode>) {

    fun getNodes(): Set<CfgNode> {
        fun visitNodes(n: CfgNode, visited: MutableSet<CfgNode> = mutableSetOf()): MutableSet<CfgNode>  {
            if (!visited.contains(n)) {
                visited += n
                n.succ.forEach {
                    visitNodes(it, visited)
                }
            }
            return visited
        }

        return entryNodes.flatMap {
            visitNodes(it)
        }.toSet()
    }
}