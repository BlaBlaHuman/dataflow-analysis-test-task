import java.io.File

fun main(args: Array<String>) {
    val programCode = File(args.getOrNull(0) ?: return).readText()
    val astPrinter = AstPrinter(5)

    println("PROGRAM AST:")
    val programTokens = Lexer(programCode).scanTokens()
    val programAST = Parser(programTokens).parse()
    println(astPrinter.printStmt(programAST))


    println("UNUSED ASSIGNMENTS:")
    LiveVarsAnalyzer(
        CfgGenerator().generateCfg(programAST)
    )
        .getUnusedAssignments()
        .forEach {
        println(astPrinter.visitDecl(it.data as Stmt.Declaration))
    }
}