CREATE SCHEMA IF NOT EXISTS dmn;



CREATE TABLE dmn.dominant_last_version_id
(
    version_id BIGINT                      NOT NULL,
    wtime      TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() at time zone 'utc')
);

insert into dmn.dominant_last_version_id(version_id)
values (1);



CREATE TABLE dmn.provider
(
    id                              bigserial                                                        NOT NULL,
    version_id                      bigint                                                           NOT NULL,
    provider_ref_id                 integer                                                          NOT NULL,
    name                            character varying                                                NOT NULL,
    description                     character varying                                                NOT NULL,
    proxy_ref_id                    integer                                                          NOT NULL,
    terminal_json                   character varying,
    terminal_object                 bytea,
    abs_account                     character varying,
    payment_terms_json              character varying,
    payment_terms_object            bytea,
    recurrent_paytool_terms_json    character varying,
    recurrent_paytool_terms_object  bytea,
    accounts_json                   character varying,
    wtime                           timestamp without time zone DEFAULT timezone('utc'::text, now()) NOT NULL,
    current                         boolean                     DEFAULT true                         NOT NULL,
    identity                        character varying,
    wallet_terms_json               character varying,
    wallet_terms_object             bytea,
    params_schema_json              character varying,
    params_schema_object            bytea[],
    CONSTRAINT provider_pkey PRIMARY KEY (id)
);

CREATE INDEX provider_idx ON dmn.provider USING btree (provider_ref_id);
CREATE INDEX provider_version_id ON dmn.provider USING btree (version_id);


CREATE TABLE dmn.term_set_hierarchy
(
    id                        bigserial                                                        NOT NULL,
    version_id                bigint                                                           NOT NULL,
    term_set_hierarchy_ref_id integer                                                          NOT NULL,
    name                      character varying,
    description               character varying,
    parent_terms_ref_id       integer,
    term_sets_json            character varying                                                NOT NULL,
    term_set_hierarchy_object bytea                                                            NOT NULL,
    wtime                     timestamp without time zone DEFAULT timezone('utc'::text, now()) NOT NULL,
    current                   boolean                     DEFAULT true                         NOT NULL,
    CONSTRAINT term_set_hierarchy_pkey PRIMARY KEY (id)
);

CREATE UNIQUE INDEX term_set_hierarchy_unq_idx ON dmn.term_set_hierarchy USING btree (term_set_hierarchy_ref_id, version_id);
CREATE INDEX term_set_hierarchy_idx ON dmn.term_set_hierarchy USING btree (term_set_hierarchy_ref_id);
CREATE INDEX term_set_hierarchy_version_id ON dmn.term_set_hierarchy USING btree (version_id);



CREATE TABLE dmn.terminal
(
    id                         bigserial                                                        NOT NULL,
    version_id                 bigint                                                           NOT NULL,
    terminal_ref_id            integer                                                          NOT NULL,
    name                       character varying                                                NOT NULL,
    description                character varying                                                NOT NULL,
    risk_coverage              character varying,
    terms_json                 character varying,
    terms_object               bytea,
    wtime                      timestamp without time zone DEFAULT timezone('utc'::text, now()) NOT NULL,
    current                    boolean                     DEFAULT true                         NOT NULL,
    external_terminal_id       character varying,
    external_merchant_id       character varying,
    mcc                        character varying,
    terminal_provider_ref_id   integer,
    CONSTRAINT terminal_pkey PRIMARY KEY (id)
);

CREATE INDEX terminal_idx ON dmn.terminal USING btree (terminal_ref_id);
CREATE INDEX terminal_version_id ON dmn.terminal USING btree (version_id);