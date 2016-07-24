/**
 * An expression.
 */
interface Expr {
    fun calc(env : Environment) : Int
}

/**
 * A const.
 */
class Const(private val value : Int) : Expr {
    override fun calc(env : Environment) : Int = value
}

/**
 * An integer variable.
 */
class Var(private val name : String) : Expr {
    override fun calc(env : Environment) : Int {
        val value = env.getVar(name)
        if (value != null) {
            return value
        }
        else {
            throw IllegalStateException("Variable $name not defined")
        }
    }
}

// Arithmetic operations

class Plus(private val left : Expr, private val right : Expr) : Expr {
    override fun calc(env : Environment) : Int = left.calc(env) + right.calc(env)
}

class Minus(private val left : Expr, private val right : Expr) : Expr {
    override fun calc(env : Environment) : Int = left.calc(env) - right.calc(env)

}

class Mult(private val left : Expr, private val right : Expr) : Expr {
    override fun calc(env : Environment) : Int = left.calc(env) * right.calc(env)
}

class Div(private val left : Expr, private val right : Expr) : Expr {
    override fun calc(env : Environment) : Int = left.calc(env) / right.calc(env)
}

class Mod(private val left : Expr, private val right : Expr) : Expr {
    override fun calc(env : Environment) : Int = left.calc(env) % right.calc(env)
}

// Comparison operators which return 1 or 0

class Eq(private val left : Expr, private val right : Expr) : Expr {
    override fun calc(env : Environment) : Int = if (left.calc(env) == right.calc(env)) 1 else 0
}

class Neq(private val left : Expr, private val right : Expr) : Expr {
    override fun calc(env : Environment) : Int = if (left.calc(env) != right.calc(env)) 1 else 0
}

class More(private val left : Expr, private val right : Expr) : Expr {
    override fun calc(env : Environment) : Int = if (left.calc(env) != right.calc(env)) 1 else 0
}

class Less(private val left : Expr, private val right : Expr) : Expr {
    override fun calc(env : Environment) : Int = if (left.calc(env) != right.calc(env)) 1 else 0
}

// Boolean operations, which take 0 for false and everything else for true and return 1 or 0.

class And(private val left : Expr, private val right : Expr) : Expr {
    override fun calc(env : Environment) : Int = if ((left.calc(env) != 0) && (right.calc(env) != 0)) 1 else 0
}

class Or(private val left : Expr, private val right : Expr) : Expr {
    override fun calc(env : Environment) : Int = if ((left.calc(env) != 0) || (right.calc(env) != 0)) 1 else 0
}
