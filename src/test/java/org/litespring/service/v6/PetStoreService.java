package org.litespring.service.v6;

import org.litespring.beans.factory.annotation.Autowired;
import org.litespring.dao.v6.AccountDao;
import org.litespring.dao.v6.ItemDao;
import org.litespring.stereotype.Component;
import org.litespring.util.MessageTracker;

@Component(value = "petStore")
public class PetStoreService implements IPetstoreService {

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private ItemDao itemDao;

    public AccountDao getAccountDao() {
        return accountDao;
    }

    public ItemDao getItemDao() {
        return itemDao;
    }

    public void placeOrder() {
        System.out.println("place order");
        MessageTracker.add("place order");
    }

}
