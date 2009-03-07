package Hanoi;

import java.util.ArrayList;
import java.util.Iterator;

public class Stack {

	private ArrayList<Disk> data;

	public Stack() {
		data = new ArrayList<Disk>();
	}

	public boolean push(Disk d) {
		if (data.isEmpty() || d.getSize() < peek().getSize()) {
			data.add(d);
			return true;
		}
		return false; 
	}

	public Iterator<Disk> iterator() {
		return data.iterator();
	}

	public Disk pop() {
		return data.remove(data.size() - 1);
	}

	public Disk peek() {
		return data.get(data.size() - 1);
	}

	public boolean isEmpty() {
		return data.isEmpty();
	}

	public void clear() {
		data.clear();
	}

}
