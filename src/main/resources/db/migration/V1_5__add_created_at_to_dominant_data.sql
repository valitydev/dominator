ALTER TABLE dmn.provider
    ADD COLUMN IF NOT EXISTS created_at timestamp without time zone NULL;
ALTER TABLE dmn.term_set_hierarchy
    ADD COLUMN IF NOT EXISTS created_at timestamp without time zone NULL;
ALTER TABLE dmn.terminal
    ADD COLUMN IF NOT EXISTS created_at timestamp without time zone NULL;
