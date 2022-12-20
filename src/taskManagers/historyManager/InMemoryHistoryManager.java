package taskManagers.historyManager;

import tasks.Task;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    private static Node first;
    private static Node last;
    private static final Map<Integer, Node> history = new HashMap<>();

    @Override
    public List<Task> getHistory() { // Получение истории задач
        List<Task> convert = new ArrayList<>();
        Node node = first;

        if (node == null) {
            return convert;
        } else {
            convert.add(node.getTask());
        }

        while (node.getNext() != null) {
            node = node.getNext();
            convert.add(node.getTask());
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

        if (getFirst() == null) {
            setFirst(currNode); // Записали значение в first
        }
    }

    @Override
    public void remove(int id) { // Удаление
        removeNode(history.get(id));
        history.remove(id);
    }

    private void removeNode(Node node) { // Вырезание ноды
        Node old = node == null ? null : node.getPrevious();
        Node next = node == null ? null : node.getNext();

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