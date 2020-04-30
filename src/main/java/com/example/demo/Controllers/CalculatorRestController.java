package com.example.demo.Controllers;

import java.lang.annotation.Annotation;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.example.demo.Models.Calculator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.microsoft.applicationinsights.core.dependencies.http.protocol.HTTP;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ch.qos.logback.core.recovery.ResilientFileOutputStream;
import io.micrometer.core.ipc.http.HttpSender.Response;

@Controller
@JsonIgnoreProperties(ignoreUnknown = true)
public class CalculatorRestController {

	private static final Logger LOG = LoggerFactory.getLogger(CalculatorController.class);

	/** REST api */

	@RequestMapping(value = "/api/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, String>> postAdd(@RequestBody Map<String, String> operand) {

		String leftOperand = operand.get("leftOperand");
		String rightOperand = operand.get("rightOperand");

		Double result = 0.0;

		Double leftNumber = getLeftNumber(leftOperand);
		Double rightNumber = getrightNumber(rightOperand);
		String operator = "+";

		LOG.info("POST: /api/add: [{}] [{}] [{}]", leftOperand, operator, rightOperand);

		Calculator calculator = new Calculator(leftNumber, rightNumber, operator);

		result = calculator.calculateResult();

		LOG.info("POST: /api/add: result = [{}]", result);

		Map<String, String> response = new HashMap<>();

		response.put("leftOperand", leftOperand);
		response.put("rightOperand", rightOperand);
		response.put("Operator", operator);
		response.put("Result", String.valueOf(result));

		return ResponseEntity.ok(response);
	}

	@RequestMapping(value = "/api/sub", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, String>> postSub(@RequestBody Map<String, String> operand) {

		String leftOperand = operand.get("leftOperand");
		String rightOperand = operand.get("rightOperand");

		Double leftNumber = getLeftNumber(leftOperand);
		Double rightNumber = getrightNumber(rightOperand);
		String operator = "-";

		LOG.info("POST: /api/add: [{}] [{}] [{}]", leftOperand, operator, rightOperand);

		Double result = 0.0;

		Calculator calculator = new Calculator(leftNumber, rightNumber, operator);

		result = calculator.calculateResult();

		LOG.info("POST: /api/add: result = [{}]", result);

		LOG.warn("WARN: I don't like subtraction");

		Map<String, String> response = new HashMap<>();

		response.put("leftOperand", leftOperand);
		response.put("rightOperand", rightOperand);
		response.put("Operator", operator);
		response.put("Result", String.valueOf(result));

		return ResponseEntity.ok(response);

	}

	@RequestMapping(value = "/api/random", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, String>> getRandom() {

		LOG.info("GET: /api/random: Everything is random.");

		Double leftNumber = (double) new Random().nextInt(999);
		Double rightNumber = (double) new Random().nextInt(999);

		Integer randomIndex = new Random().nextInt(5);

		String[] operatorList = { "+", "-", "*", "/", "^" };

		String operator = operatorList[randomIndex];

		LOG.info("GET: /api/random: [{}] [{}] [{}]", String.valueOf(leftNumber), operator, String.valueOf(rightNumber));

		Calculator calculator = new Calculator(leftNumber, rightNumber, operator);

		Double result = calculator.calculateResult();

		LOG.info("GET: /api/random: result = [{}]", result);

		if (result.isInfinite()) {
			LOG.error("ERROR: Result is too big");
		}

		Map<String, String> response = new HashMap<>();

		response.put("leftOperand", String.valueOf(leftNumber));
		response.put("rightOperand", String.valueOf(rightNumber));
		response.put("Operator", operator);
		response.put("Result", String.valueOf(result));

		return ResponseEntity.ok(response);

	}

	@RequestMapping(value = "/api/getStatus", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> getStatus(@RequestParam(value = "http", required = false, defaultValue = "0") int status) {

		URI location = null;
		;
		try {
			location = new URI("/api/getRes");
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HttpHeaders responseHeaders = new HttpHeaders();

		LOG.info("GET: /api/getRes: to toggle HttpStatus: 200, 404, 405 ");

		if (String.valueOf(status) != null) {
			switch (status) {
				case 200:
					LOG.info("GET: /api/getRes: 200 ");
					responseHeaders.setLocation(location);
					responseHeaders.set("http", "200");
					return new ResponseEntity<String>("HttpStatus: 200 its all good.", responseHeaders, HttpStatus.OK);
				case 404:
					LOG.info("GET: /api/getRes: 404 ");
					responseHeaders.setLocation(location);
					responseHeaders.set("http", "404");
					return new ResponseEntity<String>("HttpStatus: 404 nothing here!", responseHeaders, HttpStatus.NOT_FOUND);
				case 405:
					LOG.info("GET: /api/getRes: 405 ");
					responseHeaders.setLocation(location);
					responseHeaders.set("http", "405");
					return new ResponseEntity<String>("HttpStatus: 405", responseHeaders,
							HttpStatus.METHOD_NOT_ALLOWED);
				default:
					break;
			}

		}
		return new ResponseEntity<String>("Hello World, default HttpStatus: 202", responseHeaders, HttpStatus.ACCEPTED);
	}

	Double getLeftNumber(String leftOperand) {
		Double leftNumber;
		try {
			leftNumber = Double.parseDouble(leftOperand);
			LOG.info("Left Operand [{}]", leftNumber);
		} catch (NumberFormatException ex) {
			leftNumber = 0.0;
			LOG.info("Left Operand Exception [{}]", ex);
		}
		return leftNumber;

	}

	Double getrightNumber(String rightOperand) {
		Double rightNumber;
		try {
			rightNumber = Double.parseDouble(rightOperand);
			LOG.info("right Operand [{}]", rightNumber);
		} catch (NumberFormatException ex) {
			rightNumber = 0.0;
			LOG.info("right Operand Exception [{}]", ex);
		}
		return rightNumber;
	}

}