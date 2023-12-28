package cfg

import ast.Stmt

class CfgNode(val data: Stmt,
              var pred: MutableSet<CfgNode> = mutableSetOf(),
              var succ: MutableSet<CfgNode> = mutableSetOf()
)