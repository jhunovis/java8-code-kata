package stream.api;

import common.test.tool.annotation.Necessity;
import common.test.tool.dataset.ClassicOnlineStore;
import common.test.tool.entity.Customer;
import common.test.tool.entity.Item;
import common.test.tool.entity.Shop;

import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.collection.IsMapContaining.hasEntry;
import static org.junit.Assert.*;

public class Exercise8Test extends ClassicOnlineStore {

    @Test
    @Necessity(false)
    public void itemsNotOnSale() {
        Stream<Customer> customerStream = customerStream();
        Stream<Shop> shopStream = shopStream();

        /**
         * Create a set of item names that are in {@link Customer.wantToBuy} but not on sale in any shop.
         */
        Set<String> itemsOnSale = shopStream.flatMap(s -> s.getItemList().stream()).map(Item::getName).collect(Collectors.toSet());
        Set<String> itemNotOnSale = customerStream
                .flatMap(c -> c.getWantToBuy().stream())
                .map(Item::getName)
                .filter(item -> !itemsOnSale.contains(item))
                .collect(Collectors.toSet());

        assertThat(itemNotOnSale, hasSize(3));
        assertThat(itemNotOnSale, hasItems("bag", "pants", "coat"));
    }

    @Test
    @Necessity(false)
    public void havingEnoughMoney() {
        Stream<Customer> customerStream = customerStream();
        Stream<Shop> shopStream = shopStream();

        /**
         * Create a customer's name list including who are having enough money to buy all items they want which is on sale.
         * Items that are not on sale can be counted as 0 money cost.
         * If there is several same items with different prices, customer can choose the cheapest one.
         */
        List<Item> onSale = shopStream.flatMap(s -> s.getItemList().stream()).collect(Collectors.toList());
        Predicate<Customer> havingEnoughMoney = c -> c.getWantToBuy().stream()
                .mapToInt(
                        wishedItem -> onSale.stream()
                                .filter(shopItem -> shopItem.getName().equals(wishedItem.getName()))
                                .mapToInt(Item::getPrice)
                                .min().orElse(0)
                ).sum() <= c.getBudget();
        List<String> customerNameList = customerStream.filter(havingEnoughMoney).map(Customer::getName).collect(Collectors.toList());

        assertThat(customerNameList, hasSize(7));
        assertThat(customerNameList, hasItems("Joe", "Patrick", "Chris", "Kathy", "Alice", "Andrew", "Amy"));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testCustomerBudgets() throws Exception {
        Map<String, Integer> customerBudgets = ClassicOnlineStore.customerBudgetsIndexedByCustomerName(customers());

        assertThat(
                customerBudgets,
                allOf(
                        hasEntry("Alice", 2500),
                        hasEntry("Amy", 2000),
                        hasEntry("Andrew", 11000),
                        hasEntry("Chris", 9000),
                        hasEntry("Diana", 12000),
                        hasEntry("Joe", 8000),
                        hasEntry("Kathy", 6000),
                        hasEntry("Martin", 1000),
                        hasEntry("Patrick", 4000),
                        hasEntry("Steven", 2000)
                )

        );
    }


    @SuppressWarnings("unchecked")
    @Test
    public void testLowestItemPrices() throws Exception {
        Map<String, Integer> lowestItemPrices = lowestItemPrices();

        assertThat(
                lowestItemPrices,
                allOf(
                        hasEntry("bond", 480),
                        hasEntry("cable", 230),
                        hasEntry("chair", 600),
                        hasEntry("chopsticks", 180),
                        hasEntry("cold medicine", 800),
                        hasEntry("crisps", 80),
                        hasEntry("cup", 380),
                        hasEntry("desk", 1800),
                        hasEntry("earphone", 7800),
                        hasEntry("eye-drops", 600),
                        hasEntry("fork", 210),
                        hasEntry("ginseng", 120),
                        hasEntry("headphone", 8800),
                        hasEntry("ice cream", 200),
                        hasEntry("ointment", 500),
                        hasEntry("onion", 160),
                        hasEntry("plane", 2200),
                        hasEntry("plate", 680),
                        hasEntry("poultice", 900),
                        hasEntry("rope", 800),
                        hasEntry("saw", 1400),
                        hasEntry("screwdriver", 600),
                        hasEntry("small chair", 1800),
                        hasEntry("small table", 2800),
                        hasEntry("speaker", 19000),
                        hasEntry("spinach", 100),
                        hasEntry("spoon", 210),
                        hasEntry("table", 5500)
                )
        );

        assertThat(
                lowestItemPrices.size(), is(28)
        );

    }


}
