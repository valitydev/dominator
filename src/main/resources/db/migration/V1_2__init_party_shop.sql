
CREATE TYPE dmn.contract_status AS ENUM (
    'active',
    'terminated',
    'expired'
    );

CREATE TYPE dmn.representative_document AS ENUM (
    'articles_of_association',
    'power_of_attorney',
    'expired'
    );

CREATE TABLE dmn.contract
(
    id                                                         bigserial                                                        NOT NULL,
    event_created_at                                           timestamp without time zone                                      NOT NULL,
    contract_id                                                character varying                                                NOT NULL,
    party_id                                                   character varying                                                NOT NULL,
    payment_institution_id                                     integer,
    created_at                                                 timestamp without time zone                                      NOT NULL,
    valid_since                                                timestamp without time zone,
    valid_until                                                timestamp without time zone,
    status                                                     dmn.contract_status                                               NOT NULL,
    status_terminated_at                                       timestamp without time zone,
    terms_id                                                   integer                                                          NOT NULL,
    legal_agreement_signed_at                                  timestamp without time zone,
    legal_agreement_id                                         character varying,
    legal_agreement_valid_until                                timestamp without time zone,
    report_act_schedule_id                                     integer,
    report_act_signer_position                                 character varying,
    report_act_signer_full_name                                character varying,
    report_act_signer_document                                 dmn.representative_document,
    report_act_signer_doc_power_of_attorney_signed_at          timestamp without time zone,
    report_act_signer_doc_power_of_attorney_legal_agreement_id character varying,
    report_act_signer_doc_power_of_attorney_valid_until        timestamp without time zone,
    contractor_id                                              character varying,
    wtime                                                      timestamp without time zone DEFAULT timezone('utc'::text, now()) NOT NULL,
    current                                                    boolean                     DEFAULT true                         NOT NULL,
    sequence_id                                                integer,
    change_id                                                  integer,
    claim_effect_id                                            integer,
    CONSTRAINT contract_pkey PRIMARY KEY (id),
    CONSTRAINT contract_uniq UNIQUE (party_id, contract_id, sequence_id, change_id, claim_effect_id)
);

CREATE INDEX contract_contract_id ON dmn.contract USING btree (contract_id);
CREATE INDEX contract_created_at ON dmn.contract USING btree (created_at);
CREATE INDEX contract_event_created_at ON dmn.contract USING btree (event_created_at);
CREATE INDEX contract_party_id ON dmn.contract USING btree (party_id);



CREATE TABLE dmn.contract_adjustment
(
    id                     bigserial                   NOT NULL,
    contract_id            bigint                      NOT NULL,
    contract_adjustment_id character varying           NOT NULL,
    created_at             timestamp without time zone NOT NULL,
    valid_since            timestamp without time zone,
    valid_until            timestamp without time zone,
    terms_id               integer                     NOT NULL,
    CONSTRAINT contract_adjustment_pkey PRIMARY KEY (id)
);

CREATE INDEX contract_adjustment_idx ON dmn.contract_adjustment USING btree (contract_id);



CREATE TABLE dmn.contract_revision
(
    id       bigserial                                                        NOT NULL,
    obj_id   bigint                                                           NOT NULL,
    revision bigint                                                           NOT NULL,
    wtime    timestamp without time zone DEFAULT timezone('utc'::text, now()) NOT NULL,
    CONSTRAINT contract_revision_pkey PRIMARY KEY (id)
);

CREATE UNIQUE INDEX contract_revision_idx ON dmn.contract_revision USING btree (obj_id, revision);



CREATE TYPE dmn.contractor_type AS ENUM (
    'registered_user',
    'legal_entity',
    'private_entity'
    );

CREATE TYPE dmn.legal_entity AS ENUM (
    'russian_legal_entity',
    'international_legal_entity'
    );

CREATE TYPE dmn.private_entity AS ENUM (
    'russian_private_entity'
    );

