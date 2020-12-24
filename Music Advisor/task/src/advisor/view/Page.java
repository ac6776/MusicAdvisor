package advisor.view;

import java.util.List;

public class Page<T> {
    private int number;
    private List<T> data;

    public Page(int number, List<T> data) {
        this.number = number;
        this.data = data;
    }

    public int getNumber() {
        return number;
    }

    public void printData() {
        for (T elem: data) {
            System.out.println(elem.toString());
        }
    }
}
