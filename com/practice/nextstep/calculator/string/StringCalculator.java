package com.practice.nextstep.calculator.string;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class StringCalculator {

    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    public void run() throws IOException {

        String input;
        while ((input = br.readLine()) != null && !input.equals("0")) {
            executor(input);
        }
    }

    private void executor(String input) {

        StringParser parser = new StringParser();
        List<Integer> operands = parser.sanitizeExpression(input);
        int result = sum(operands);
        System.out.println(result);
    }

    private int sum(List<Integer> operands) {

        int result = 0;
        for (int value : operands) {
            result += value;
        }
        return result;
    }

}
