package taskManager.historyManager;

import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private static Node first;
    private static Node last;
    private static final HashMap<Integer, Node> history = new HashMap<>();

    @Override
    public List<Task> getHistory() { // Получение истории задач
        List<Task> convert = new ArrayList<>();
        Node node = last;

        if (node == null) {
            return convert;
        } else {
            convert.add(node.getTask());
        }

        while (node.getPrevious() != null) {
            node = node.getPrevious();
            convert.add(node.getTask());
        }

        for (Task task : convert) { // Проверка вывода
            System.out.println("Задача - " + task.getId());
        }
        return convert;
    }

    @Override
    public void add(Task task) { // Добавление в историю
        int id = task.getId();

        if (history.containsKey(id)) { // Проверяем повторения
            removeNode(history.get(id));
        }

        final Node lastNode = last; // Запомнили последнее значение
        final Node currNode = new Node(lastNode, task, null); // Создали ноду
        history.put(id, currNode); // Вставили ноду в историю
        setLast(currNode); // Теперь последняя нода это текущая

        if (lastNode == null) {
            setLast(currNode);
        } else {
            lastNode.setNext(currNode); // Перезаписали у предыдущего значения ссылку на следующее
        }
    }

    @Override
    public void remove(int id) {// Удаление !!! ТРЕБУЕТСЯ РЕАЛИЗАЦИЯ В МЕНЕДЖЕРЕ
        removeNode(history.get(id));
        history.remove(id);
    }

    private void removeNode(Node node) { // Вырезание ноды
        Node old = node.getPrevious();
        Node next = node.getNext();

        if (old == null) {
            setFirst(next);
        } else {
            old.setNext(next);
            node.setPrevious(null);
        }

        if (next == null) {
            setLast(old);
        } else {
            next.setPrevious(old);
            node.setNext(null);
        }
    }

    public static Node getFirst() {
        return first;
    }

    public static void setFirst(Node first) {
        InMemoryHistoryManager.first = first;
    }

    public static Node getLast() {
        return last;
    }

    public static void setLast(Node last) {
        InMemoryHistoryManager.last = last;
    }
}