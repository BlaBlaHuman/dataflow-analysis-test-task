class Parser(private val tokens: List<Token>) {
    private var current: Int = 0

    fun parse(): Stmt.StmtList {
        val statements = mutableListOf<Stmt>()
        while (!isAtEnd()) {
            statements.add(statement() ?: continue)
        }
        return Stmt.StmtList(statements)
    }

    private fun statement(): Stmt? {
        try {
            if (match(TokenType.IF))
                return ifStatement()
            if (match(TokenType.WHILE))
                return whileStatement()
            return declarationStatement()
        }
        catch (e: ParseError) {
            System.err.println("${e.token} ${e.message}")
            synchronize()
            return null
        }
    }

    private fun whileStatement(): Stmt {
        val condition = expression()
        val statements = mutableListOf<Stmt>()
        while (!check(TokenType.END) and !isAtEnd()) {
            statements.add(statement() ?: continue)
        }
        consume(TokenType.END, "Expected end keyword")
        return Stmt.While(condition, Stmt.StmtList(statements))
    }


    private fun ifStatement(): Stmt {
        val condition = expression()
        val statements = mutableListOf<Stmt>()
        while (!check(TokenType.END) and !isAtEnd()) {
            statements.add(statement() ?: continue)
        }
        consume(TokenType.END, "Expected end keyword")
        return Stmt.If(condition, Stmt.StmtList(statements))
    }

    private fun declarationStatement(): Stmt {
        val name = consume(TokenType.IDENTIFIER, "Expected variable name.")
        if (match(TokenType.EQUAL)) {
            return Stmt.Declaration(Expr.Variable(name), expression())
        }
        throw error(peek(), "Expected equal sign")
    }

    private fun expression(): Expr {
        return comparison()
    }

    private fun comparison(): Expr {
        var expr: Expr = term()
        while (match(TokenType.GREATER, TokenType.LESS)) {
            val operator = previous()
            val right: Expr = term()
            expr = Expr.Binary(expr, operator, right)
        }
        return expr
    }

    private fun term(): Expr {
        var expr: Expr = factor()
        while (match(TokenType.MINUS, TokenType.PLUS)) {
            val operator = previous()
            val right: Expr = factor()
            expr = Expr.Binary(expr, operator, right)
        }
        return expr
    }

    private fun factor(): Expr {
        var expr: Expr = unary()
        while (match(TokenType.SLASH, TokenType.STAR)) {
            val operator = previous()
            val right: Expr = unary()
            expr = Expr.Binary(expr, operator, right)
        }
        return expr
    }

    private fun unary(): Expr {
        if (match(TokenType.MINUS)) {
            val operator = previous()
            val right = unary()
            return Expr.Unary(operator, right)
        }

        return primary()
    }

    private fun primary(): Expr {
        if (match(TokenType.CONSTANT)) {
            return Expr.Literal(previous().literal ?: throw error(previous(), "Constant literal is null"))
        }

        if (match(TokenType.IDENTIFIER)) {
            return Expr.Variable(previous())
        }

        if (match(TokenType.LBRACKET)) {
            val expr = expression()
            consume(TokenType.RBRACKET, "Expected ')' after expression.")
            return Expr.Grouping(expr)
        }

        throw error(peek(), "Expected expression.")
    }

    private fun consume(type: TokenType, message: String): Token {
        if (check(type)) return advance()
        throw error(peek(), message)
    }

    private fun error(token: Token, message: String): ParseError {
        return ParseError(token, message)
    }

    private fun synchronize() {
        advance()
        while (!isAtEnd()) {
            when (peek().type) {
                TokenType.IF, TokenType.WHILE, TokenType.END -> return
                else -> advance()
            }
        }
    }

    private fun match(vararg types: TokenType): Boolean {
        for (type in types) {
            if (check(type)) {
                advance()
                return true
            }
        }
        return false
    }

    private fun check(type: TokenType): Boolean {
        return if (isAtEnd()) false else peek().type === type
    }

    private fun advance(): Token {
        if (!isAtEnd()) current++
        return previous()
    }

    private fun isAtEnd(): Boolean {
        return peek().type === TokenType.EOF
    }

    private fun peek(): Token {
        return tokens[current]
    }

    private fun previous(): Token {
        return tokens[current - 1]
    }

    private class ParseError(val token: Token, message: String) : RuntimeException(message)
}