package com.unimib.oases.util.datastructure.binarytree

suspend fun Tree.evaluate(
    onManual: suspend (ManualNode) -> Boolean,
    onLeafReached: (LeafNode) -> Unit
){
    // true -> left, false -> right
    var node: Node = this.root
    while (true) {
        node = when (node) {
            is AutoNode -> {
                if (node.predicate()){
                    node.children.left
                }
                else{
                    node.children.right
                }
            }
            is ManualNode -> {
                val answer = onManual(node)
                if (answer)
                    node.children.left
                else
                    node.children.right
            }
            is LeafNode -> {
                onLeafReached(node)
                break
            }
            else -> break
        }
    }
}

sealed interface Tree {
    val id: ComplaintId
    val root: ManualNode
}

enum class ComplaintId(val id: String) {
    DIARRHEA("diarrhea")
}