package advisor.view;

import java.util.ArrayList;
import java.util.List;

public class View<T> {
    private List<Page> pages;
    private int currentPage;
    private int itemsPerPage;
    private int totalPages;

    public View(int itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }

    public void bind(List<T> data) {
        if (pages == null) {
            pages = new ArrayList<>();
        }
        if (pages.size() > 0) {
            pages.clear();
        }
        currentPage = 1;
        totalPages = data.size() / itemsPerPage + (data.size() % itemsPerPage != 0 ? 1 : 0);
        for (int i = 0; i < totalPages; i++) {
            pages.add(new Page(i + 1, crop(data, i * itemsPerPage)));
        }
    }

    public List<T> crop(List<T> data, int start) {
        List<T> list = new ArrayList<>();
        int end = Math.min(start + itemsPerPage, data.size());
        for (int i = start; i < end; i++) {
            list.add(data.get(i));
        }
        return list;
    }

    public void display() {
        Page page = pages.get(currentPage - 1);
        page.printData();
        System.out.printf("---PAGE %d OF %d---\n", page.getNumber(), pages.size());
    }

    public boolean nextPage() {
        if (currentPage < pages.size()) {
            currentPage++;
            return true;
        }
        System.out.println("No more pages.");
        return false;
    }

    public boolean prevPage() {
        if (currentPage > 1) {
            currentPage--;
            return true;
        }
        System.out.println("No more pages.");
        return false;
    }
}
