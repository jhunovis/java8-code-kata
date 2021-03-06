package stream.api;

import common.test.tool.annotation.Necessity;
import common.test.tool.dataset.ClassicOnlineStore;
import common.test.tool.entity.Customer;

import org.junit.Test;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class Exercise3Test extends ClassicOnlineStore {

    @Test
    @Necessity(true)
    public void howManyItemsWanted() {
        List<Customer> customerList = this.mall.getCustomerList();

        /**
         * Count how many items there are in {@link Customer.wantToBuy} using {@link Stream#count}
         */
        long sum = customerList.stream().flatMap(c -> c.getWantToBuy().stream()).count();

        assertThat(sum, is(32L));
    }

    @Test
    @Necessity(true)
    public void richestCustomer() {
        List<Customer> customerList = this.mall.getCustomerList();

        /**
         * Find the richest customer's budget by using {@link Stream#max} and {@link Comparator#naturalOrder}
         * Don't use {@link Stream#sorted}
         */
        Comparator<Integer> comparator = Comparator.naturalOrder();
        Optional<Integer> richestCustomer = customerList.stream().map(Customer::getBudget).max(comparator);

        assertThat(comparator.getClass().getSimpleName(), is("NaturalOrderComparator"));
        assertThat(richestCustomer.get(), is(12000));
    }

    @Test
    @Necessity(true)
    public void youngestCustomer() {
        List<Customer> customerList = this.mall.getCustomerList();

        /**
         * Find the youngest customer by using {@link Stream#min}
         * Don't use {@link Stream#sorted}
         */
        Comparator<Customer> comparator = (first, second) -> {
            Integer firstAge = first.getAge();
            Integer secondAge = second.getAge();
            return firstAge.equals(secondAge) ? 0 : firstAge < secondAge ? -1 : 1;
        };
        Optional<Customer> youngestCustomer = customerList.stream().min(comparator);

        assertThat(youngestCustomer.get(), is(customerList.get(8)));
    }
}
