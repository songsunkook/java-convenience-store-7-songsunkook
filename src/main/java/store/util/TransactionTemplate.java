package store.util;

public class TransactionTemplate {

    private TransactionTemplate() {
    }

    private static class TransactionTemplateHolder {
        private static final TransactionTemplate INSTANCE = new TransactionTemplate();
    }

    public static TransactionTemplate getInstance() {
        return TransactionTemplateHolder.INSTANCE;
    }
}
