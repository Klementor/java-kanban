package InMemoryTaskManager.implementation;

import InMemoryTaskManager.interfaces.HistoryManager;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryHistoryManager implements HistoryManager {
    private final CustomLinkedList<Task> requestHistory;

    public InMemoryHistoryManager() {
        this.requestHistory = new CustomLinkedList<>();
    }

    @Override
    public void remove(int id) {
        requestHistory.removeNode(id);
    }

    @Override
    public ArrayList<Task> getHistory() {
        return requestHistory.getHistory();
    }

    @Override
    public void addHistory(Task task) {
        requestHistory.addLast(task);
    }

    private static class CustomLinkedList<T extends Task> {
        private HashMap<Integer, Node<T>> memory = new HashMap<>();
        private Node<T> tail;
        private Node<T> head;

        private static class Node<E> {
            private final E data;
            private Node<E> next;
            private Node<E> prev;

            public Node(Node<E> prev, E data, Node<E> next) {
                this.data = data;
                this.next = next;
                this.prev = prev;
            }
        }

        private void addLast(T element) {
            if (memory.containsKey(element.getId())) {
                removeNode(memory.get(element.getId()));
            }
            final Node<T> l = tail;
            final Node<T> newNode = new Node<>(l, element, null);
            tail = newNode;
            if (l == null) {
                head = newNode;
            } else {
                l.next = newNode;
            }
            memory.put(element.getId(), newNode);

        }

        private void removeNode(Node<T> node) {
            final Node<T> next = node.next;
            final Node<T> prev = node.prev;

            if (prev == null) {
                head = next;
            } else {
                prev.next = next;
            }

            if (next == null) {
                tail = prev;
            } else {
                next.prev = prev;
            }

        }

        private void removeNode(int id) {
            if (memory.get(id) != null) {
                removeNode(memory.get(id));
            }
        }

        private ArrayList<T> getHistory() {
            ArrayList<T> result = new ArrayList<>();
            for (Node<T> node = head; node != null; node = node.next) {
                result.add(node.data);
            }
            return result;
        }
    }
}

