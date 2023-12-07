abstract class Stmt {
    abstract fun <R> accept(stmtVisitor: StmtVisitor<R>): R

    class Declaration(val name: Token, val expr: Expr): Stmt() {
        override fun <R> accept(stmtVisitor: StmtVisitor<R>): R {
            return stmtVisitor.visitDecl(this)
        }
    }

    class If(val cond: Expr, val thenBranch: StmtList): Stmt() {
        override fun <R> accept(stmtVisitor: StmtVisitor<R>): R {
            return stmtVisitor.visitIf(this)
        }
    }

    class While(val cond: Expr, val body: StmtList): Stmt() {
        override fun <R> accept(stmtVisitor: StmtVisitor<R>): R {
            return stmtVisitor.visitWhile(this)
        }
    }

    class StmtList(val stmts: List<Stmt>): Stmt() {
        override fun <R> accept(stmtVisitor: StmtVisitor<R>): R {
            return stmtVisitor.visitStmtList(this)
        }

    }
}