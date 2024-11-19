package store.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

import store.repository.Repository;

public class TransactionTemplate {

    private List<Repository> targetRepositories = new ArrayList<>();
    private final Map<String, TransactionState> transactionStates = new ConcurrentHashMap<>();

    private TransactionTemplate() {
    }

    private static class TransactionTemplateHolder {

        private static final TransactionTemplate INSTANCE = new TransactionTemplate();
    }

    public static TransactionTemplate getInstance() {
        return TransactionTemplateHolder.INSTANCE;
    }

    public void addTarget(Repository<?> targetRepository) {
        if (!targetRepositories.contains(targetRepository)) {
            targetRepositories.add(targetRepository);
        }
    }

    public void execute(Runnable action) {
        String txId = UUID.randomUUID().toString();
        try {
            begin(txId);
            action.run();
            commit(txId);
        } catch (Exception e) {
            rollback(txId);
            throw e;
        }
    }

    public <T> void execute(Consumer<T> action, T arg) {
        String txId = UUID.randomUUID().toString();
        try {
            begin(txId);
            action.accept(arg);
            commit(txId);
        } catch (Exception e) {
            rollback(txId);
            throw e;
        }
    }

    public boolean executeAndGet(BooleanSupplier action) {
        String txId = UUID.randomUUID().toString();
        try {
            begin(txId);
            boolean result = action.getAsBoolean();
            commit(txId);
            return result;
        } catch (Exception e) {
            rollback(txId);
            throw e;
        }
    }

    private void begin(String txId) {
        TransactionState state = new TransactionState();
        transactionStates.put(txId, state);

        targetRepositories.forEach(targetRepository ->
            state.beganData.put(targetRepository, targetRepository.getCopiedCollection()));
    }

    private void commit(String txId) {
        transactionStates.remove(txId);
    }

    private void rollback(String txId) {
        TransactionState state = transactionStates.get(txId);
        if (state != null) {
            targetRepositories.forEach(targetRepository -> {
                Collection<?> beganCollection = state.beganData.get(targetRepository);

                targetRepository.clear();
                targetRepository.addAll(beganCollection);
            });
            transactionStates.remove(txId);
        }
    }

    private static class TransactionState {
        private Map<Repository<?>, Collection<Object>> beganData = new HashMap<>();
    }
}
