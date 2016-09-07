/**
 * An expression.
 */
interface Expr {
    fun calc(env : Environment) : Int
}

/**
 * A const.
 */
class Const(val value : Int) : Expr {
    override fun calc(env : Environment) : Int = value

    override fun equals(other : Any?) =
        other is Const && other.value.equals(value)

    override fun hashCode() = value
}

/**
 * An integer variable.
 */
class Var(val name : String) : Expr {
    override fun calc(env : Environment) : Int {
        val value = env.getVar(name)
        if (value != null) {
            return value
        }
        else {
            throw IllegalStateException("Variable $name not defined")
        }
    }

    override fun equals(other : Any?) =
        other is Var && other.name.equals(name)

    override fun hashCode() = name.hashCode()
}

class BinOp(val opType : Char, val left : Expr, val right : Expr) : Expr {
    override fun calc(env : Environment) : Int {
        val leftVal = left.calc(env)
        val rightVal = right.calc(env)
        return when (opType) {
            '+' -> leftVal + rightVal
            '-' -> leftVal - rightVal
            '*' -> leftVal * rightVal
            '/' -> leftVal / rightVal
            '%' -> leftVal % rightVal
            '~' -> if (leftVal == rightVal) 1 else 0
            '!' -> if (leftVal != rightVal) 1 else 0
            '>' -> if (leftVal > rightVal) 1 else 0
            '<' -> if (leftVal < rightVal) 1 else 0
            '&' -> if (leftVal != 0 && rightVal != 0) 1 else 0
            '|' -> if (leftVal != 0 || rightVal != 0) 1 else 0
            else -> {
                throw IllegalStateException()
            }
        }
    }

    override fun equals(other : Any?) =
        other is BinOp && other.opType.equals(opType)
            && other.left.equals(left) && other.right.equals(right)

    override fun hashCode() : Int {
        var result = opType.hashCode()
        result = 31 * result + left.hashCode()
        result = 31 * result + right.hashCode()
        return result
    }
}