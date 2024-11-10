package store.repository;

import store.domain.customer.Customer;

public class CustomerRepository {

    private Customer customer = new Customer();

    public void reset() {
        customer = new Customer();
    }

    public Customer get() {
        return customer;
    }
}
