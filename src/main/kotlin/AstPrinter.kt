class AstPrinter(private val tabSize: Int): ExprVisitor<String>, StmtVisitor<String> {
    private fun printExpr(expr: Expr): String {
        return expr.accept(this)
    }

    fun printStmt(stmt: Stmt): String {
        return stmt.accept(this)
    }
    override fun visitBinary(expr: Expr.Binary): String {
        return parenthesize(expr.operator.lexeme,
                        expr.left, expr.right)
    }

    override fun visitGrouping(expr: Expr.Grouping): String {
        return parenthesize("group", expr.expr)
    }

    override fun visitLiteral(expr: Expr.Literal): String {
        return expr.value
    }

    override fun visitUnary(expr: Expr.Unary): String {
        return parenthesize(expr.operator.lexeme, expr.expr)
    }

    override fun visitVariable(expr: Expr.Variable): String {
        return expr.name.lexeme
    }

    override fun visitIf(stmt: Stmt.If): String {
        return "if ${printExpr(stmt.cond)}\n" +
                printStmt(stmt.thenBranch).lines().joinToString("\n") { " ".repeat(tabSize) + it }
    }

    override fun visitWhile(stmt: Stmt.While): String {
        return "while ${printExpr(stmt.cond)}\n" +
                printStmt(stmt.body).lines().joinToString("\n") { " ".repeat(tabSize) + it }
    }

    override fun visitDecl(stmt: Stmt.Declaration): String {
        return "decl ${stmt.name.lexeme} ${this.printExpr(stmt.expr)}"
    }

    override fun visitStmtList(stmt: Stmt.StmtList): String {
        return stmt.stmts.joinToString("\n") { "|---> ${printStmt(it)}" }
    }

    private fun parenthesize(name: String, vararg exprs: Expr): String {
        return "($name " + exprs.joinToString(" ") { it.accept(this) } + ")"
    }

}