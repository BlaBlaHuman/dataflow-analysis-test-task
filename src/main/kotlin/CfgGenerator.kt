class CfgGenerator: StmtVisitor<Pair<MutableSet<CfgNode>, MutableSet<CfgNode>>> {
    fun generateCfg(ast: Stmt.StmtList): Pair<MutableSet<CfgNode>, MutableSet<CfgNode>> {
        return ast.accept(this)
    }

    override fun visitIf(stmt: Stmt.If): Pair<MutableSet<CfgNode>, MutableSet<CfgNode>> {
        val firstNode = CfgNode(stmt)
        val body = stmt.thenBranch.accept(this)
        firstNode.succ.addAll(body.first)
        body.first.forEach { it.pred.add(firstNode) }
        return Pair(mutableSetOf(firstNode), (body.second + firstNode).toMutableSet())
    }

    override fun visitWhile(stmt: Stmt.While): Pair<MutableSet<CfgNode>, MutableSet<CfgNode>> {
        val firstNode = CfgNode(stmt)
        val body = stmt.body.accept(this)
        firstNode.succ.addAll(body.first)
        body.first.forEach { it.pred.add(firstNode) }
        body.second.forEach { it.succ.add(firstNode) }
        return Pair(mutableSetOf(firstNode), mutableSetOf(firstNode))
    }

    override fun visitDecl(stmt: Stmt.Declaration): Pair<MutableSet<CfgNode>, MutableSet<CfgNode>> {
        val newNode = CfgNode(stmt)
        return Pair(mutableSetOf(newNode), mutableSetOf(newNode))
    }

    override fun visitStmtList(stmt: Stmt.StmtList): Pair<MutableSet<CfgNode>, MutableSet<CfgNode>> {
        val root = stmt.stmts.first().accept(this)
        var prev = root
        for (idx in 1..stmt.stmts.lastIndex) {
            val tmp = stmt.stmts[idx].accept(this)
            prev.second.forEach { it.succ.addAll(tmp.first) }
            tmp.first.forEach { it.pred.addAll(prev.second) }
            prev = tmp
        }
        return Pair(root.first, prev.second)
    }
}