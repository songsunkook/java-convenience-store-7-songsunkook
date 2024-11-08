package store.view;

public class OutputView {

    private StringBuilder buffer;

    public OutputView(StringBuilder buffer) {
        setupBuffer();
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
