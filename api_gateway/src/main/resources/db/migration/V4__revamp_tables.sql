DROP TABLE IF EXISTS languages;
DROP TABLE IF EXISTS auth_methods;
DROP TABLE IF EXISTS users;


CREATE TABLE IF NOT EXISTS users(
  user_id SERIAL PRIMARY KEY,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
  name VARCHAR(255) NOT NULL
);


CREATE TABLE IF NOT EXISTS email_auth(
  auth_id SERIAL PRIMARY KEY,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
  user_id INTEGER NOT NULL UNIQUE,
  email VARCHAR(255) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS api_keys(
  api_key_id SERIAL PRIMARY KEY,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
  user_id INTEGER NOT NULL UNIQUE,
  api_key TEXT NOT NULL UNIQUE,
  is_valid BOOLEAN NOT NULL DEFAULT false,
  FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS code_submissions(
    submission_id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    api_key_id INTEGER NOT NULL,
    is_submission_complete BOOLEAN NOT NULL DEFAULT false,
    submission_time TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    source_code TEXT,
    stdin TEXT,
    stdout TEXT,
    expected_output TEXT,
    time_limit FLOAT,
    memory_limit FLOAT,
    cpu_time_limit FLOAT,
    wall_time_limit FLOAT,
    memory FLOAT,
    cpu_time FLOAT,
    wall_time FLOAT,
    exit_status INTEGER,
    message TEXT,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE SET NULL,
    FOREIGN KEY (api_key_id) REFERENCES api_keys(api_key_id) ON DELETE SET NULL
);