CREATE TABLE dmn.contractor
(
    id                                             bigserial                                                        NOT NULL,
    event_created_at                               timestamp without time zone                                      NOT NULL,
    party_id                                       character varying                                                NOT NULL,
    contractor_id                                  character varying                                                NOT NULL,
    type                                           dmn.contractor_type                                               NOT NULL,
    identificational_level                         character varying,
    registered_user_email                          character varying,
    legal_entity                                   dmn.legal_entity,
    russian_legal_entity_registered_name           character varying,
    russian_legal_entity_registered_number         character varying,
    russian_legal_entity_inn                       character varying,
    russian_legal_entity_actual_address            character varying,
    russian_legal_entity_post_address              character varying,
    russian_legal_entity_representative_position   character varying,
    russian_legal_entity_representative_full_name  character varying,
    russian_legal_entity_representative_document   character varying,
    russian_legal_entity_russian_bank_account      character varying,
    russian_legal_entity_russian_bank_name         character varying,
    russian_legal_entity_russian_bank_post_account character varying,
    russian_legal_entity_russian_bank_bik          character varying,
    international_legal_entity_legal_name          character varying,
    international_legal_entity_trading_name        character varying,
    international_legal_entity_registered_address  character varying,
    international_legal_entity_actual_address      character varying,
    international_legal_entity_registered_number   character varying,
    private_entity                                 dmn.private_entity,
    russian_private_entity_first_name              character varying,
    russian_private_entity_second_name             character varying,
    russian_private_entity_middle_name             character varying,
    russian_private_entity_phone_number            character varying,
    russian_private_entity_email                   character varying,
    wtime                                          timestamp without time zone DEFAULT timezone('utc'::text, now()) NOT NULL,
    current                                        boolean                     DEFAULT true                         NOT NULL,
    sequence_id                                    integer,
    change_id                                      integer,
    claim_effect_id                                integer,
    international_legal_entity_country_code        character varying,
    CONSTRAINT contractor_pkey PRIMARY KEY (id),
    CONSTRAINT contractor_uniq UNIQUE (party_id, contractor_id, sequence_id, change_id, claim_effect_id)
);

CREATE INDEX contractor_contractor_id ON dmn.contractor USING btree (contractor_id);
CREATE INDEX contractor_event_created_at ON dmn.contractor USING btree (event_created_at);
CREATE INDEX contractor_party_id ON dmn.contractor USING btree (party_id);



CREATE TABLE dmn.contractor_revision
(
    id       bigserial                                                        NOT NULL,
    obj_id   bigint                                                           NOT NULL,
    revision bigint                                                           NOT NULL,
    wtime    timestamp without time zone DEFAULT timezone('utc'::text, now()) NOT NULL,
    CONSTRAINT contractor_revision_pkey PRIMARY KEY (id)
);

CREATE UNIQUE INDEX contractor_revision_idx ON dmn.contractor_revision USING btree (obj_id, revision);



CREATE TABLE dmn.country
(
    id             bigserial                                                        NOT NULL,
    version_id     bigint                                                           NOT NULL,
    country_ref_id character varying                                                NOT NULL,
    name           character varying                                                NOT NULL,
    trade_bloc     text[]                                                           NOT NULL,
    wtime          timestamp without time zone DEFAULT timezone('utc'::text, now()) NOT NULL,
    current        boolean                     DEFAULT true                         NOT NULL,
    CONSTRAINT country_pkey PRIMARY KEY (id)
);



CREATE TABLE dmn.currency
(
    id              bigserial                                                        NOT NULL,
    version_id      bigint                                                           NOT NULL,
    currency_ref_id character varying                                                NOT NULL,
    name            character varying                                                NOT NULL,
    symbolic_code   character varying                                                NOT NULL,
    numeric_code    smallint                                                         NOT NULL,
    exponent        smallint                                                         NOT NULL,
    wtime           timestamp without time zone DEFAULT timezone('utc'::text, now()) NOT NULL,
    current         boolean                     DEFAULT true                         NOT NULL,
    CONSTRAINT currency_pkey PRIMARY KEY (id)
);

CREATE INDEX currency_idx ON dmn.currency USING btree (currency_ref_id);
CREATE INDEX currency_version_id ON dmn.currency USING btree (version_id);



CREATE TYPE dmn.blocking AS ENUM (
    'unblocked',
    'blocked'
    );

CREATE TYPE dmn.suspension AS ENUM (
    'active',
    'suspended'
    );

CREATE TABLE dmn.party
(
    id                         bigserial                                                        NOT NULL,
    event_created_at           timestamp without time zone                                      NOT NULL,
    party_id                   character varying                                                NOT NULL,
    contact_info_email         character varying                                                NOT NULL,
    created_at                 timestamp without time zone                                      NOT NULL,
    blocking                   dmn.blocking                                                      NOT NULL,
    blocking_unblocked_reason  character varying,
    blocking_unblocked_since   timestamp without time zone,
    blocking_blocked_reason    character varying,
    blocking_blocked_since     timestamp without time zone,
    suspension                 dmn.suspension                                                    NOT NULL,
    suspension_active_since    timestamp without time zone,
    suspension_suspended_since timestamp without time zone,
    revision                   bigint                                                           NOT NULL,
    revision_changed_at        timestamp without time zone,
    party_meta_set_ns          character varying,
    party_meta_set_data_json   character varying,
    wtime                      timestamp without time zone DEFAULT timezone('utc'::text, now()) NOT NULL,
    current                    boolean                     DEFAULT true                         NOT NULL,
    sequence_id                integer,
    change_id                  integer,
    CONSTRAINT party_pkey PRIMARY KEY (id),
    CONSTRAINT party_uniq UNIQUE (party_id, sequence_id, change_id)
);

