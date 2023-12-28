abstract class Expr {
    abstract fun <R> accept(exprVisitor: ExprVisitor<R>): R
    abstract fun getIds(): Set<Variable>

    class Binary (val left: Expr, val operator: Token, val right: Expr) : Expr() {
        override fun <R> accept(exprVisitor: ExprVisitor<R>): R {
            return exprVisitor.visitBinary(this)
        }

        override fun getIds(): Set<Variable> {
            return left.getIds() + right.getIds()
        }

    }
    class Grouping (val expr: Expr) : Expr() {
        override fun <R> accept(exprVisitor: ExprVisitor<R>): R {
            return exprVisitor.visitGrouping(this)
        }

        override fun getIds(): Set<Variable> {
            return expr.getIds()
        }
    }

    class Literal (val value: String) : Expr() {
        override fun <R> accept(exprVisitor: ExprVisitor<R>): R {
            return exprVisitor.visitLiteral(this)
        }

        override fun getIds(): Set<Variable> {
            return mutableSetOf()
        }
    }

    class Unary (val operator: Token, val expr: Expr): Expr() {
        override fun <R> accept(exprVisitor: ExprVisitor<R>): R {
            return exprVisitor.visitUnary(this)
        }

        override fun getIds(): Set<Variable> {
            return expr.getIds()
        }
    }

    class Variable (val id: Token): Expr() {
        override fun <R> accept(exprVisitor: ExprVisitor<R>): R {
            return exprVisitor.visitVariable(this)
        }

        override fun getIds(): Set<Variable> {
            return mutableSetOf(this)
        }

        override fun equals(other: Any?): Boolean {
            return other is Variable && id.lexeme == other.id.lexeme
        }

        override fun hashCode(): Int {
            return id.lexeme.hashCode()
        }

    }
}