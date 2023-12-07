interface StmtVisitor<R> {
    fun visitIf(stmt: Stmt.If): R
    fun visitWhile(stmt: Stmt.While): R
    fun visitDecl(stmt: Stmt.Declaration): R
    fun visitStmtList(stmt: Stmt.StmtList): R
}