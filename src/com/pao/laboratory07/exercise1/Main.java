package com.pao.laboratory07.exercise1;

import com.pao.laboratory07.exercise1.exceptions.CannotRevertInitialOrderStateException;
import com.pao.laboratory07.exercise1.exceptions.OrderIsAlreadyFinalException;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        OrderState current = OrderState.valueOf(scanner.nextLine().trim());
        System.out.println("Initial order state: " + current);

        Deque<OrderState> history = new ArrayDeque<>();

        while (scanner.hasNextLine()) {
            String command = scanner.nextLine().trim();

            if (command.equals("QUIT")) {
                System.out.println("User quit the program.");
                break;
            }

            switch (command) {
                case "next" -> {
                    try {
                        if (current.isFinal()) throw new OrderIsAlreadyFinalException("Order is already in a final state.");
                        history.push(current);
                        current = current.next();
                        System.out.println("Order state updated to: " + current);
                    } catch (OrderIsAlreadyFinalException e) {
                        System.out.println(e.getMessage());
                    }
                }
                case "cancel" -> {
                    try {
                        if (current.isFinal()) throw new OrderIsAlreadyFinalException("Cannot cancel a final state order.");
                        history.push(current);
                        current = OrderState.CANCELED;
                        System.out.println("Order has been canceled.");
                    } catch (OrderIsAlreadyFinalException e) {
                        System.out.println(e.getMessage());
                    }
                }
                case "undo" -> {
                    try {
                        if (history.isEmpty()) throw new CannotRevertInitialOrderStateException("No previous state to revert to.");
                        current = history.pop();
                        System.out.println("Order state reverted to: " + current);
                    } catch (CannotRevertInitialOrderStateException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }
    }
}
