CREATE TABLE dmn.identity
(
    id                             bigserial                                                        NOT NULL,
    event_created_at               timestamp without time zone                                      NOT NULL,
    event_occured_at               timestamp without time zone                                      NOT NULL,
    sequence_id                    integer                                                          NOT NULL,
    party_id                       character varying                                                NOT NULL,
    party_contract_id              character varying,
    identity_id                    character varying                                                NOT NULL,
    identity_provider_id           character varying                                                NOT NULL,
    identity_effective_chalenge_id character varying,
    identity_level_id              character varying,
    wtime                          timestamp without time zone DEFAULT timezone('utc'::text, now()) NOT NULL,
    current                        boolean                     DEFAULT true                         NOT NULL,
    external_id                    character varying,
    blocked                        boolean,
    context_json                   character varying,
    CONSTRAINT identity_pkey PRIMARY KEY (id),
    CONSTRAINT identity_uniq UNIQUE (identity_id, sequence_id)
);

CREATE INDEX identity_event_created_at_idx ON dmn.identity USING btree (event_created_at);
CREATE INDEX identity_event_occured_at_idx ON dmn.identity USING btree (event_occured_at);
CREATE INDEX identity_id_idx ON dmn.identity USING btree (identity_id);
CREATE INDEX identity_party_id_idx ON dmn.identity USING btree (party_id);




CREATE TABLE dmn.wallet
(
    id                   bigserial                                                        NOT NULL,
    event_created_at     timestamp without time zone                                      NOT NULL,
    event_occured_at     timestamp without time zone                                      NOT NULL,
    sequence_id          integer                                                          NOT NULL,
    wallet_id            character varying                                                NOT NULL,
    wallet_name          character varying                                                NOT NULL,
    identity_id          character varying,
    party_id             character varying,
    currency_code        character varying,
    wtime                timestamp without time zone DEFAULT timezone('utc'::text, now()) NOT NULL,
    current              boolean                     DEFAULT true                         NOT NULL,
    account_id           character varying,
    accounter_account_id bigint,
    external_id          character varying,
    CONSTRAINT wallet_pkey PRIMARY KEY (id),
    CONSTRAINT wallet_uniq UNIQUE (wallet_id, sequence_id)
);

CREATE INDEX wallet_event_created_at_idx ON dmn.wallet USING btree (event_created_at);
CREATE INDEX wallet_event_occured_at_idx ON dmn.wallet USING btree (event_occured_at);
CREATE INDEX wallet_id_idx ON dmn.wallet USING btree (wallet_id);




CREATE TYPE dmn.challenge_resolution AS ENUM (
    'approved',
    'denied'
    );

CREATE TYPE dmn.challenge_status AS ENUM (
    'pending',
    'cancelled',
    'completed',
    'failed'
    );


CREATE TABLE dmn.challenge
(
    id                    bigserial                                                        NOT NULL,
    event_created_at      timestamp without time zone                                      NOT NULL,
    event_occured_at      timestamp without time zone                                      NOT NULL,
    sequence_id           integer                                                          NOT NULL,
    identity_id           character varying                                                NOT NULL,
    challenge_id          character varying                                                NOT NULL,
    challenge_class_id    character varying                                                NOT NULL,
    challenge_status      dmn.challenge_status                                             NOT NULL,
    challenge_resolution  dmn.challenge_resolution,
    challenge_valid_until timestamp without time zone,
    wtime                 timestamp without time zone DEFAULT timezone('utc'::text, now()) NOT NULL,
    current               boolean                     DEFAULT true                         NOT NULL,
    proofs_json           character varying,
    CONSTRAINT challenge_pkey PRIMARY KEY (id),
    CONSTRAINT challenge_uniq UNIQUE (challenge_id, identity_id, sequence_id)
);

CREATE INDEX challenge_event_created_at_idx ON dmn.challenge USING btree (event_created_at);
CREATE INDEX challenge_event_occured_at_idx ON dmn.challenge USING btree (event_occured_at);
CREATE INDEX challenge_id_idx ON dmn.challenge USING btree (challenge_id);
