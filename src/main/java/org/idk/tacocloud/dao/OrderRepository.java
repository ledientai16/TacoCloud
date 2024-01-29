package org.idk.tacocloud.dao;

import org.idk.tacocloud.entity.TacoOrder;

public interface OrderRepository {
    TacoOrder save(TacoOrder tacoOrder);
}
