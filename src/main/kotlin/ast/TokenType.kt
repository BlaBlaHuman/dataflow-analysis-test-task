package ast

enum class TokenType {
    LBRACKET,
    RBRACKET,

    IDENTIFIER,
    CONSTANT,

    WHILE,
    IF,
    END,

    EQUAL,
    PLUS,
    MINUS,
    STAR,
    SLASH,
    LESS,
    GREATER,

    EOF
}