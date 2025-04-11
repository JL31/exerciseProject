---
--- Queries to creates tables
---

CREATE TABLE IF NOT EXISTS exercise (
	uuid UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
	created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE,
    name VARCHAR(100),
    type VARCHAR(50),
    duration INTEGER,
    number_of_series INTEGER,
    rest_duration_between_series INTEGER,
    number_of_repetitions INTEGER,
    distance INTEGER
);