CREATE INDEX party_contact_info_email ON dmn.party USING btree (contact_info_email);
CREATE INDEX party_created_at ON dmn.party USING btree (created_at);
CREATE INDEX party_current ON dmn.party USING btree (current);
CREATE INDEX party_event_created_at ON dmn.party USING btree (event_created_at);
CREATE INDEX party_party_id ON dmn.party USING btree (party_id);


CREATE TABLE dmn.shop
(
    id                         bigserial                                                        NOT NULL,
    event_created_at           timestamp without time zone                                      NOT NULL,
    party_id                   character varying                                                NOT NULL,
    shop_id                    character varying                                                NOT NULL,
    created_at                 timestamp without time zone                                      NOT NULL,
    blocking                   dmn.blocking                                                      NOT NULL,
    blocking_unblocked_reason  character varying,
    blocking_unblocked_since   timestamp without time zone,
    blocking_blocked_reason    character varying,
    blocking_blocked_since     timestamp without time zone,
    suspension                 dmn.suspension                                                    NOT NULL,
    suspension_active_since    timestamp without time zone,
    suspension_suspended_since timestamp without time zone,
    details_name               character varying                                                NOT NULL,
    details_description        character varying,
    location_url               character varying                                                NOT NULL,
    category_id                integer                                                          NOT NULL,
    account_currency_code      character varying,
    account_settlement         bigint,
    account_guarantee          bigint,
    account_payout             bigint,
    contract_id                character varying                                                NOT NULL,
    payout_tool_id             character varying,
    payout_schedule_id         integer,
    wtime                      timestamp without time zone DEFAULT timezone('utc'::text, now()) NOT NULL,
    current                    boolean                     DEFAULT true                         NOT NULL,
    sequence_id                integer,
    change_id                  integer,
    claim_effect_id            integer,
    CONSTRAINT shop_pkey PRIMARY KEY (id),
    CONSTRAINT shop_uniq UNIQUE (party_id, shop_id, sequence_id, change_id, claim_effect_id)
);




CREATE TABLE dmn.shop_revision
(
    id       bigserial                                                        NOT NULL,
    obj_id   bigint                                                           NOT NULL,
    revision bigint                                                           NOT NULL,
    wtime    timestamp without time zone DEFAULT timezone('utc'::text, now()) NOT NULL,
    CONSTRAINT shop_revision_pkey PRIMARY KEY (id)
);

CREATE UNIQUE INDEX shop_revision_idx ON dmn.shop_revision USING btree (obj_id, revision);



CREATE TYPE dmn.payout_tool_info AS ENUM (
    'russian_bank_account',
    'international_bank_account',
    'wallet_info',
    'payment_institution_account'
    );

CREATE TABLE dmn.payout_tool
(
    id                                                             bigserial                   NOT NULL,
    contract_id                                                    bigint                      NOT NULL,
    payout_tool_id                                                 character varying           NOT NULL,
    created_at                                                     timestamp without time zone NOT NULL,
    currency_code                                                  character varying           NOT NULL,
    payout_tool_info                                               dmn.payout_tool_info        NOT NULL,
    payout_tool_info_russian_bank_account                          character varying,
    payout_tool_info_russian_bank_name                             character varying,
    payout_tool_info_russian_bank_post_account                     character varying,
    payout_tool_info_russian_bank_bik                              character varying,
    payout_tool_info_international_bank_account_holder             character varying,
    payout_tool_info_international_bank_name                       character varying,
    payout_tool_info_international_bank_address                    character varying,
    payout_tool_info_international_bank_iban                       character varying,
    payout_tool_info_international_bank_bic                        character varying,
    payout_tool_info_international_bank_local_code                 character varying,
    payout_tool_info_international_bank_number                     character varying,
    payout_tool_info_international_bank_aba_rtn                    character varying,
    payout_tool_info_international_bank_country_code               character varying,
    payout_tool_info_international_correspondent_bank_account      character varying,
    payout_tool_info_international_correspondent_bank_name         character varying,
    payout_tool_info_international_correspondent_bank_address      character varying,
    payout_tool_info_international_correspondent_bank_bic          character varying,
    payout_tool_info_international_correspondent_bank_iban         character varying,
    payout_tool_info_international_correspondent_bank_number       character varying,
    payout_tool_info_international_correspondent_bank_aba_rtn      character varying,
    payout_tool_info_international_correspondent_bank_country_code character varying,
    payout_tool_info_wallet_info_wallet_id                         character varying,
    CONSTRAINT payout_tool_pkey PRIMARY KEY (id)
);

CREATE INDEX payout_tool_idx ON dmn.payout_tool USING btree (contract_id);

CREATE TABLE dmn.shedlock
(
    name       character varying(64) NOT NULL,
    lock_until timestamp(3) without time zone,
    locked_at  timestamp(3) without time zone,
    locked_by  character varying(255),
    CONSTRAINT shedlock_pkey PRIMARY KEY (name)
);