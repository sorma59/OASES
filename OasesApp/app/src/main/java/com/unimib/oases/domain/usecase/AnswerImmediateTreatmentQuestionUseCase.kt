package com.unimib.oases.domain.usecase

import com.unimib.oases.domain.model.complaint.binarytree.LeafNode
import com.unimib.oases.domain.model.complaint.binarytree.ManualNode
import com.unimib.oases.domain.model.complaint.binarytree.Tree
import com.unimib.oases.domain.model.complaint.binarytree.next
import com.unimib.oases.domain.model.complaint.binarytree.toImmediateTreatmentQuestionState
import com.unimib.oases.ui.screen.medical_visit.evaluation.EvaluationState
import com.unimib.oases.ui.screen.medical_visit.evaluation.ImmediateTreatmentQuestionState
import com.unimib.oases.ui.screen.medical_visit.evaluation.TreeSummary
import com.unimib.oases.ui.screen.medical_visit.evaluation.appendQuestion
import com.unimib.oases.ui.screen.medical_visit.evaluation.rebranch
import com.unimib.oases.util.replaceAt
import javax.inject.Inject

class AnswerImmediateTreatmentQuestionUseCase @Inject constructor() {
    operator fun invoke(answer: Boolean, node: ManualNode, tree: Tree, state: EvaluationState): EvaluationState{
        val nextNode = node.next(answer)

        val treeIndex = state.immediateTreatmentAlgorithms.indexOf(tree)

        return when (nextNode){
            is ManualNode -> {
                val updatedQuestions = state.immediateTreatmentQuestions
                    .elementAt(treeIndex)
                    .rebranch(node, answer)
                    .appendQuestion(ImmediateTreatmentQuestionState(nextNode))
                state.copy(
                    immediateTreatmentQuestions = updateImmediateTreatmentQuestions(
                        state,
                        treeIndex,
                        updatedQuestions,
                        shouldShowNextAlgorithm = false
                    ),
                    leaves = state.leaves.replaceAt(treeIndex, null)
                )
            }
            is LeafNode -> {
                val isLastAlgorithm =
                    state.immediateTreatmentAlgorithmsToShow == state.immediateTreatmentAlgorithms.size
                val isLastShownAlgorithm =
                    state.immediateTreatmentAlgorithmsToShow == treeIndex + 1
                val updatedList = state.immediateTreatmentQuestions
                    .elementAt(treeIndex)
                    .rebranch(node, answer)
                if (isLastAlgorithm)
                    state.copy(
                        immediateTreatmentQuestions = updateImmediateTreatmentQuestions(
                            state,
                            treeIndex,
                            updatedList,
                            shouldShowNextAlgorithm = false
                        ),
                        leaves = state.leaves.replaceAt(treeIndex, nextNode),
                        immediateTreatmentAlgorithmsToShow = calculateNumberOfTreesToShow(tree, state),
                        detailsQuestions = getDetailsQuestion(state),
                        detailsQuestionsToShow = if (state.detailsQuestionsToShow == 0) 1 else state.detailsQuestionsToShow
                    )
                else {
                    state.copy(
                        immediateTreatmentQuestions = updateImmediateTreatmentQuestions(
                            state,
                            treeIndex,
                            updatedList,
                            shouldShowNextAlgorithm = isLastShownAlgorithm
                        ),
                        leaves = state.leaves.replaceAt(treeIndex, nextNode),
                        immediateTreatmentAlgorithmsToShow = calculateNumberOfTreesToShow(tree, state)
                    )
                }
            }
        }
    }

    private fun getDetailsQuestion(state: EvaluationState) = state.complaint!!.details.questions

    /**
     * Calculates the number of immediate treatment algorithms to show based on the current state and the answered tree.
     * This function should only be called when a leaf node of an immediate treatment algorithm is reached.
     *
     * If the answered tree is not the last one currently shown, the number of trees to show remains the same.
     * Otherwise, it increments the number of trees to show, up to the total number of available algorithms.
     *
     * @param tree The immediate treatment algorithm (Tree) that was just answered.
     * @param state The current [EvaluationState] of the medical visit.
     * @return The updated number of immediate treatment algorithms to display.
     */
    private fun calculateNumberOfTreesToShow(tree: Tree, state: EvaluationState): Int {
        val treeOrdinal = state.immediateTreatmentAlgorithms.indexOf(tree) + 1
        return if (treeOrdinal != state.immediateTreatmentAlgorithmsToShow)
            state.immediateTreatmentAlgorithmsToShow
        else
            (state.immediateTreatmentAlgorithmsToShow + 1)
                .coerceAtMost(state.immediateTreatmentAlgorithms.size)
    }

    private fun updateImmediateTreatmentQuestions(
        state: EvaluationState,
        algorithmToEdit: Int,
        updatedList: TreeSummary,
        shouldShowNextAlgorithm: Boolean
    ): List<TreeSummary> {
        val newImmediateTreatmentQuestions = state.immediateTreatmentQuestions.toMutableList()
        newImmediateTreatmentQuestions[algorithmToEdit] = updatedList

        if (shouldShowNextAlgorithm) {
            val nextTree = state.immediateTreatmentAlgorithms.elementAt(algorithmToEdit + 1)
            newImmediateTreatmentQuestions.add(
                TreeSummary(
                    treeId = nextTree.id.value,
                    answers = listOf(
                        nextTree.root.toImmediateTreatmentQuestionState()
                    )
                )
            )
        }

        return newImmediateTreatmentQuestions.toList()
    }
}