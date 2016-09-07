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
class Assign(val name : String, val value : Expr) : Stmt {
    override fun exec(env : Environment) =
        env.setVar(name, value.calc(env))

    override fun equals(other : Any?) =
        other is Assign && other.name.equals(name) && other.value.equals(value)

    override fun hashCode() : Int {
        var result = name.hashCode()
        result = 31 * result + value.hashCode()
        return result
    }
}

/**
 * Reads a number from input stream and write to [varName].
 */
class Read(val varName : String) : Stmt {
    override fun exec(env : Environment) =
        env.setVar(varName, env.readInt())

    override fun equals(other : Any?) =
        other is Read && other.varName.equals(varName)

    override fun hashCode() = varName.hashCode()
}

/**
 * Writes the value of [expr] to output stream.
 */
class Write(val expr : Expr) : Stmt {
    override fun exec(env : Environment) =
        env.writeInt(expr.calc(env))

    override fun equals(other : Any?) =
        other is Write && other.expr.equals(expr)

    override fun hashCode() = expr.hashCode()
}

/**
 * Executes [firstStmt] and then [secondStmt].
 */
class Seq(val firstStmt : Stmt, val secondStmt : Stmt) : Stmt {
    override fun exec(env : Environment) {
        firstStmt.exec(env)
        secondStmt.exec(env)
    }

    override fun equals(other : Any?) =
        other is Seq && other.firstStmt.equals(firstStmt) &&
            other.secondStmt.equals(secondStmt)

    override fun hashCode() : Int {
        var result = firstStmt.hashCode()
        result = 31 * result + secondStmt.hashCode()
        return result
    }
}

/**
 * If [condition] is non-zero, executes [stmtTrue], otherwise executes [stmtFalse].
 */
class If(val condition : Expr, val stmtTrue : Stmt, val stmtFalse : Stmt) : Stmt {
    override fun exec(env : Environment) =
        (if (condition.calc(env) != 0) stmtTrue else stmtFalse).exec(env)

    override fun equals(other : Any?) =
        other is If && other.condition.equals(condition) &&
            other.stmtTrue.equals(stmtTrue) && other.stmtFalse.equals(stmtFalse)

    override fun hashCode() : Int {
        var result = condition.hashCode()
        result = 31 * result + stmtTrue.hashCode()
        result = 31 * result + stmtFalse.hashCode()
        return result
    }
}

/**
 * Whie [condition] is non-zero, executes [stmt].
 */
class While(val condition : Expr, val stmt : Stmt) : Stmt {
    override fun exec(env : Environment) {
        while (condition.calc(env) != 0) {
            stmt.exec(env)
        }
    }

    override fun equals(other : Any?) =
        other is While && other.condition.equals(condition) && other.stmt.equals(stmt)

    override fun hashCode() : Int {
        var result = condition.hashCode()
        result = 31 * result + stmt.hashCode()
        return result
    }
}