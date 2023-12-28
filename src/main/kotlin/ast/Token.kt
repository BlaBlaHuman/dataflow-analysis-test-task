package ast

class Token(val type: TokenType,
            val lexeme: String,
            val literal: String?,
            val line: Int) {
    override fun toString(): String {
        return "Line $line: (type $type, lexeme $lexeme, literal $literal)"
    }
}