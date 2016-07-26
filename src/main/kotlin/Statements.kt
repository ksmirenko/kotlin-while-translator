/**
 * A statement.
 */
interface Stmt {
    fun exec(env : Environment)
}

/**
 * Does nothing.
 */
object Skip : Stmt {
    override fun exec(env : Environment) {
    }
}

/**
 * [name] := [value]
 */
class Assign(private val name : String, private val value : Expr) : Stmt {
    override fun exec(env : Environment) = env.setVar(name, value.calc(env))
}

/**
 * Reads a number from input stream and write to [varName].
 */
class Read(private val varName : String) : Stmt {
    override fun exec(env : Environment) {
        val value = env.readInt()
        if (value != null) {
            env.setVar(varName, value)
        }
        else {
            throw IllegalStateException("Input stream was empty")
        }
    }
}

/**
 * Writes the value of [expr] to output stream.
 */
class Write(private val expr : Expr) : Stmt {
    override fun exec(env : Environment) = env.writeInt(expr.calc(env))
}

/**
 * Executes [firstStmt] and then [secondStmt].
 */
class Seq(private val firstStmt : Stmt, private val secondStmt : Stmt) : Stmt {
    override fun exec(env : Environment) {
        firstStmt.exec(env)
        secondStmt.exec(env)
    }
}

/**
 * If [condition] is non-zero, executes [stmtTrue], otherwise executes [stmtFalse].
 */
class If(private val condition : Expr, private val stmtTrue : Stmt, private val stmtFalse : Stmt) : Stmt {
    override fun exec(env : Environment) = (if (condition.calc(env) != 0) stmtTrue else stmtFalse).exec(env)

}

/**
 * Whie [condition] is non-zero, executes [stmt].
 */
class While(private val condition : Expr, private val stmt : Stmt) : Stmt {
    override fun exec(env : Environment) {
        while (condition.calc(env) != 0) {
            stmt.exec(env)
        }
    }
}