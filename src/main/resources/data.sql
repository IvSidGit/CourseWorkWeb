-- src/main/resources/data.sql

-- Очистка таблицы (опционально)
DELETE FROM artists WHERE id IS NOT NULL;

-- Вставка тестовых данных
INSERT INTO artists (name, age, slug, description) VALUES
('Taylor Swift', 34, 'taylor-swift', 'Американская певица, автор песен, известная своими автобиографическими песнями'),
('The Weeknd', 34, 'the-weeknd', 'Канадский певец и автор песен, известный своим уникальным вокалом'),
('Billie Eilish', 22, 'billie-eilish', 'Американская певица и автор песен, ставшая популярной в подростковом возрасте');

-- Если есть AUTO_INCREMENT, можно явно указать ID
-- INSERT INTO artists (id, name, age, slug, description) VALUES
-- (1, 'Taylor Swift', 34, 'taylor-swift', '...'),
-- (2, 'The Weeknd', 34, 'the-weeknd', '...'),
-- (3, 'Billie Eilish', 22, 'billie-eilish', '...');