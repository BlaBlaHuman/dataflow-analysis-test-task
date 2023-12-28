import analysis.LiveVarsAnalyzer
import ast.AstPrinter
import ast.Stmt
import cfg.CfgGenerator
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.findOrSetObject
import com.github.ajalt.clikt.core.requireObject
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.help
import com.github.ajalt.clikt.parameters.types.file
import parser.Lexer
import parser.Parser

class Parser: CliktCommand() {
    private val codePath by argument().file(mustExist = true).help("Path to your code file")
    private val config by findOrSetObject { mutableMapOf<String, Any>() }
    override fun run() {
        val programCode = codePath.readText()
        val programTokens = Lexer(programCode).scanTokens()
        val programAST = Parser(programTokens).parse()
        config["AST"] = programAST
    }
}

class GenerateAST: CliktCommand(name = "ast", help = "Generate and print AST for the given program") {
    private val config by requireObject<Map<String, Any>>()
    override fun run() {
        val programAst = config["AST"] as Stmt.StmtList
        val astPrinter = AstPrinter(5)
        echo(astPrinter.printStmt(programAst))
    }
}

class UnusedAssignmentsAnalysis: CliktCommand(name = "lva", help = "Analyze unused assignments using live variables analysis") {
    private val config by requireObject<Map<String, Any>>()

    override fun run() {
        val programAst = config["AST"] as Stmt.StmtList
        val printer = AstPrinter()
        LiveVarsAnalyzer(CfgGenerator().generateCfg(programAst)).getUnusedAssignments().forEach {
            println(printer.visitDecl(it.data as Stmt.Declaration))
        }
    }
}

fun main(args: Array<String>) = Parser().subcommands(GenerateAST(), UnusedAssignmentsAnalysis()).main(args)
