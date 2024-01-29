package org.idk.tacocloud.dao;

import org.idk.tacocloud.entity.IngredientRef;
import org.idk.tacocloud.entity.Taco;
import org.idk.tacocloud.entity.TacoOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Types;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Repository
public class JdbcOrderRepository implements OrderRepository{
    private JdbcOperations jdbcOperations;
    @Autowired
    public JdbcOrderRepository(JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }
    @Override
    @Transactional


    public TacoOrder save(TacoOrder tacoOrder) {
        PreparedStatementCreatorFactory pscf =
            new PreparedStatementCreatorFactory(
                "insert into Taco_Order "
                        + "(delivery_name, delivery_street, delivery_city, "
                        + "delivery_state, delivery_zip, cc_number, "
                        + "cc_expiration, cc_cvv, placed_at) "
                        + "values (?,?,?,?,?,?,?,?,?)",
                Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,
                Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
                Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP
            );
        pscf.setReturnGeneratedKeys(true);
        tacoOrder.setPlacedAt(new Date());

        PreparedStatementCreator psc =
                pscf.newPreparedStatementCreator(
                        Arrays.asList(
                            tacoOrder.getDeliveryName(),
                            tacoOrder.getDeliveryStreet(),
                            tacoOrder.getDeliveryCity(),
                            tacoOrder.getDeliveryState(),
                            tacoOrder.getDeliveryZip(),
                            tacoOrder.getCcNumber(),
                            tacoOrder.getCcExpiration(),
                            tacoOrder.getCcCVV(),
                            tacoOrder.getPlacedAt()
                        )
                );
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcOperations.update(psc, keyHolder);

        long orderId = keyHolder.getKey().longValue();
        tacoOrder.setId(orderId);

        List<Taco> tacos = tacoOrder.getTacos();
        int i=0;
        for (Taco taco : tacos) {
            saveTaco(orderId, i++, taco);
        }
        return null;
    }

    private Long saveTaco(long orderId, int orderKey, Taco taco) {
        PreparedStatementCreatorFactory pscf =
                new PreparedStatementCreatorFactory(
                    "INSERT Into Taco " +
                        "(name, taco_order, taco_order_key, created_at)" +
                        "Value(?,?,?,?)",
                        Types.VARCHAR,Types.BIGINT,Types.BIGINT,Types.TIMESTAMP
                );
        pscf.setReturnGeneratedKeys(true);
        taco.setCreatedAt(new Date());
        PreparedStatementCreator psc =
                pscf.newPreparedStatementCreator(
                        Arrays.asList(
                            taco.getName(),
                            orderId,
                            orderKey,
                            taco.getCreatedAt()
                        )
                );
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcOperations.update(psc, keyHolder);
        long tacoId = keyHolder.getKey().longValue();
        taco.setId(tacoId);

        saveIngredientRefs(tacoId, taco.getIngredients());
        return tacoId;
    }

    private void saveIngredientRefs(long tacoId, List<IngredientRef> ingredients) {
        int key = 0;
        for (IngredientRef ingredientRef : ingredients) {
            jdbcOperations.update(
                    "insert into Ingredient_Ref (ingredient, taco, taco_key) "
                            + "values (?, ?, ?)",
                    ingredientRef.getIngredient(), tacoId, key++
            );
        }
    }
}
