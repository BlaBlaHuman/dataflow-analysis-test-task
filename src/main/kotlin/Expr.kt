abstract class Expr {
    abstract fun <R> accept(exprVisitor: ExprVisitor<R>): R

    class Binary (val left: Expr, val operator: Token, val right: Expr) : Expr() {
        override fun <R> accept(exprVisitor: ExprVisitor<R>): R {
            return exprVisitor.visitBinary(this)
        }

    }
    class Grouping (val expr: Expr) : Expr() {
        override fun <R> accept(exprVisitor: ExprVisitor<R>): R {
            return exprVisitor.visitGrouping(this)
        }
    }

    class Literal (val value: String) : Expr() {
        override fun <R> accept(exprVisitor: ExprVisitor<R>): R {
            return exprVisitor.visitLiteral(this)
        }
    }

    class Unary (val operator: Token, val expr: Expr): Expr() {
        override fun <R> accept(exprVisitor: ExprVisitor<R>): R {
            return exprVisitor.visitUnary(this)
        }
    }

    class Variable (val name: Token): Expr() {
        override fun <R> accept(exprVisitor: ExprVisitor<R>): R {
            return exprVisitor.visitVariable(this)
        }

    }
}