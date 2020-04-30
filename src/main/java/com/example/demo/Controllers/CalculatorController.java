package com.example.demo.Controllers;

import com.example.demo.Models.Calculator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
public class CalculatorController {

	private static final Logger LOG = LoggerFactory.getLogger(CalculatorController.class);
	
	@GetMapping("/")
	public String index(Model model) {
		model.addAttribute("operator", "+");
		model.addAttribute("view", "views/calculatorForm");
		return "base-layout";
	}

	@GetMapping("/restres")
	public String indexdb(Model model){
		model.addAttribute("operator", "+");
		model.addAttribute("view", "views/calculatorForm");
		return null;
		
	}
	
	@PostMapping("/")
	public String index(
			@RequestParam String leftOperand,
			@RequestParam String operator,
			@RequestParam String rightOperand,
			Model model
	) {
		double leftNumber;
		double rightNumber;

		try {
			leftNumber = Double.parseDouble(leftOperand);
			LOG.info("Left Operand [{}]", leftNumber);
		}
		catch (NumberFormatException ex) {
			leftNumber = 0;
			LOG.info("Left Operand Exception [{}]", ex);
		}

		try {
			rightNumber = Double.parseDouble(rightOperand);
			LOG.info("Right Operand [{}]", rightNumber);
		}
		catch (NumberFormatException ex) {
			rightNumber = 0;
			LOG.info("Right Operand Exception [{}]", ex);
		}
		
		Calculator calculator = new Calculator(
				leftNumber,
				rightNumber,
				operator
		);
		LOG.info("new Calculator ( [{}] [{}] [{}])", leftNumber, operator, rightNumber);
		
		double result = calculator.calculateResult();
		LOG.info("result: [{}]", result);
		model.addAttribute("leftOperand", leftNumber);
		model.addAttribute("operator", operator);
		model.addAttribute("rightOperand", rightNumber);
		model.addAttribute("result", result);

		model.addAttribute("view", "views/calculatorForm");
		return "base-layout";
	}
}
