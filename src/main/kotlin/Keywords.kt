/**
 * All keywords and operators of While language.
 */
enum class Keyword(val v : String) {
    // expressions

    PLUS("+"),
    MINUS("-"),
    MUL("*"),
    DIV("/"),
    MOD("%"),

    EQ("=="),
    NEQ("!="),
    MORETHAN(">"),
    LESSTHAN("<"),

    AND("&"),
    OR("|"),

    // statements

    SKIP("SKIP"),
    ASSIGN(":="),
    READ("READ"),
    WRITE("WRITE"),
    SEQ(";"),
    IF("IF"),
    WHILE("WHILE")
}