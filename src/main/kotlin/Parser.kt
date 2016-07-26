import java.util.*

/**
 * Parser of While code.
 */
class Parser(rawCode : String) {
    val codeStream : CodeStream
    var ast : Stmt = Skip

    private val keywords = Keyword.values().map { it.v }
    private val binOps = hashMapOf(
        '*' to 3, '/' to 3, '%' to 3,
        '+' to 2, '-' to 2,
        '>' to 1, '<' to 1,
        '~' to 0, '!' to 0,
        '&' to -1,
        '|' to -2
    )

    init {
        codeStream = CodeStream(rawCode)
    }

    fun parseStmt() : Stmt {
        if (codeStream.isEmpty())
            throw IllegalArgumentException("Unexpected end of stream")
        val stmt = when (codeStream.read()) {
            Keyword.SKIP.v ->
                Skip
            Keyword.READ.v ->
                Read(parseVarName())
            Keyword.WRITE.v ->
                Write(parseExpr())
            Keyword.ASSIGN.v ->
                Assign(parseVarName(), parseExpr())
            Keyword.SEQ.v ->
                throw IllegalArgumentException("Unexpected semicolon")
            Keyword.IF.v ->
                If(parseExpr(), parseStmt(), parseStmt())
            Keyword.WHILE.v ->
                While(parseExpr(), parseStmt())
            else -> {
                throw IllegalArgumentException("Syntax error")
            }
        }
        if (!codeStream.isEmpty()) {
            if (codeStream.read() == Keyword.SEQ.v)
                return Seq(stmt, parseStmt())
            else
                throw IllegalArgumentException("Syntax error")
        }
        else
            return stmt
    }

    fun parseExpr() : Expr {
        val exprOutputStack = Stack<Expr>()
        val workStack = Stack<Char>()

        fun pushOperator(op : Char) {
            try {
                assert(op in binOps.keys)
                val rightOperand = exprOutputStack.pop()
                val leftOperand = exprOutputStack.pop()
                exprOutputStack.push(BinOp(op, leftOperand, rightOperand))
            }
            catch (e : EmptyStackException) {
                throw IllegalArgumentException("Syntax error")
            }
        }

        // shunting-yard algorithm
        loop@while (true) {
            if (codeStream.isEmpty())
                break
            val curToken = codeStream.read()
            val curBinOpIndex = binOps.keys.indexOf(curToken[0])
            when {
                curBinOpIndex > -1 -> {  // binary operator
                    while (workStack.isNotEmpty() && workStack.peek() != '(' &&
                        binOps[curToken[0]]!! <= binOps[workStack.peek()]!!) {
                        pushOperator(workStack.pop())

                    }
                    workStack.push(curToken[0])
                }
                curToken.isValidVarName() -> {
                    exprOutputStack.push(Var(curToken))
                }
                curToken == "(" -> {
                    workStack.push('(')
                }
                curToken == ")" -> {
                    try {
                        while (workStack.peek() != '(') {
                            val topBinOp = workStack.pop()
                            assert(topBinOp in binOps.keys)
                            pushOperator(topBinOp)
                        }
                    }
                    catch (e : EmptyStackException) {
                        throw IllegalArgumentException("Mismatched parentheses")
                    }
                    workStack.pop()
                }
                else -> {
                    try {
                        exprOutputStack.push(Const(curToken.toInt()))
                    }
                    catch (e : NumberFormatException) { // end of expression
                        // finish parsing expression
                        codeStream.rollBack()
                        break@loop
                    }
                }
            }
        }

        while (workStack.isNotEmpty()) {
            val topOp = workStack.pop()
            if (topOp == '(')
                throw IllegalArgumentException("Mismatched parentheses")
            pushOperator(topOp)
        }
        if (exprOutputStack.size != 1)
            throw IllegalArgumentException("Syntax error")
        return exprOutputStack.pop()
    }

    private fun parseVarName() : String {
        val varName : String = codeStream.read()
        if (!varName.isValidVarName())
            throw IllegalArgumentException("A variable name can only contain letters " +
                "or digits and start with a letter.")
        return varName
    }


    private fun String.isValidVarName() =
        fold(this[0].isLetter(), { v, c -> v && c.isLetterOrDigit() }) &&
            this !in keywords
}