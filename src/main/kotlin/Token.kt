class Token(private val type: TokenType,
            private val lexeme: String,
            private val literal: String?,
            private val line: Int) {
    override fun toString(): String {
        return "$line: $type $lexeme $literal"
    }
}