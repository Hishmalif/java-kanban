package taskManager.historyManager;

import tasks.Task;

public class Node {
    private Node previous;
    private Task task;
    private Node next;

    public Node(Node previous, Task task, Node next) {
        this.previous = previous;
        this.task = task;
        this.next = next;
    }

    public Node getPrevious() {
        return previous;
    }

    public void setPrevious(Node previous) {
        this.previous = previous;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }
}