drop database if exists foodpantry_test;
create database foodpantry_test;
use foodpantry_test;

-- create tables and relationships for testing
create table app_user (
    app_user_id int primary key auto_increment,
    username varchar(50) not null unique,
    password_hash varchar(2048) not null,
    enabled bit not null default(1)
);

create table app_role (
    app_role_id int primary key auto_increment,
    `name` varchar(50) not null unique
);

create table app_user_role (
    app_user_id int not null,
    app_role_id int not null,
    constraint fk_app_user_role_app_user
		foreign key (app_user_id)
        references app_user(app_user_id),
	constraint fk_app_user_role_app_role
		foreign key (app_role_id)
        references app_role(app_role_id)
);

create table measurements (
	measurement_id int primary key auto_increment,
    measurement_name text
);

create table ingredients (
	ingredient_id int primary key auto_increment,
    ingredient_name text
);


create table item_pantry (
	item_pantry_id int primary key auto_increment,
    quantity int null,
    ingredient_id int null,
    app_user_id int null,
    measurement_id int null,
    constraint fk_item_pantry_item
		foreign key (ingredient_id)
        references ingredients(ingredient_id),
	constraint fk_item_pantry_app_user
		foreign key (app_user_id)
        references app_user(app_user_id),
	constraint fk_item_pantry_measurements
		foreign key (measurement_id)
        references measurements(measurement_id)
);

create table recipes (
	recipe_id int primary key auto_increment,
    recipe_title varchar(50) null,
    recipe_steps text null,
    recipe_cook_time int null,
    recipe_image text,
    app_user_id int null,
    constraint fk_recipes_app_user
		foreign key (app_user_id)
        references app_user(app_user_id)
);

create table recipe_ingredients (
	recipe_ingredients_id int primary key auto_increment,
    quantity int null,
    recipe_id int null,
    ingredient_id int null,
    measurement_id int null,
    constraint fk_recipe_ingredients_recipes
		foreign key (recipe_id)
        references recipes(recipe_id),
	constraint fk_recipe_ingredients_ingredients
		foreign key (ingredient_id)
        references ingredients(ingredient_id),
	constraint fk_recipe_ingredients_measurements
		foreign key (measurement_id)
        references measurements(measurement_id)
);

delimiter //
create procedure set_known_good_state()
begin


	delete from recipe_ingredients;
	alter table recipe_ingredients auto_increment = 1;
	delete from recipes;
	alter table recipes auto_increment = 1;
    delete from item_pantry;
	alter table item_pantry auto_increment = 1;
    delete from app_user_role;
    delete from app_user;
	alter table app_user auto_increment = 1;
    delete from app_role;
	alter table app_role auto_increment = 1;
	delete from ingredients;
	alter table ingredients auto_increment = 1;
    delete from measurements;
    alter table measurements auto_increment = 1;




	-- passwords are set to "P@ssw0rd!"
	insert into app_user (username, password_hash, enabled) values
		('john@smith.com', '$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa', 1),
		('sally@jones.com', '$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa', 1);

	insert into app_role (`name`) values
		('USER'),
		('ADMIN');

	insert into app_user_role values
		(1, 1),
		(2, 1);

	insert into measurements (measurement_name) values
		('tsp'),('tbsp'),
		('quarter cup'),('half cup'),('cup'),
		('oz'),('fl oz'),('lb'),
		('g'),('kg'),
		('mL'),('L');

	insert into recipes (recipe_title, recipe_steps, recipe_cook_time, recipe_image, app_user_id) values
		('Mac N Cheese', '1st: Get the mac, 2nd: Get the cheese, 3rd: Combine and enjoy!', 20, 'placeholder for image for now', 1),
		('Frozen Dihydrogen Monoxide', '1st: Fill up your ice cube trey with plain old regulard water, 2nd: Put in freezer for one hour and let the magic happen!', 60, 'placeholder for image for now', 1);

	insert into ingredients (ingredient_name) values
		('Salt'), ('Pepper'), ('Olive Oil'),
		('Garlic'), ('Onion'), ('Butter'), ('Eggs'),
		('Flour'), ('Sugar'), ('Milk'), ('Tomatoes'),
		('Rice'), ('Chicken'), ('Pasta'), ('Lemon'),
		('Lime'), ('Basil'), ('Oregano'), ('Cumin'),
		('Paprika'), ('Cinnamon'), ('Cheese'),
		('Vinegar'),  ('Chicken Broth'), ('Beef Broth'),
		('Water'), ('Oxygen');

insert into item_pantry (quantity, ingredient_id, app_user_id, measurement_id) values
	(50, 1, 1, 1),
    (500, 2, 1, 1),
    (300, 3, 1, 2);

insert into recipe_ingredients (quantity, recipe_id, ingredient_id, measurement_id) values
	(50, 1, 1, 1),
    (1, 1, 6, 1),
    (1, 1, 10, 2),
    (500, 1, 22, 1),
    (2, 2, 26, 2),
    (1, 2, 27, 2);

 end //
-- 4. Change the statement terminator back to the original.
delimiter ;


