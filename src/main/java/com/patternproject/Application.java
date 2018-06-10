package com.patternproject;

import com.patternproject.model.CalculatorModelNashorn;
import com.patternproject.controller.CalculatorControllerImpl;
import com.patternproject.view.CalculatorViewConsole;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

/**
 * @author Koliadin Nikita
 */
@Slf4j
@UtilityClass
public class Application {
    public static void main(String[] args) {
        log.info(System.lineSeparator() + "--------------------------------Calculator-------------------------------");

        log.info("Preparing the application to start");

        val calculatorModel = new CalculatorModelNashorn();
        val calculatorController = new CalculatorControllerImpl();
        val calculatorView = new CalculatorViewConsole();

        calculatorController.setCalculatorModel(calculatorModel);
        calculatorController.setCalculatorView(calculatorView);
        calculatorView.setCalculatorController(calculatorController);

        log.info("Preparing the application was done successful");

        calculatorView.startConsoleCalculator();
    }
}
