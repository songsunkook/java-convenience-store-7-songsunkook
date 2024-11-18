package store.repository;

import java.util.Collection;
import java.util.List;

import store.domain.customer.Customer;

public class CustomerRepository implements Repository<Customer> {

    private Customer customer = new Customer();

    public void reset() {
        customer = new Customer();
    }

    public Customer get() {
        return customer;
    }

    @Override
    public Collection<Customer> getCopiedCollection() {
        return List.of(Customer.copyOf(customer));
    }

    @Override
    public void clear() {
        customer = null;
    }

    @Override
    public void addAll(Collection<Customer> collection) {
        if (collection != null && !collection.isEmpty()) {
            customer = collection.iterator().next();
        }
    }
}
