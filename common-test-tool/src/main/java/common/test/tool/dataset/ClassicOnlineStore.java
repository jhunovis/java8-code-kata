package common.test.tool.dataset;

import common.test.tool.entity.Customer;
import common.test.tool.entity.Item;
import common.test.tool.entity.OnlineShoppingMall;
import common.test.tool.entity.Shop;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.bind.JAXB;

public class ClassicOnlineStore {

    protected final OnlineShoppingMall mall =
        JAXB.unmarshal(new File("../common-test-tool/src/test/resources/data.xml"), OnlineShoppingMall.class);

    protected static Map<String, Integer> customerBudgetsIndexedByCustomerName(List<Customer> customerList) {
        return customerList.stream()
                .collect(Collectors.toMap(Customer::getName, Customer::getBudget));
    }

    protected List<Customer> customers() {
        return this.mall.getCustomerList();
    }

    protected Stream<Customer> customerStream() {
        return customers().stream();
    }

    protected Map<String, Integer> lowestItemPrices() {
        return shopStream()
                .flatMap(s -> s.getItemList().stream())
                .collect(Collectors.toMap(Item::getName, Item::getPrice, Math::min));
    }

    protected Stream<Shop> shopStream() {
        return this.mall.getShopList().stream();
    }

}
