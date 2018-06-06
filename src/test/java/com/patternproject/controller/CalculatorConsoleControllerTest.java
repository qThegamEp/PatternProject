package com.patternproject.controller;

import com.patternproject.model.CalculatorModel;
import com.patternproject.model.CalculatorNashornModel;
import com.patternproject.view.CalculatorView;
import com.patternproject.view.CalculatorConsoleView;

import com.patternproject.util.TestUtil;

import lombok.val;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import org.mockito.junit.MockitoJUnitRunner;

import java.io.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Koliadin Nikita
 */
@RunWith(MockitoJUnitRunner.class)
public class CalculatorConsoleControllerTest {

    @Mock
    private CalculatorModel calculatorModelMock;
    private CalculatorConsoleController calculatorController;
    private CalculatorConsoleController calculatorControllerEmpty;

    @Before
    public void setUp() {
        calculatorController = new CalculatorConsoleController();
        calculatorControllerEmpty = new CalculatorConsoleController();

        calculatorController.setCalculatorModel(calculatorModelMock);

        System.setIn(TestUtil.CONSOLE_INPUT_STREAM);
        System.setOut(TestUtil.CONSOLE_PRINT_STREAM);

        when((calculatorModelMock).calculate("sin(1)*sin(1)+cos(1)*cos(1)")).thenReturn(1d);
        when((calculatorModelMock).calculate("15+3")).thenReturn(18d);
        when((calculatorModelMock).calculate("sqrt(225)")).thenReturn(15d);
        when((calculatorModelMock).calculate("tan(0)")).thenReturn(0d);
    }

    @Test
    public void shouldCreateObject() {
        assertThat(calculatorController).isNotNull();
        assertThat(calculatorControllerEmpty).isNotNull();
        assertThat(calculatorModelMock).isNotNull();
        assertThat(new CalculatorConsoleController(new CalculatorNashornModel(), new CalculatorConsoleView())).isNotNull();
    }

    @Test
    public void shouldImplements() {
        assertThat(calculatorController).isInstanceOf(CalculatorController.class);
        assertThat(calculatorControllerEmpty).isInstanceOf(CalculatorController.class);
        assertThat(calculatorModelMock).isInstanceOf(CalculatorModel.class);
    }

    @Test
    public void shouldGetAndSetModel() {
        calculatorControllerEmpty.setCalculatorModel(calculatorModelMock);

        assertThat(calculatorControllerEmpty.getCalculatorModel()).isNotNull().isEqualTo(calculatorModelMock).isInstanceOf(CalculatorModel.class);
    }

    @Test
    public void shouldGetAndSetView() {
        val calculatorView = new CalculatorConsoleView();

        calculatorControllerEmpty.setCalculatorView(calculatorView);

        assertThat(calculatorControllerEmpty.getCalculatorView()).isNotNull().isEqualTo(calculatorView).isInstanceOf(CalculatorView.class);
    }

    @Test
    public void shouldSkipEmptyExpression() {
        calculatorController.calculateToConsoleInOut(new StringReader(";  ; ;;;  \b  ; \n;\n\n \t\t; \t; \t \t\r \t;  "));

        verifyZeroInteractions(calculatorModelMock);
    }

