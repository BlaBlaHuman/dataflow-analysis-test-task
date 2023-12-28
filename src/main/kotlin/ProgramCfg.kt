class ProgramCfg(val entryNodes: Set<CfgNode>, val exitNodes: Set<CfgNode>) {
    val allNodes: Set<CfgNode>
    init {
        fun visitNodes(n: CfgNode, visited: MutableSet<CfgNode> = mutableSetOf()): MutableSet<CfgNode>  {
            if (!visited.contains(n)) {
                visited += n
                n.succ.forEach {
                    visitNodes(it, visited)
                }
            }
            return visited
        }

        allNodes = entryNodes.flatMap {
            visitNodes(it)
        }.toSet()
    }
}