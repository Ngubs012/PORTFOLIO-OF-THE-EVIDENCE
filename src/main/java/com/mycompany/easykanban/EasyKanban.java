/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.easykanban;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
/**
 *
 * @author RC_Student_lab
 */


public class EasyKanban {
    private static String username;
    private static List<Task> tasks = new ArrayList<>();

    public static void main(String[] args) {
        login();
        if (username != null && !username.isEmpty()) {
            displayWelcomeMessage();
            int option;
            do {
                option = displayMenuAndGetOption();
                switch (option) {
                    case 1:
                        addTasks();
                        break;
                    case 2:
                        showReport();
                        break;
                    case 3:
                        searchTask();
                        break;
                    case 4:
                        sortTasksByDuration();
                        break;
                    case 5:
                        JOptionPane.showMessageDialog(null, "Goodbye!");
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Invalid . Please try again.");
                }
            } while (option != 5);
        } else {
            JOptionPane.showMessageDialog(null, "Login failed. Exiting.");
        }
    }

    private static void login() {
        username = JOptionPane.showInputDialog("Please enter your username:");
    }

    private static void displayWelcomeMessage() {
        JOptionPane.showMessageDialog(null, "Welcome to EasyKanban, " + username + "!");
    }

    private static int displayMenuAndGetOption() {
        String optionStr = JOptionPane.showInputDialog("Choose an option:\n" +
                "1. Add tasks\n" +
                "2. Show report\n" +
                "3. Search task\n" +
                "4. Sort tasks by duration\n" +
                "5. Quit");
        return Integer.parseInt(optionStr);
    }

    private static void addTasks() {
        int numTasks = Integer.parseInt(JOptionPane.showInputDialog("Enter the number of tasks:"));
        for (int i = 0; i < numTasks; i++) {
            Task task = new Task();
            task.setTaskNumber(tasks.size() + 1);
            task.setTaskName(JOptionPane.showInputDialog("Enter task name:"));
            task.setTaskDescription(JOptionPane.showInputDialog("Enter task description:"));
            task.setDeveloperDetails(JOptionPane.showInputDialog("Enter developer details:"));
            task.setTaskDuration(Integer.parseInt(JOptionPane.showInputDialog("Enter task duration (in hours):")));
            task.generateTaskID();
            tasks.add(task);
            JOptionPane.showMessageDialog(null, task.printTaskDetails());
        }
        JOptionPane.showMessageDialog(null, "Total hours: " + Task.returnTotalHours(tasks));
    }

    private static void showReport() {
        if (tasks.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No tasks available to show.");
            return;
        }

        StringBuilder report = new StringBuilder("Task Report:\n");
        for (Task task : tasks) {
            report.append(task.printTaskDetails()).append("\n\n");
        }
        JOptionPane.showMessageDialog(null, report.toString());
    }

    private static void searchTask() {
        String searchKeyword = JOptionPane.showInputDialog("Enter task name or developer to search:");
        Optional<Task> foundTask = tasks.stream()
                .filter(task -> task.getTaskName().equalsIgnoreCase(searchKeyword) || 
                        task.getDeveloperDetails().equalsIgnoreCase(searchKeyword))
                .findFirst();

        if (foundTask.isPresent()) {
            JOptionPane.showMessageDialog(null, "Task Found:\n" + foundTask.get().printTaskDetails());
        } else {
            JOptionPane.showMessageDialog(null, "No task found with the given name or developer.");
        }
    }

    private static void sortTasksByDuration() {
        tasks.sort(Comparator.comparingInt(Task::getTaskDuration));
        JOptionPane.showMessageDialog(null, "Tasks sorted by duration.");
    }
}

class Task {
    private String taskName;
    private int taskNumber;
    private String taskDescription;
    private String developerDetails;
    private int taskDuration;
    private String taskID;

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskNumber(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public void setDeveloperDetails(String developerDetails) {
        this.developerDetails = developerDetails;
    }

    public String getDeveloperDetails() {
        return developerDetails;
    }

    public void setTaskDuration(int taskDuration) {
        this.taskDuration = taskDuration;
    }

    public int getTaskDuration() {
        return taskDuration;
    }

    public void generateTaskID() {
        String taskNameAbbreviation = taskName.substring(0, 2).toUpperCase();
        String developerNameAbbreviation = developerDetails.substring(developerDetails.lastIndexOf(" ") + 1).toUpperCase();
        this.taskID = taskNameAbbreviation + ":" + taskNumber + ":" + developerNameAbbreviation;
    }

    public String printTaskDetails() {
        return "Task Name: " + taskName + "\n" +
                "Task Number: " + taskNumber + "\n" +
                "Task Description: " + taskDescription + "\n" +
                "Developer Details: " + developerDetails + "\n" +
                "Task Duration: " + taskDuration + " hours" + "\n" +
                "Task ID: " + taskID;
    }

    public static int returnTotalHours(List<Task> tasks) {
        return tasks.stream().mapToInt(Task::getTaskDuration).sum();
    }
}