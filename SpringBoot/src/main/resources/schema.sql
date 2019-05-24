CREATE TABLE IF NOT EXISTS Ingredient (
    id VARCHAR(4) UNIQUE NOT NULL,
    name VARCHAR(25) NOT NULL,
    type VARCHAR(10) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS Taco (
    id IDENTITY,
    name VARCHAR(50) NOT NULL,
    createdAt TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS Taco_Ingredients (
    taco BIGINT NOT NULL,
    ingredient VARCHAR(4) NOT NULL,
    FOREIGN KEY (taco) REFERENCES Taco(id),
    FOREIGN KEY (ingredient) REFERENCES Ingredient(id)
);

CREATE TABLE IF NOT EXISTS Taco_Order (
    id IDENTITY,
    username VARCHAR(50) NOT NULL,
    deliveryName VARCHAR(50) NOT NULL,
    deliveryStreet VARCHAR(50) NOT NULL,
    deliveryCity VARCHAR(50) NOT NULL,
    deliveryState VARCHAR(2) NOT NULL,
    deliveryZip VARCHAR(10) NOT NULL,
    ccNumber VARCHAR(16) NOT NULL,
    ccExpiration VARCHAR(5) NOT NULL,
    ccCVV VARCHAR(3) NOT NULL,
    placedAt TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS Taco_Order_Tacos (
    tacoOrder BIGINT NOT NULL,
    taco BIGINT NOT NULL,
    FOREIGN KEY (tacoOrder) REFERENCES Taco_Order(id),
    FOREIGN KEY (taco) REFERENCES Taco(id)
);

CREATE TABLE IF NOT EXISTS User (
    username VARCHAR(50) NOT NULL,
    password VARCHAR(100) NOT NULL,
    fullname VARCHAR(50) NOT NULL,
    street VARCHAR(50) NOT NULL,
    city VARCHAR(30) NOT NULL,
    state VARCHAR(30) NOT NULL,
    zip VARCHAR(10) NOT NULL,
    phone VARCHAR(15) NOT NULL
);




