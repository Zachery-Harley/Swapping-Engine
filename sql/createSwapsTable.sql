DROP TABLE IF EXISTS swaps;
CREATE TABLE swaps (
	id SERIAL PRIMARY KEY,
	buy_order INTEGER NOT NULL,
	sell_order INTEGER NOT NULL,
	price DOUBLE PRECISION NOT NULL,
	size INTEGER NOT NULL,
	swap_time TIMESTAMP NOT NULL
);

