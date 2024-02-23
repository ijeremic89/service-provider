CREATE TABLE provider
(
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE service
(
    id          BIGSERIAL PRIMARY KEY,
    description TEXT NOT NULL
);

CREATE TABLE provider_services
(
    provider_id BIGINT NOT NULL,
    service_id  BIGINT NOT NULL,
    PRIMARY KEY (provider_id, service_id),

    CONSTRAINT fk_provider FOREIGN KEY (provider_id) REFERENCES provider (id) ON DELETE CASCADE,
    CONSTRAINT fk_service FOREIGN KEY (service_id) REFERENCES service (id) ON DELETE CASCADE
);

CREATE INDEX idx_service_provider on provider_services (provider_id);
CREATE INDEX idx_service on provider_services (service_id);

INSERT INTO provider (id, name) VALUES (1, 'Telemach');
INSERT INTO provider (id, name) VALUES (2, 'A1');
INSERT INTO provider (id, name) VALUES (3, 'T-com');
INSERT INTO provider (id, name) VALUES (4, 'Iskon TV');
INSERT INTO provider (id, name) VALUES (5, 'TELE2');
INSERT INTO provider (id, name) VALUES (6, 'Hrvatski Telekom');
INSERT INTO provider (id, name) VALUES (7, 'Optima');

INSERT INTO service (id, description) VALUES (1, 'Kućni internet');
INSERT INTO service (id, description) VALUES (2, 'Kućni internet - OPTIKA');
INSERT INTO service (id, description) VALUES (3, 'Mobilni internet');
INSERT INTO service (id, description) VALUES (4, 'Kućni telefon');
INSERT INTO service (id, description) VALUES (5, 'Mobilni telefon');
INSERT INTO service (id, description) VALUES (6, 'Televizija');
INSERT INTO service (id, description) VALUES (7, 'TV to GO');

INSERT INTO provider_services (provider_id, service_id) VALUES (1, 2);
INSERT INTO provider_services (provider_id, service_id) VALUES (1, 3);
INSERT INTO provider_services (provider_id, service_id) VALUES (1, 5);
INSERT INTO provider_services (provider_id, service_id) VALUES (1, 6);
INSERT INTO provider_services (provider_id, service_id) VALUES (2, 2);
INSERT INTO provider_services (provider_id, service_id) VALUES (2, 3);
INSERT INTO provider_services (provider_id, service_id) VALUES (2, 5);
INSERT INTO provider_services (provider_id, service_id) VALUES (2, 6);
INSERT INTO provider_services (provider_id, service_id) VALUES (3, 1);
INSERT INTO provider_services (provider_id, service_id) VALUES (3, 4);
INSERT INTO provider_services (provider_id, service_id) VALUES (3, 7);
INSERT INTO provider_services (provider_id, service_id) VALUES (4, 6);
INSERT INTO provider_services (provider_id, service_id) VALUES (4, 7);
INSERT INTO provider_services (provider_id, service_id) VALUES (5, 1);
INSERT INTO provider_services (provider_id, service_id) VALUES (5, 2);
INSERT INTO provider_services (provider_id, service_id) VALUES (5, 3);
INSERT INTO provider_services (provider_id, service_id) VALUES (5, 4);
INSERT INTO provider_services (provider_id, service_id) VALUES (5, 5);
INSERT INTO provider_services (provider_id, service_id) VALUES (5, 6);
INSERT INTO provider_services (provider_id, service_id) VALUES (6, 6);
INSERT INTO provider_services (provider_id, service_id) VALUES (6, 1);
INSERT INTO provider_services (provider_id, service_id) VALUES (7, 1);
INSERT INTO provider_services (provider_id, service_id) VALUES (7, 2);
INSERT INTO provider_services (provider_id, service_id) VALUES (7, 4);

SELECT setval('provider_id_seq', (SELECT MAX(id) FROM provider));
SELECT setval('service_id_seq', (SELECT MAX(id) FROM service));