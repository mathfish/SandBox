package spring.in.action.SpringBoot.data;

import spring.in.action.SpringBoot.Taco;

public interface TacoRepository {
    Taco save(Taco design);
}
