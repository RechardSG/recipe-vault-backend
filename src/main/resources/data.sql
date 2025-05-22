-- Insert into recipes
INSERT INTO recipes (id, title, difficulty, creator_name, instructions, created_date) VALUES
(1, 'Pasta', 'MEDIUM', 'Lulu', 'Cook pasta, sprinkle herbs in olive oil, mix with pasta, and top with parmesan.', '2025-05-20T12:30:00'),
(2, 'Smoked Omelette', 'HARD', 'Rechard', 'Mix eggs with spices, cook over heat, fold in cheese.', '2025-05-21T09:45:00'),
(3, 'Fish Curry', 'HARD', 'Kishor', 'Boil fish in coconut curry base, finish with curry leaves.', '2025-05-18T15:00:00'),
(4, 'Breakfast Smoothie', 'EASY', 'Ritwik', 'Blend banana and berries with yogurt, pour into bowl, top with oats and honey.', '2025-05-22T08:00:00'),
(5, 'Stir Fry Tofu', 'MEDIUM', 'Yizhen', 'Stir fry tofu and vegetables with soy-ginger sauce until crisp.', '2025-05-19T19:15:00');

-- Insert into ingredients
INSERT INTO ingredients (name, recipe_id) VALUES
('Spaghetti', 1),
('Basil', 1),
('Thyme', 1),
('Olive Oil', 1),
('Parmesan', 1),

('Eggs', 2),
('Smoked Paprika', 2),
('Onions', 2),
('Cheese', 2),

('White Fish', 3),
('Coconut Milk', 3),
('Curry Leaves', 3),
('Spices', 3),

('Banana', 4),
('Berries', 4),
('Oats', 4),
('Yogurt', 4),
('Honey', 4),

('Tofu', 5),
('Ginger', 5),
('Soy Sauce', 5),
('Pepper', 5),
('Broccoli', 5);
