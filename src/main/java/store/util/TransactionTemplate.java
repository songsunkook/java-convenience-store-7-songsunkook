package store.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import store.repository.Repository;
import store.view.OutputView;

public class TransactionTemplate {

    private Map<Repository, Collection<Object>> origin = new HashMap<>();
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
            OutputView.transactionException(e);
        }
    }

    private void begin() {
        targetRepositories.forEach(targetRepository -> {
            origin.put(targetRepository, (Collection<Object>)targetRepository.getOriginalCollection());
            beganData.put(targetRepository, (Collection<Object>)targetRepository.getCopiedCollection());
        });
    }

    private void commit() {
        origin = new HashMap<>();
        beganData = new HashMap<>();
    }

    private void rollback() {
        targetRepositories.forEach(targetRepository -> {
            Collection<Object> originCollection = origin.get(targetRepository);
            Collection<Object> beganCollection = beganData.get(targetRepository);

            if (originCollection != null && beganCollection != null) {
                originCollection.clear();
                originCollection.addAll(beganCollection);
            }
        });
    }
}
