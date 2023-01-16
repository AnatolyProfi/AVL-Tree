
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@NoArgsConstructor
public class MyTree<E> implements Collection<E> {
    private Entry<E> root = null;
    private int size;


    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size > 0;
    }

    @Override
    public boolean contains(Object o) {
        for (E e : getFullTree()) {
            if (e == o) {
                return true;
            }
        }
        return false;
    }

    public E get(E e) {
        if (contains(e)) {
            return e;
        } else {
            return null;
        }
    }

    public Collection<E> getFullTree() {
        List<List<E>> listList = new LinkedList<>();
        listList.add(List.of(root.getValue()));
        listList.add(getTreeRightValues().stream().toList());
        listList.add(getTreeLeftValues().stream().toList());
        List<E> list = listList.stream().flatMap(Collection::stream).toList();
        return list;
    }

    public Collection<E> getTreeLeftValues() {
        List<E> list = new LinkedList<>();
        Entry<E> current = root;
        while (current != null) {
            if (current.getRight() != null) {
                current = current.getRight();
                list.add(current.getValue());
                if (current.getLeft() != null) {
                    list.add(current.getLeft().getValue());
                }
            } else {
                return list;
            }
        }
        return null;
    }

    public Collection<E> getTreeRightValues() {
        List<E> list = new LinkedList<>();
        Entry<E> current = root;
        while (current != null) {
            if (current.getLeft() != null) {
                current = current.getLeft();
                list.add(current.getValue());
                if (current.getRight() != null) {
                    list.add(current.getRight().getValue());
                }
            } else {
                return list;
            }
        }
        return null;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return getFullTree().toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return getFullTree().toArray(a);
    }

    private Entry<E> addBalance(E value) {
        Entry<E> node = new Entry<E>(value);
        if (root == null) {
            root = node;
        } else add(root, root, node);
        return node;

    }

    private Entry<E> add(Entry<E> entry, Entry<E> parent, Entry<E> addedElement) {

        if (entry == null) {
            addedElement.parent = parent;
            return addedElement;
        }

        if (hash(entry) < hash(addedElement)) {
            entry.right = add(entry.right, entry, addedElement);
        } else {
            entry.left = add(entry.left, entry, addedElement);
        }

        entry.height = Math.max(height(entry.right), height(entry.left)) + 1;

        if (height(entry) > 2) entry = balancing(entry);

        return entry;
    }

    @Override
    public boolean add(E e) {
        if (root == null) {
            root = new Entry<>(e);
            size++;
            return true;
        }
        Entry<E> current = root;
        Entry<E> newEntry = new Entry<E>(e);
        while (current != null) {
            if (current.getRight() != null && hash(current.getValue()) > hash(newEntry.getValue())) {
                current = current.getRight();
            } else if (current.getRight() == null && hash(current.getValue()) > hash(newEntry.getValue())) {
                newEntry.setParent(current);
                current.setRight(newEntry);
                size++;
                return true;
            } else if (current.getLeft() != null && hash(current.getValue()) < hash(newEntry.getValue())) {
                current = current.getLeft();
            } else if (current.getLeft() == null && hash(current.getValue()) < hash(newEntry.getValue())) {
                newEntry.setParent(current);
                current.setLeft(newEntry);
                size++;
                return true;
            }
        }
        return false;
    }

    private Entry<E> balancing(Entry<E> entry) {
        if (height(entry.right) - height(entry.left) >= 2) {
            if (height(entry.right.left) <= height(entry.right.right)) {
                return shortLeftTurn(entry);
            } else {
                return longLeftTurn(entry);
            }
        } else if (height(entry.left) - height(entry.right) >= 2) {
            if (height(entry.left.right) <= height(entry.left.left)) {
                return shortRightTurn(entry);
            } else {
                return longRightTurn(entry);
            }
        }
        return entry;
    }

    private Entry<E> longLeftTurn(Entry<E> entry) {
        Entry<E> leftPart = entry;
        Entry<E> rightPart = entry.right;
        entry = rightPart.left;

        if (leftPart.parent != null) {
            entry.parent = leftPart.parent;
        } else {
            entry.parent = null;
        }
        if (entry.right != null) {
            rightPart.left = entry.right;
            rightPart.left.parent = rightPart;
        } else {
            rightPart.left = null;
        }

        if (entry.left != null) {
            leftPart.right = entry.left;
            leftPart.right.parent = leftPart;
        } else {
            leftPart.right = null;
        }
        entry.setRight(rightPart);
        entry.getRight().setHeight(entry.getHeight() - 1);
        entry.getRight().setParent(entry);
        entry.setLeft(leftPart);
        entry.getRight().setHeight(entry.getHeight() - 2);
        entry.getLeft().setParent(entry);
        if (entry.parent != null) {
            if (hash(entry.parent) < hash(entry)) {
                entry.parent.right = entry;
            } else {
                entry.parent.left = entry;
            }
        } else {
            root = entry;
        }
        return entry;
    }

    private Entry<E> shortLeftTurn(Entry<E> entry) {
        Entry<E> leftPart = entry;
        if (entry.getParent() != null) {
            entry.right.parent = entry.parent;
        } else {
            entry.right.parent = null;
        }
        entry = entry.right;
        leftPart.right = entry.left;
        if (leftPart.right != null) {
            leftPart.right.parent = leftPart;
        }
        entry.left = leftPart;
        leftPart.parent = entry;
        if (entry.parent != null) {
            if (hash(entry.parent) < hash(entry)) {
                entry.parent.right = entry;
            } else {
                entry.parent.left = entry;
            }
        } else {
            root = entry;
        }
        return entry;
    }

    private int height(Entry<E> entry) {
        if (entry == null) {
            return 0;
        }
        return entry.height;
    }

    private Entry<E> shortRightTurn(Entry<E> entry) {
        Entry<E> rightPart = entry;
        if (entry.parent != null) {
            entry.left.parent = entry.parent;
        } else {
            entry.left.parent = null;
        }

        entry = entry.left;
        rightPart.left = entry.right;
        if (rightPart.left != null) {
            rightPart.left.parent = rightPart;
        }
        entry.right = rightPart;
        rightPart.parent = entry;
        if (entry.parent != null) {
            if (hash(entry.parent) < hash(entry)) {
                entry.parent.right = entry;
            } else {
                entry.parent.left = entry;
            }
        } else {
            root = entry;
        }
        return entry;
    }

    private Entry<E> longRightTurn(Entry<E> entry) {
        Entry<E> rightPart = entry;
        Entry<E> leftPart = entry.left;
        entry = leftPart.right;

        if (rightPart.parent != null) {
            entry.parent = rightPart.parent;
        } else {
            entry.parent = null;
        }

        if (entry.left != null) {
            leftPart.right = entry.left;
            leftPart.right.parent = leftPart;
        } else leftPart.right = null;

        if (entry.right != null) {
            rightPart.left = entry.right;
            rightPart.left.parent = rightPart;
        } else {
            rightPart.left = null;
        }

        entry.left = leftPart;
        entry.left.height = entry.height - 1;
        entry.left.parent = entry;
        entry.right = rightPart;
        entry.right.height = entry.height - 2;
        entry.right.parent = entry;
        if (entry.parent != null) {
            if (hash(entry.parent) < hash(entry)) {
                entry.parent.right = entry;
            } else {
                entry.parent.left = entry;
            }
        } else {
            root = entry;
        }
        return entry;
    }

    private int hash(Object o) {
        return (o.hashCode()) & (2147483646);
    }

    @Override
    public boolean remove(Object o) {
        Entry<E> current = root;
        while (current != null) {
            if (current.getRight() != null && hash(current.getValue()) > hash(o)) {
                current = current.getRight();
            } else if (current.getRight() == null && hash(current.getValue()) > hash(o)) {
                current.getParent().setRight(current.getRight());
                return true;
            } else if (current.getLeft() != null && hash(current.getValue()) < hash(o)) {
                current = current.getLeft();
            } else if (current.getLeft() == null && hash(current.getValue()) < hash(o)) {
                current.getParent().setRight(current.getLeft());
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object e : c) {
            return contains(e);
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        for (E e : c) {
            addBalance(e);
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        for (Object e : c) {
            remove(e);
        }
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        clear();
        for (Object e : c) {
            add((E) e);
        }
        return true;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    public

    @Getter
    @Setter
    @AllArgsConstructor
    static class Entry<E> {

        private int height = 1;
        private E value;
        private Entry<E> left, right, parent;

        public Entry(Entry<E> entry) {
            this.right = entry.right;
            this.left = entry.left;
            this.parent = entry.parent;
        }

        public Entry(Entry<E> entry, Entry<E> parent) {
            this.right = entry.right;
            this.left = entry.left;
            this.parent = parent;
        }

        public Entry(E value) {
            this.value = value;
        }

        final Entry<E> getFirstEntry() {
            Entry<E> p = parent;
            if (p != null) {
                while (p.left != null) {
                    p = p.left;
                }
            }
            return p;
        }

        final Entry<E> getLastEntry() {
            Entry<E> p = parent;
            if (p != null) {
                while (p.right != null) {
                    p = p.right;
                }
            }
            return p;
        }

        public static <E> Entry<E> successor(Entry<E> root) {
            if (root == null) {
                return null;
            } else if (root.right != null) {
                Entry<E> p = root.right;
                while (p.left != null) {
                    p = p.left;
                }
                return p;
            } else {
                Entry<E> p = root.parent;
                Entry<E> ch = root;
                while (p != null && ch == p.right) {
                    ch = p;
                    p = p.parent;
                }
                return p;
            }
        }
    }

}