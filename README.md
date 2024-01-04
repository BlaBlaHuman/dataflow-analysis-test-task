# dataflow-analysis-test-task

## Usage
```
./gradlew run --args="<codepath> <command>"

Options:
  -h, --help  Show this message and exit

Arguments:
  <codepath>  Path to your code file

Commands:
  ast  Generate and print AST for the given program
  lva  Analyze unused assignments using live variables analysis
  
Example:
./gradlew run --args="examples/sampleCode ast"

```