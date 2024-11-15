package store.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

import store.repository.Repository;

public class TransactionTemplate {

    private Map<Repository, Collection<Object>> beganData = new HashMap<>();
    private List<Repository> targetRepositories = new ArrayList<>();

    private TransactionTemplate() {
    }

    private static class TransactionTemplateHolder {

        private static final TransactionTemplate INSTANCE = new TransactionTemplate();
    }
    public static TransactionTemplate getInstance() {
        return TransactionTemplateHolder.INSTANCE;
    }

    public void addTarget(Repository targetRepository) {
        targetRepositories.add(targetRepository);
    }

    public void execute(Runnable action) {
        try {
            begin();
            action.run();
            commit();
        } catch (Exception e) {
            rollback();
            throw e;
        }
    }

    public <T> void execute(Consumer<T> action, T arg) {
        try {
            begin();
            action.accept(arg);
            commit();
        } catch (Exception e) {
            rollback();
            throw e;
        }
    }

    public boolean executeAndGet(Supplier<Boolean> action) {
        try {
            begin();
            Boolean result = action.get();
            commit();
            return result;
        } catch (Exception e) {
            rollback();
            throw e;
        }
    }

    private void begin() {
        targetRepositories.forEach(targetRepository ->
            beganData.put(targetRepository, (Collection<Object>)targetRepository.getCopiedCollection()));
    }

    private void commit() {
        beganData = new HashMap<>();
    }

    private void rollback() {
        targetRepositories.forEach(targetRepository -> {
            Collection<Object> beganCollection = beganData.get(targetRepository);

            targetRepository.clear();
            targetRepository.addAll(beganCollection);
        });
    }
}
