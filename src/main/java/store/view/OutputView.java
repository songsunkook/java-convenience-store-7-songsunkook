package store.view;

public class OutputView {

    private StringBuilder buffer;

    public OutputView(StringBuilder buffer) {
        setupBuffer();
    }

    public void exception(Exception e) {
        printWithFlush(e.getMessage());
    }

    private void setupBuffer() {
        buffer = new StringBuilder();
    }

    private void flush() {
        System.out.print(buffer);
        setupBuffer();
    }

    private void printWithFlush(String content) {
        buffer.append(content);
        flush();
    }
}
