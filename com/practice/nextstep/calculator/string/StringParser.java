package com.practice.nextstep.calculator.string;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class StringParser {

    private List<String> dividers;
    private List<Integer> operands;
    public StringParser() {
        dividers = new ArrayList<>();
        dividers.add(",");
        dividers.add(":");
    }

    public List<Integer> sanitizeExpression(String input) {
        input = customDividerValidator(input);
        List<Integer> operands = input.equals(" ") ? List.of(0) : extractOperands(input);
        validateNegativeNumber(operands);
        return operands;
    }

    private List<Integer> extractOperands(String input) {

        String regex = String.join("|", dividers);
        String[] arr = input.split(regex);

        operands = Arrays.stream(arr)
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        return operands;
    }

    private String customDividerValidator(String input) {

//        String regex = "//(.)\\n";
//        Matcher matcher = Pattern.compile(regex).matcher(input.toString());
//        if (matcher.find()) {
//            dividers.add(matcher.group(1));
//            input = input.substring(matcher.end());
//        }

        if(input.contains("//")) {
            input = input.replace("//", "");
            String customDivider = input.substring(0, 1);
            dividers.add(customDivider);
            int index = input.indexOf("n");
            input = input.substring(index + 1, input.length());
        }

        return input;
    }

    private void validateNegativeNumber(List<Integer> operands) {
        for (int value : operands) {
            isNegativeNumber(value);
        }
    }

    private void isNegativeNumber(int value) {
        if (value < 0) {
            throw new RuntimeException();
        }
    }
}
