package network.omisego.omgmerchant.calculator

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 9/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.widget.ImageButton
import android.widget.TextView
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyZeroInteractions
import com.nhaarman.mockito_kotlin.whenever
import org.amshove.kluent.mock
import org.junit.Before
import org.junit.Test

class CalculatorInteractionTest {
    private val mockTextView: TextView = mock()
    private val mockImageView: ImageButton = mock()
    private val mockOperation: CalculatorInteraction.Operation = mock()
    private val mockState: CalculatorState = mock()
    private val mockCondition: CalculatorCondition = mock()
    private val calculatorHandler: CalculatorInteraction by lazy {
        CalculatorInteraction(mockCondition).apply {
            this.operation = mockOperation
        }
    }

    @Before
    fun setup() {
        whenever(mockCondition.state).thenReturn(mockState)
    }

    @Test
    fun `should do nothing if the operation is undefined`() {
        whenever(mockTextView.text).thenReturn("")
        calculatorHandler.handleNumPadPressed(mockTextView)

        verifyZeroInteractions(mockCondition)
        verifyZeroInteractions(mockState)
        verifyZeroInteractions(mockOperation)
    }

    @Test
    fun `should clear the calculator before appending if clearBeforeAppend flag is true`() {
        whenever(mockTextView.text).thenReturn("1")
        whenever(mockState.clearBeforeAppend).thenReturn(true)

        calculatorHandler.handleNumPadPressed(mockTextView)

        verify(mockOperation).onClear()
        verify(mockState).recentButton = CalculatorButton.NUM_1
        verify(mockOperation).onAppend('1')
    }

    @Test
    fun `should append the text without clear if the clearBeforeAppend flag is false`() {
        whenever(mockTextView.text).thenReturn("2")
        whenever(mockState.clearBeforeAppend).thenReturn(false)

        calculatorHandler.handleNumPadPressed(mockTextView)

        verify(mockOperation, never()).onClear()
        verify(mockState).recentButton = CalculatorButton.NUM_2
        verify(mockOperation).onAppend('2')
    }

    @Test
    fun `should clear all text and reset all states to default when press AC button`() {
        whenever(mockTextView.text).thenReturn("AC")
        calculatorHandler.handleNumPadPressed(mockTextView)

        verify(mockOperation).onClear()
        verify(mockState).recentButton = CalculatorButton.NUM_0
        verify(mockState).clearBeforeAppend = false
        verify(mockState).containOperator = false
    }

    @Test
    fun `should simply append the operator if the containOperator flag is false`() {
        whenever(mockTextView.text).thenReturn("+")
        whenever(mockState.containOperator).thenReturn(false)

        calculatorHandler.handleNumPadPressed(mockTextView)

        verify(mockOperation, never()).onEvaluate()
        verify(mockOperation).onAppend('+')
        verify(mockState).containOperator = true
        verify(mockState).clearBeforeAppend = false
    }

    @Test
    fun `should evaluate and append the operator if the containOperator flag is true`() {
        whenever(mockTextView.text).thenReturn("+")
        whenever(mockState.containOperator).thenReturn(true)

        calculatorHandler.handleNumPadPressed(mockTextView)

        verify(mockOperation).onEvaluate()
        verify(mockOperation).onAppend('+')
        verify(mockState).containOperator = true
        verify(mockState).clearBeforeAppend = false
    }

    @Test
    fun `should set flag clearBeforeAppend true if the percent operator has been evaluated and returned true`() {
        whenever(mockTextView.text).thenReturn("%")
        whenever(mockState.recentButton).thenReturn(CalculatorButton.NUM_3)
        whenever(mockOperation.onEvaluate()).thenReturn(true)

        calculatorHandler.handleNumPadPressed(mockTextView)

        verify(mockOperation).onAppend('%')
        verify(mockOperation).onEvaluate()
        verify(mockState).clearBeforeAppend = true
    }

    @Test
    fun `should not set flag clearBeforeAppend if the percent operator has been evaluated and returned false`() {
        whenever(mockTextView.text).thenReturn("%")
        whenever(mockState.recentButton).thenReturn(CalculatorButton.NUM_3)
        whenever(mockOperation.onEvaluate()).thenReturn(false)

        calculatorHandler.handleNumPadPressed(mockTextView)

        verify(mockOperation).onAppend('%')
        verify(mockOperation).onEvaluate()
        verify(mockState, never()).clearBeforeAppend
    }

    @Test
    fun `should set flag clearBeforeAppend true if the equal operator has been evaluated and returned true`() {
        whenever(mockTextView.text).thenReturn("=")
        whenever(mockState.recentButton).thenReturn(CalculatorButton.NUM_3)
        whenever(mockOperation.onEvaluate()).thenReturn(true)

        calculatorHandler.handleNumPadPressed(mockTextView)

        verify(mockOperation).onEvaluate()
        verify(mockState).clearBeforeAppend = true
    }

    @Test
    fun `should not set flag clearBeforeAppend if the equal operator has been evaluated and returned false`() {
        whenever(mockTextView.text).thenReturn("=")
        whenever(mockState.recentButton).thenReturn(CalculatorButton.NUM_3)
        whenever(mockOperation.onEvaluate()).thenReturn(true)

        calculatorHandler.handleNumPadPressed(mockTextView)

        verify(mockOperation).onEvaluate()
        verify(mockState, never()).clearBeforeAppend
    }

    @Test
    fun `should invoke onDelete if the view is ImageButton`() {
        calculatorHandler.handleNumPadPressed(mockImageView)

        verify(mockOperation).onDelete()
    }
}
