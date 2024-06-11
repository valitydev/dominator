ALTER TABLE dmn.party
    ADD COLUMN IF NOT EXISTS name character varying;
ALTER TABLE dmn.party
    ADD COLUMN IF NOT EXISTS comment character varying;
ALTER TABLE dmn.party
    ADD COLUMN IF NOT EXISTS manager_contact_emails character varying;