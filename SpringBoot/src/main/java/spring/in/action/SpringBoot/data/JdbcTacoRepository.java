package spring.in.action.SpringBoot.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import spring.in.action.SpringBoot.Ingredient;
import spring.in.action.SpringBoot.Taco;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.Arrays;
import java.util.Date;

public class JdbcTacoRepository implements TacoRepository {

    private final JdbcTemplate jdbc;

    @Autowired
    public JdbcTacoRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Taco save(Taco taco) {
        long tacoId = saveTacoInfo(taco);
        taco.setId(tacoId);
        for (String ingredientCode : taco.getIngredients()) {
            saveIngredientToTaco(ingredientCode, tacoId);
        }

        return taco;
    }

    private void saveIngredientToTaco(String ingredientCode, long tacoId) {
        jdbc.update("INSERT INTO Taco_Ingredients (taco, ingredient) VALUES (?, ?)",
                        tacoId,
                        ingredientCode);
    }

    private long saveTacoInfo(Taco taco) {
        taco.setCreatedAt(new Date());
        PreparedStatementCreator preparedStatementCreator =
                new PreparedStatementCreatorFactory(
                        "INSERT INTO Taco (name, createdAt) values (?, ?)",
                        Types.VARCHAR,
                        Types.TIMESTAMP).newPreparedStatementCreator(
                                Arrays.asList(taco.getName(),
                                new Timestamp(taco.getCreatedAt().getTime())));

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(preparedStatementCreator, keyHolder);
        return keyHolder.getKey().longValue();
    }
}
