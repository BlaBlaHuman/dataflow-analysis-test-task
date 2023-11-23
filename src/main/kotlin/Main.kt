import java.io.File

fun main(args: Array<String>) {
    val fileName = args.getOrNull(0) ?: return
    val code = File(fileName).readText()
    Lexer(code).scanTokens().forEach {
        println(it)
    }
}