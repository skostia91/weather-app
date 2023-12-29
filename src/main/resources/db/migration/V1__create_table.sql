CREATE TABLE users(
                      id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                      login VARCHAR(45) NOT NULL UNIQUE,
                      password varchar(205) NOT NULL check(length(password) > 2)
);

CREATE TABLE locations(
                      id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                      name varchar(45) not null,
                      user_id int REFERENCES users (id) ON DELETE CASCADE,
                      latitude numeric NOT NULL,
                      longitude numeric NOT NULL
);

CREATE UNIQUE INDEX idx_base_target ON locations (user_id, name);

CREATE TABLE sessions (
                          id varchar(50) PRIMARY KEY,
                          user_id int REFERENCES users (id) ON DELETE CASCADE,
                          expires_at TIMESTAMP(6)
);
CREATE INDEX sessions_unique_user_id_idx ON sessions (user_id);
