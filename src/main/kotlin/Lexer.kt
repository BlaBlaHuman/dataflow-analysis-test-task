

class Lexer(private val source: String) {
    private var hadError = false
    private val tokens: MutableList<Token> = mutableListOf()
    private var start = 0
    private var current = 0
    private var line = 1
    private val keywords: HashMap<String, TokenType> = hashMapOf(
        Pair("while", TokenType.WHILE),
        Pair("if", TokenType.IF),
        Pair("end", TokenType.END),
    )

    fun scanTokens(): MutableList<Token> {
        while (current < source.length) {
            start = current
            scanToken()
        }

        tokens.add(Token(TokenType.EOF, "", null, line))
        return tokens
    }

    private fun scanToken() {
        when (val c: Char = advance()) {
            '(' -> addToken(TokenType.LBRACKET)
            ')' -> addToken(TokenType.RBRACKET)
            '-' -> addToken(TokenType.MINUS)
            '+' -> addToken(TokenType.PLUS)
            '*' -> addToken(TokenType.STAR)
            '/' -> addToken(TokenType.SLASH)
            '=' -> addToken(TokenType.EQUAL)
            '>' -> addToken(TokenType.GREATER)
            '<' -> addToken(TokenType.LESS)
            ' ', '\r', '\t' -> return
            '\n' -> line+=1
            else ->
                if (c.isDigit())
                    number()
                else if (c in 'a'..'z')
                    identifier()
                else
                    error(line, "Unexpected character: $c")
        }
    }

    private fun advance(): Char {
        return source[current++]
    }

    private fun peek(): Char? {
        return source.getOrNull(current)
    }

    private fun addToken(type: TokenType) {
        addToken(type, null)
    }

    private fun addToken(type: TokenType, literal: String?) {
        val text = source.substring(start, current)
        tokens.add(Token(type, text, literal, line))
    }

    private fun number() {
        while (peek()?.isDigit() == true) advance()

        addToken(TokenType.CONSTANT, source.substring(start, current))
    }

    private fun identifier() {
        while (peek() in 'a'..'z') advance()

        val text = source.substring(start, current)
        val keyword = keywords[text]

        if (keyword != null) {
            addToken(keyword)
            return
        }

        if (text.length == 1) {
            addToken(TokenType.IDENTIFIER)
            return
        }

        error(line, "Unknown identifier: $text")
    }

    private fun error(errLine: Int, message: String) {
        System.err.println(
            "[line $errLine] Error: $message"
        )
        hadError = true
    }
}