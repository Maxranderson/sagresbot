create table users (
  id INTEGER PRIMARY KEY,
  is_bot INTEGER NOT NULL,
  first_name TEXT NOT NULL,
  last_name TEXT,
  username TEXT,
  language_code TEXT,
  can_join_groups INTEGER,
  can_read_all_group_messages INTEGER,
  supports_inline_queries INTEGER
)