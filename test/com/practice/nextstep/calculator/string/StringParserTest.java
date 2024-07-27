package com.practice.nextstep.calculator.string;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class StringParserTest {

    @Test
    void validateTest() {
        String target = "1,2;3";
        String[] arr1 = target.split(",");
        String[] arr2 = target.split(";");
        System.out.println(Arrays.toString(arr1));
        System.out.println(Arrays.toString(arr2));

        //Assertions.assertEquals(Arrays.toString(target.split(",")), "12;3");
    }

    @Test
    void regexTest() {
        String regex = "^//(.+)\\n";
        String input1 = "//;\n1;2;3";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input1);

        if (matcher.find()) {
            System.out.println(matcher.group(1));
        }
    }

    @Test
    void regexTest_2() {
        String regex =  "//(.)\n";
        String input = "//;\n1;2;3";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        if(matcher.find()) {
            System.out.println("match");
            String customDivider = matcher.group(1);
            input = input.substring(matcher.end());
        }

        System.out.println(input);
    }

    @Test
    void regexCLITest() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        //String input = br.readLine();
        String input2 = "//;\n1;2;3";
        String regex = "//(.)\n(.*)";

        Pattern pattern = Pattern.compile(regex);
        //Matcher matcher = pattern.matcher(input);
        Matcher matcher2 = pattern.matcher(input2);

        //Assertions.assertFalse(matcher.find());
        Assertions.assertTrue(matcher2.find());
    }

    @Test
    void regexCLITest_2() throws IOException {

        String input = "//;\n1;2;3";
        BufferedReader br = new BufferedReader(new StringReader(input));

        String actualInput = br.readLine(); // reads "//;" and ignores the rest of the input for this example

        String regex = "//(.)\n(.*)";
        Pattern pattern = Pattern.compile(regex);

        Matcher matcherFromInput = pattern.matcher(actualInput);
        Matcher matcherFromString = pattern.matcher("//;\n1;2;3");

        System.out.println(actualInput);
        //System.out.println("//;\n1;2;3");

        Assertions.assertTrue(actualInput.equals("//;\n1;2;3"), "It should be true");
        Assertions.assertFalse(matcherFromInput.find(), "Matcher should find a match in the input");
        Assertions.assertTrue(matcherFromString.find(), "Matcher should find a match in the input string");

        Assertions.assertEquals(";", matcherFromString.group(1), "Group 1 should be ';'");
    }

    @Test
    void regexCLITest_3() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); // 인코딩 지정
        String input = br.readLine();
        String regex = "//(.)\n(.*)"; // 줄 바꿈 문자 처리

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        Assertions.assertTrue(matcher.find());
    }

}