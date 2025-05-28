CREATE TABLE IF NOT EXISTS badge
(
    badge_id    INT PRIMARY KEY,
    name        varchar(100)
);

INSERT IGNORE INTO badge (badge_id, name)
VALUES (1, '스타벅스 첫 방문'),
       (2, '스타벅스 5곳 방문'),
       (3, '스타벅스 10곳 방문'),
       (4, '스타벅스 20곳 방문'),
       (5, '스타벅스 전체 방문'),
       (6, '인기있는 동선에 선정');
