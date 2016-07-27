import java.util.*

/**
 * Parser of While code.
 */
class Parser(rawCode : String) {
    private val codeStream : CodeStream

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
        val curToken = codeStream.read()
        val stmt = when (curToken) {
            Keyword.SKIP.v ->
                Skip
            Keyword.READ.v ->
                Read(parseVarName())
            Keyword.WRITE.v ->
                Write(parseExpr())
            Keyword.IF.v -> {
                val ifCondition = parseExpr()
                parseParenthesis(true)
                val ifTrueStmt = parseStmt()
                parseParenthesis(false)
                parseParenthesis(true)
                val ifFalseStmt = parseStmt()
                parseParenthesis(false)
                If(ifCondition, ifTrueStmt, ifFalseStmt)
            }
            Keyword.WHILE.v -> {
                val whileCondition = parseExpr()
                parseParenthesis(true)
                val whileStmt = parseStmt()
                parseParenthesis(false)
                While(whileCondition, whileStmt)
            }
            else -> {
                // a var name, a semicolon or a syntax error
                if (curToken.isValidVarName() && !codeStream.isEmpty()
                    && codeStream.lookAhead() == Keyword.ASSIGN.v) {
                    codeStream.read()
                    Assign(curToken, parseExpr())
                }
                else
                    throw IllegalArgumentException("Syntax error")
            }
        }
        if (!codeStream.isEmpty() && codeStream.lookAhead() == Keyword.SEQ.v) {
            codeStream.read()
            return Seq(stmt, parseStmt())
        }
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
                curToken.length == 1 && curBinOpIndex > -1 -> {  // binary operator
                    while (workStack.isNotEmpty() && workStack.peek() != '('
                        && binOps[curToken[0]]!! <= binOps[workStack.peek()]!!) {
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

    private fun parseParenthesis(isLeft : Boolean) {
        if (codeStream.isEmpty() || codeStream.read() != if (isLeft) "{" else "}")
            throw IllegalArgumentException(
                "Syntax error: expected ${if (isLeft) "an opening" else "a closing"} parenthesis"
            )
    }

    private fun parseVarName() : String {
        val varName : String = codeStream.read()
        if (!varName.isValidVarName())
            throw IllegalArgumentException("A variable name can only contain letters " +
                "or digits and start with a letter.")
        return varName
    }


    private fun String.isValidVarName() =
        fold(this[0].isLetter(), { v, c -> v && c.isLetterOrDigit() })
            && this !in keywords
}