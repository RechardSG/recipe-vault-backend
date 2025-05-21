-- Insert sample recipes
INSERT INTO recipes (id, title, difficulty, instructions, image_url, creator_name, created_date) VALUES
(1, 'Classic Fried Rice', 'EASY', 'Cook rice, stir-fry with vegetables, egg, and soy sauce.', NULL, 'Alice', '2025-05-19 19:32:36'),
(2, 'Spaghetti Carbonara', 'MEDIUM', 'Boil pasta, cook bacon, mix with eggs and cheese.', NULL, 'Bob', '2025-05-19 19:32:36'),
(3, 'Avocado Toast', 'EASY', 'Toast bread, mash avocado with salt and lemon, spread and serve.', NULL, 'Cathy', '2025-05-19 19:32:36'),
(4, 'Beef Stew', 'HARD', 'Brown beef, simmer with vegetables and broth for 2 hours.', NULL, 'Dan', '2025-05-19 19:32:36'),
(5, 'Pancakes', 'EASY', 'Mix flour, egg, milk; cook on a skillet until golden.', NULL, 'Ella', '2025-05-19 19:32:36');

-- Insert sample ingredients
INSERT INTO ingredients (id, name, recipe_id) VALUES
(1, 'Rice', 1),
(2, 'Egg', 1),
(3, 'Carrot', 1),
(4, 'Bacon', 2),
(5, 'Spaghetti', 2),
(6, 'Parmesan', 2),
(7, 'Avocado', 3),
(8, 'Bread', 3),
(9, 'Beef', 4),
(10, 'Potato', 4),
(11, 'Carrot', 4),
(12, 'Flour', 5),
(13, 'Milk', 5),
(14, 'Egg', 5);
