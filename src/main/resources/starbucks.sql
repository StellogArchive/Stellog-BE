CREATE TABLE IF NOT EXISTS starbucks
(
    starbucks_id    INT PRIMARY KEY,
    name            VARCHAR(100),
    address         VARCHAR(255),
    latitude        DOUBLE,
    longitude       DOUBLE
);

INSERT IGNORE INTO starbucks (starbucks_id, name, address, latitude, longitude)
VALUES (1, '제주세화DT', '제주특별자치도 제주시 구좌읍 일주동로 3121', 33.522827, 126.853291),
       (2, '더제주송당파크R', '제주특별자치도 제주시 구좌읍 비자림로 1189', 33.435586, 126.731656),
       (3, '제주함덕', '제주특별자치도 제주시 조천읍 조함해안로 522', 33.542497, 126.668694),
       (4, '제주삼화DT', '제주특별자치도 제주시 연삼로 884 (도련일동)', 33.51189, 126.580329),
       (5, '제주칠성', '제주특별자치도 제주시 관덕로 55 (일도일동)', 33.513556, 126.525222),
       (6, '제주일도DT', '제주특별자치도 제주시 동광로 130 (일도이동)', 33.507511, 126.54065),
       (7, '제주시청', '제주특별자치도 제주시 중앙로 215 (이도이동)', 33.500776, 126.529691),
       (8, '제주도남DT', '제주특별자치도 제주시 연북로 394 (도남동)', 33.480416, 126.523607),
       (9, '제주서해안로DT', '제주특별자치도 제주시 서해안로 624 (용담삼동)', 33.516796, 126.503561),
       (10, '제주공항DT', '제주특별자치도 제주시 월성로 42 (용담이동)', 33.500603, 126.505383),
       (11, '제주용담DT', '제주특별자치도 제주시 서해안로 380 (용담삼동) 화이트하우스', 33.512414, 126.484548),
       (12, '제주노형DT', '제주특별자치도 제주시 월랑로 68 (노형동)', 33.490921, 126.47422),
       (13, '제주노형', '제주특별자치도 제주시 도령로 27 (노형동)', 33.487799, 126.481892),
       (14, '신제주이마트', '제주특별자치도 제주시 1100로 3348, 신제주이마트 1층 (노형동)', 33.484806, 126.480452),
       (15, '제주노형로', '제주특별자치도 제주시 노형로 376 (노형동)', 33.483597, 126.476184),
       (16, '제주한라수목원DT', '제주특별자치도 제주시 수목원서길 3-5 (노형동)', 33.469159, 126.484721),
       (17, '제주평화로DT', '제주특별자치도 제주시 노형로 37 (해안동)', 33.463309, 126.451046),
       (18, '제주외도DT', '제주특별자치도 제주시 일주서로 7300 (외도일동)', 33.492439, 126.427186),
       (19, '제주애월DT', '제주특별자치도 제주시 애월읍 애월해안로 376', 33.472836, 126.348722),
       (20, '제주한담해변DT', '제주특별자치도 제주시 애월읍 일주서로 6142', 33.458332, 126.310962),
       (21, '제주협재', '제주특별자치도 제주시 한림읍 한림로 337', 33.39392, 126.240345),
       (22, '제주금악DT', '제주특별자치도 제주시 한림읍 평화로 1360-2', 33.348047, 126.359363),
       (23, '성산일출봉', '제주특별자치도 서귀포시 성산읍 일출로 284-5 1,2층', 33.462847, 126.935538),
       (24, '제주성산DT', '제주특별자치도 서귀포시 성산읍 일출로 80', 33.449721, 126.920645),
       (25, '제주서귀포남원DT', '제주특별자치도 서귀포시 남원읍 일주동로 7129', 33.283032, 126.719004),
       (26, '서귀포올레', '제주특별자치도 서귀포시 중정로 69 (서귀동)', 33.248298, 126.563781),
       (27, '서귀포DT', '제주특별자치도 서귀포시 일주서로 11 (강정동)', 33.24955, 126.506737),
       (28, '제주중문DT', '제주특별자치도 서귀포시 천제연로 95(색달동)', 33.254999, 126.415905),
       (29, '제주중문', '제주특별자치도 서귀포시 중문관광로110번길 32 (색달동)', 33.251226, 126.41283),
       (30, '제주신화월드R', '제주특별자치도 서귀포시 안덕면 신화역사로304번길 38 B1-29', 33.306181, 126.316534),
       (31, '제주에듀시티', '제주특별자치도 서귀포시 대정읍 에듀시티로 36', 33.291841, 126.284302),
       (32, '제주모슬포DT', '제주특별자치도 서귀포시 대정읍 일주서로 2549', 33.227275, 126.257909),
       (33, '제주송악산', '제주특별자치도 서귀포시 대정읍 형제해안로 322', 33.206706, 126.289837);