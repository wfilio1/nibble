drop database if exists foodpantry;
create database foodpantry;
use foodpantry;

drop table if exists app_role;
drop table if exists app_user;

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

CREATE TABLE app_user_role (
    app_user_id INT NOT NULL,
    app_role_id INT NOT NULL,
    constraint fk_app_user_role_app_user
		FOREIGN KEY (app_user_id)
        REFERENCES app_user(app_user_id),
	constraint fk_app_user_role_app_role
		FOREIGN KEY (app_role_id)
        REFERENCES app_role(app_role_id)
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


-- set up inserts into our OG database

-- passwords are set to "P@ssw0rd!"
insert into app_user (username, password_hash, enabled) values
    ('john@smith.com', '$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa', 1),
    ('sally@jones.com', '$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa', 1),
    ('wendy@filio.com', '$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa', 1),
    ('gee@wang.com', '$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa', 1),
    ('joshua@smesny.com', '$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa', 1),
    ('tyler@robinson.com', '$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa', 1);

insert into app_role (`name`) values
    ('USER'),
    ('ADMIN');

insert into app_user_role values
    (1, 1),
    (2, 1),
    (3, 1),
    (4, 1),
    (5, 1);

insert into measurements (measurement_name) values
    ('tsp'),('tbsp'),
    ('quarter cup'),('half cup'),('cup'),
    ('oz'),('fl oz'),('lb'),
    ('g'),('kg'),
    ('mL'),('L'), ('slice'), ('piece');

insert into recipes (recipe_title, recipe_steps, recipe_cook_time, recipe_image, app_user_id) values
	('Mac N Cheese', '1st: Get the mac\n2nd: Get the cheese\n3rd: Combine and enjoy!', 20, 'https://images.unsplash.com/photo-1667499989723-c4ab9549d63c?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1035&q=80', 1),
	('Frozen Dihydrogen Monoxide', '1st: Fill up your ice cube tray with plain old regular water\n2nd: Put in freezer for one hour and let the magic happen!', 60, 'https://images.unsplash.com/photo-1612984349637-0267e40c8a84?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=2070&q=80', 1),
    ('Kare Kare', '1st: In a large pot, bring the water to a boil\n2nd: Put in the oxtail followed by the onions and simmer for 2.5 to 3 hrs or until tender (35 minutes if using a pressure cooker)\n3rd: Once the meat is tender, add the ground peanuts, peanut butter, and coloring (water from the annatto seed mixture) and simmer for 5 to 7 minutes\n4th: Add the toasted ground rice and simmer for 5 minutes\n5th: On a separate pan, saute the garlic then add the banana flower, eggplant, and string beans and cook for 5 minutes\n6th: Transfer the cooked vegetables to the large pot (where the rest of the ingredients are)\n7th: Add salt and pepper to taste\n8th: Serve hot with shrimp paste. Enjoy!', 160, 'https://panlasangpinoy.com/wp-content/uploads/2009/05/kare-kare-recipe-panlasang-pinoy.jpg', 3),
    ('Galbi Jjim', '1st: Soak the beef ribs in cold water for 10 to 20 mins. This is to draw out the red liquid (which looks like blood) from the meat. Change the water a few times during this time\n2nd: Blend the sauce ingredients in a mixer or food processor. Set it aside until needed\n3rd: In a large pot, in rolling boiling water, boil the ribs for 6 to 8 minutes over medium high heat. Drain the water and rinse the ribs in cold running water. Cut off any obvious excess fat from the ribs\n4th: Place the ribs in a dutch oven and pour the water in (1 & 1/2 cups). Add 2/3 of the sauce. Boil them over medium heat for about 30 mins, covered. Stir gently and change the position of the ribs. Reduce the heat to medium low and continue to simmer for about 10 minutes, covered\n5th: Add the remaining ingredients (carrots, jujube, gingko nuts, chestnuts and the remaining sauce) and continue to simmer it over medium low heat for about 20 mins, covered\n6th: By now the ribs should be fully cooked, but they may not be tender. Continue to simmer them over medium low heat until your desired tenderness is achieved. (I normally simmer it for an additional 1 hour, covered.) Depending on how long you simmered it, you may still have a reasonable amount of sauce/liquid left in the pot. To boil it off, you can heat up the stove to medium high heat and boil it for 10 to 12 minutes, covered. This should thicken the sauce and leaves just the right amount of it at the bottom of the pot. Alternatively, you can continue to boil off the sauce over medium low heat until the sauce nearly disappears. (This can take another 1 hour or so. And this will make the meat off the bone. FYI, many Korean moms would say that the bones shouldn’t fall off as it’s visually less appealing when you serve it.)\n7th: Serve galbi jjim hot with steamed rice and other Korean side dishes. To reheat, add 1/3 cup of water in a dutch oven and boil it for 5 to 7 mins over medium high heat, covered.', 165, 'https://mykoreankitchen.com/wp-content/uploads/2017/12/1.-Kalbi-Jjim.jpg', 4),
	('Takoyaki', '1st: Grind ¼ cup katsuobushi (dried bonito flakes) into a fine powder. Set aside; we‘ll use this powder when we‘re cooking the takoyaki\n2nd: Cut 2 green onions/scallions into fine slices and mince 1 Tbsp pickled red ginger (beni shoga or kizami beni shoga)\n3rd: Cut 4.0 oz octopus sashimi (boiled octopus) into ½-inch (1.3-cm) bite-sized pieces; cut into smaller pieces for kids so they can chew it more easily. I use the rangiri cutting technique.)\n4th: In a large mixing bowl, combine 1 cup all-purpose flour (plain flour), 2 tsp baking powder, and ½ tsp Diamond Crystal kosher salt and whisk it all together\n5th: Add 2 large eggs (50 g each w/o shell), 1 tsp soy sauce, and 1½ cups dashi (Japanese soup stock)\n6th: Whisk it all together until well blended and transfer the batter to a measuring cup with a handle (or any other pitcher with a spout for easy pouring.)\n7th: Heat the takoyaki pan to 400ºF (200ºC) over medium heat. Use a brush to generously grease the pan‘s rounded chambers and flat top surface with 2 Tbsp neutral oil. When smoke starts to rise, pour the batter to fill the chambers. It’s okay to slightly overfill the cavities. In the next steps, the batter will likely overflow as you add more ingredients to it\n8th: Add 1–3 octopus pieces, depending on their size, to each chamber and sprinkle on top the katsuobushi powder that you ground earlier\n9th: Sprinkle ⅓ cup tenkasu/agedama (tempura scraps), the green onion slices, and the chopped pickled red ginger on top. After 3 minutes or so, when the batter on the bottom has set and is slightly crisp, use skewers to break the connected batter between each chamber. Then, rotate each piece 90 degrees (a quarter turn) toward the bottom of the pan, stuffing the connected dough back into the ball as you turn it. The uncooked batter will flow out from inside to create another side of the ball. After you finish turning them, set a timer for 4 minutes\n10th: After 4 minutes, rotate them again, starting with the first ball: Turn each takoyaki another 90 degrees toward the bottom of the pan so the remaining uncooked batter pours out into the chamber to complete the ball shape. Home takoyaki griddles don‘t distribute heat evenly, so it’s a good idea to swap the balls around to different chambers so they brown evenly. After turning and cooking for another 4 minutes, they are done, 11th: Transfer them onto a plate and drizzle ½ cup takoyaki sauce and Japanese Kewpie mayonnaise on top. Finish the dish with a sprinkling of katsuobushi (dried bonito flakes) and aonori (dried green laver seaweed) and a side of pickled red ginger (beni shoga or kizami beni shoga). Serve immediately. (But, be careful—they‘re VERY hot inside!)\n12th: You can keep the leftovers in an airtight container and store in the refrigerator for 3 days or in the freezer for 2–3 weeks.', 25, 'https://www.justonecookbook.com/wp-content/uploads/2013/10/Takoyaki-NEW-400x400.jpg', 5),
    ('Caprese Salad',
    '1st: Slice mozzarella and tomatoes.\n
    2nd: Arrange them in alternating layers on a plate.\n
    3rd: Top with fresh basil leaves.\n
    4th: Drizzle with olive oil and sprinkle with salt and pepper.',
    10,
    'https://images.unsplash.com/photo-1529312266912-b33cfce2eefd?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1374&q=80',
    1),
    ('Garlic Butter Shrimp',
    'Melt butter in a pan.\n
    Add minced garlic and sauté until fragrant.\n
    Add shrimp and cook until pink and opaque.',
    20,
    'https://images.unsplash.com/photo-1632660345494-9cc2607f89c2?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1631&q=80',
    2),
    ('Honey Soy Salmon',
    'Mix honey and soy sauce.\n
    Marinate salmon in the mixture for a few minutes.\n
    Grill or bake until the salmon is cooked through.',
    15,
    'https://images.unsplash.com/photo-1519708227418-c8fd9a32b7a2?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1470&q=80',
    3)
    ;

insert into ingredients (ingredient_name) values
    ('Salt'), ('Pepper'), ('Olive Oil'),
    ('Garlic'), ('Onion'), ('Butter'), ('Eggs'),
    ('Flour'), ('Sugar'), ('Milk'), ('Tomatoes'),
    ('Rice'), ('Chicken'), ('Pasta'), ('Lemon'),
    ('Lime'), ('Basil'), ('Oregano'), ('Cumin'),
    ('Paprika'), ('Cinnamon'), ('Cheese'),
    ('Vinegar'),  ('Chicken Broth'), ('Beef Broth'),
    ('Water'), ('Oxygen'), ('Oxtail'), ('Bok Choy'),
    ('String Beans'), ('Eggplant'), ('Peanuts'),
    ('Peanut Butter'), ('Shrimp Paste'), ('Annato Seeds'),
    ('Toasted Ground Rice'), ('Red Apple'), ('Soy Sauce'),
	('Brown Sugar'), ('Honey'), ('Rice Wine'), ('Sesame Oil'),
	('Black Pepper'), ('Beef Short Ribs'), ('Carrots'),
	('Chestnuts'), ('Gingko nuts'), ('Dried Jujube'),
    ('Baking Powder'), ('Dashi'), ('Cooking Oil'),
    ('Tenkasu'), ('Takoyaki Sauce'), ('Kewpie Mayonnaise'),
    ('Bonito Flakes'), ('Aonori'), ('Octopus'), ('Bread');


insert into item_pantry (quantity, ingredient_id, app_user_id, measurement_id) values
	(50, 1, 1, 1),
    (500, 2, 1, 1),
    (300, 3, 1, 2);

insert into recipe_ingredients (quantity, recipe_id, ingredient_id, measurement_id) values
	(50, 1, 1, 1),
    (1, 1, 6, 1),
    (1, 1, 10, 2),
    (1, 1, 22, 13),
    (2, 2, 26, 2),
    (1, 2, 27, 2),
    (3, 3, 28, 8),
    (250, 3, 29, 9),
    (250, 3, 30, 9),
    (3, 3, 31, 9),
    (1, 3, 32, 5),
    (1, 3, 33, 4),
    (1, 3, 34, 4),
    (1, 3, 26, 12),
    (1, 3, 35, 4),
    (1, 3, 36, 4),
    (1, 3, 4, 2),
    (75, 3, 5, 9),
    (40, 3, 1, 9),
    (30, 3, 2, 9),
    (170, 4, 37, 9),
    (60, 4, 5, 9),
    (6, 4, 38, 2),
    (2, 4, 39, 2),
    (2, 4, 40, 2),
    (2, 4, 41, 2),
    (1, 4, 4, 2),
    (1, 4, 42, 1),
    (40, 4, 43, 9),
    (2, 4, 44, 10),
    (3, 4, 26, 4),
    (275, 4, 45, 9),
    (30, 4, 46, 9),
    (30, 4, 47, 9),
    (15, 4, 48, 9),

    (1, 5, 8, 5),
    (2, 5, 49, 1),
    (1, 5, 1, 1),
    (100, 5, 7, 9),
    (1, 5, 38, 1),
    (3, 5, 50, 4),
    (2, 5, 51, 1),
    (1, 5, 52, 4),
    (1, 5, 53, 5),
    (100, 5, 54, 9),
    (50, 5, 55, 9),
    (50, 5, 56, 9),
    (4, 5, 57, 6),
    (1,6,11,1),
    (1,6,22,2),
    (1,6,3,3),
    (1,7,4,4),
    (1,7,6,5),
    (1,7,34,6),
    (1,8,40,7),
    (1,8,38,8),
    (1,8,1,9);


SELECT * FROM ingredients;

SELECT * FROM recipes;

SELECT * FROM app_user;

SELECT * FROM app_user_role;

SELECT * FROM app_user as a
join app_user_role as ar on a.app_user_id = ar.app_user_id
join app_role as a_role on ar.app_role_id = a_role.app_role_id;

SELECT * FROM measurements;

select item_pantry_id, quantity, ingredient_id, app_user_id, measurement_id
from item_pantry
order by item_pantry_id;

SELECT * FROM recipe_ingredients;
