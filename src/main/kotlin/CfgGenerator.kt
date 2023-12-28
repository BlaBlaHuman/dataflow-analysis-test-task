class CfgGenerator: StmtVisitor<ProgramCfg> {
    fun generateCfg(ast: Stmt.StmtList): ProgramCfg {
        return ast.accept(this)
    }

    override fun visitIf(stmt: Stmt.If): ProgramCfg {
        val mainNode = CfgNode(stmt)
        val body = stmt.thenBranch.accept(this)
        addSucc(setOf(mainNode), body.entryNodes)
        addPred(body.entryNodes, setOf(mainNode))
        return ProgramCfg(mutableSetOf(mainNode), (body.exitNodes + mainNode).toMutableSet())
    }

    override fun visitWhile(stmt: Stmt.While): ProgramCfg {
        val mainNode = CfgNode(stmt)
        val body = stmt.body.accept(this)
        addSucc(setOf(mainNode), body.entryNodes)
        addPred(body.entryNodes, setOf(mainNode))
        addSucc(body.exitNodes, setOf(mainNode))
        return ProgramCfg(mutableSetOf(mainNode), mutableSetOf(mainNode))
    }

    override fun visitDecl(stmt: Stmt.Declaration): ProgramCfg {
        val newNode = CfgNode(stmt)
        return ProgramCfg(mutableSetOf(newNode), mutableSetOf(newNode))
    }

    override fun visitStmtList(stmt: Stmt.StmtList): ProgramCfg {
        val root = stmt.stmts.first().accept(this)
        var lastNode = root
        stmt.stmts.drop(1).forEach { node ->
            val currNode = node.accept(this)
            addSucc(lastNode.exitNodes, currNode.entryNodes)
            addPred(currNode.entryNodes, lastNode.exitNodes)
            lastNode = currNode
        }
        return ProgramCfg(root.entryNodes, lastNode.exitNodes)
    }

    private fun addSucc(nodes: Set<CfgNode>, succ: Set<CfgNode>) {
        nodes.forEach { it.succ.addAll(succ) }
    }

    private fun addPred(nodes: Set<CfgNode>, pred: Set<CfgNode>) {
        nodes.forEach { it.pred.addAll(pred) }
    }
}