    @Test
    public void shouldOutputToConsole() {
        val byteArrayOutputStream = new ByteArrayOutputStream();

        System.setOut(new PrintStream(byteArrayOutputStream));

        calculatorController.calculateToConsoleInOut(new StringReader("sin(1)*sin(1)+cos(1)*cos(1);15+3;sqrt(225);tan(0)"));

        val actual = byteArrayOutputStream.toString();
        val expected = "1.0" + System.lineSeparator()
                + "18.0" + System.lineSeparator()
                + "15.0" + System.lineSeparator()
                + "0.0" + System.lineSeparator();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldPassSeparatedExpressions() {
        System.out.println("====[TEST] shouldPassSeparatedExpressions [TEST]====");

        calculatorController.calculateToConsoleInOut(new StringReader("sin(1)*sin(1)+cos(1)*cos(1);15+3;sqrt(225);tan(0)"));

        verify(calculatorModelMock).calculate("sin(1)*sin(1)+cos(1)*cos(1)");
        verify(calculatorModelMock).calculate("15+3");
        verify(calculatorModelMock).calculate("sqrt(225)");
        verify(calculatorModelMock).calculate("tan(0)");
        verifyNoMoreInteractions(calculatorModelMock);
    }

    @Test
    public void shouldWorkDefaultStartCalculate() {
        val input = "sin(1)*sin(1)+cos(1)*cos(1)";

        val byteArrayInputStream = new ByteArrayInputStream(input.getBytes());
        val byteArrayOutputStream = new ByteArrayOutputStream();

        System.setIn(byteArrayInputStream);
        System.setOut(new PrintStream(byteArrayOutputStream));

        calculatorController.startDefaultCalculate();

        verify(calculatorModelMock).calculate("sin(1)*sin(1)+cos(1)*cos(1)");
        verifyNoMoreInteractions(calculatorModelMock);

        val actual = byteArrayOutputStream.toString();
        val expected = "1.0" + System.lineSeparator();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldIdentifySpace() {
        System.out.println("====[TEST] shouldIdentifySpace [TEST]====");

        when((calculatorModelMock).calculate("  tan(0)")).thenReturn(0d);

        calculatorController.calculateToConsoleInOut(new StringReader("sqrt(225);  tan(0)"));

        verify(calculatorModelMock).calculate("sqrt(225)");
        verify(calculatorModelMock).calculate("  tan(0)");
        verifyNoMoreInteractions(calculatorModelMock);
    }

    @Test
    public void shouldThrowNullPointerExceptionWhenModelParameterOfArgsConstructorIsNull() {
        assertThatNullPointerException().isThrownBy(
                () -> new CalculatorConsoleController(null, new CalculatorConsoleView())
        ).withMessage("calculatorModel is marked @NonNull but is null");
    }

    @Test
    public void shouldThrowNullPointerExceptionWhenViewParameterOfArgsConstructorIsNull() {
        assertThatNullPointerException().isThrownBy(
                () -> new CalculatorConsoleController(new CalculatorNashornModel(), null)
        ).withMessage("calculatorView is marked @NonNull but is null");
    }

    @Test
    public void shouldThrowNullPointerExceptionWhenSetCalculatorModelToNull() {
        assertThatNullPointerException().isThrownBy(
                () -> calculatorControllerEmpty.setCalculatorModel(null)
        ).withMessage("calculatorModel is marked @NonNull but is null");
    }

    @Test
    public void shouldThrowNullPointerExceptionWhenSetCalculatorViewToNull() {
        assertThatNullPointerException().isThrownBy(
                () -> calculatorControllerEmpty.setCalculatorView(null)
        ).withMessage("calculatorView is marked @NonNull but is null");
    }

    @Test
    public void shouldThrowNullPointerExceptionWhenCalculatorModelIsNull() {
        assertThatNullPointerException().isThrownBy(
                () -> calculatorControllerEmpty.calculateToConsoleInOut(new StringReader("15 + 5"))
        );
    }

    @Test
    public void shouldThrowNullPointerExceptionWhenSystemOutIsNull() {
        System.setOut(null);

        assertThatNullPointerException().isThrownBy(
                () -> calculatorController.calculateToConsoleInOut(new StringReader("15 + 5"))
        );
    }

    @Test
    public void shouldCloseInputStreamWhenThrowIllegalArgumentException() throws IOException {
        val input = "end";

        val byteArrayInputStream = spy(new ByteArrayInputStream(input.getBytes()));

        System.setIn(byteArrayInputStream);

        when((calculatorModelMock).calculate("end")).thenThrow(IllegalArgumentException.class);

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
                () -> calculatorController.startDefaultCalculate()
        );

        verify(calculatorModelMock).calculate("end");
        verify(byteArrayInputStream, times(1)).close();
        verifyNoMoreInteractions(calculatorModelMock);
    }
}
