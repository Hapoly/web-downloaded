
public class SadafArray<T> {
	private Node head;
	public int size = 0;

	public T get(int i) {
		Node tail = head;
		for (int j = 0; j < i; j++) {
			tail = tail.next;

		}
		return tail.value;
	}

	@Override
	public String toString() {
		Node tail = head;
		String result = "";
		if (tail == null)
			return result;
		while (tail.next != null) {
			result += (tail.value) + " | ";
			tail = tail.next;
		}
		result += (tail.value);
		return result;
	}

	public void add(T t) {
		Node tail = head;
		if (tail == null) {
			head = new Node();
			head.value = t;
		} else {
			while (tail.next != null) {
				tail = tail.next;
			}
			Node n = new Node();
			n.value = t;
			tail.next = n;
		}
		size++;
	}

	public void merg(SadafArray<T> a) {//
		for (int i = 0; i < a.size; i++)
			if (!contains(a.get(i)))
				this.add(a.get(i));
	}

	public int getSize() {
		return size;
	}

	private class Node {
		public T value;
		public Node next;
	}

	public boolean contains(T t) {
		for (int i = 0; i < size; i++)
			if (t.equals(get(i)))
				return true;
		return false;

	}

}
