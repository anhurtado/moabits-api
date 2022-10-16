INSERT INTO "movement_types" (name) VALUES ('debit');
INSERT INTO "movement_types" (name) VALUES ('credit');
INSERT INTO "movement_types" (name) VALUES ('transfer');

INSERT INTO "clients" (document_number, company_name) VALUES ('J123456789', 'Digitel Inc');
INSERT INTO "clients" (document_number, company_name) VALUES ('J987654321', 'Movistar Inc');

INSERT INTO "movements" (account_number, amount, created_at, client_id, movement_type_id, balance) VALUES ('12345', 100, now(), 1, 2, 100);
INSERT INTO "movements" (account_number, amount, created_at, client_id, movement_type_id, balance) VALUES ('12345', 20, now(), 1, 1, 80);
INSERT INTO "movements" (account_number, amount, created_at, client_id, movement_type_id, balance) VALUES ('12345', 30, now(), 1, 1, 50);
INSERT INTO "movements" (account_number, amount, created_at, client_id, movement_type_id, balance) VALUES ('12345', 50, now(), 1, 3, 0);
INSERT INTO "movements" (account_number, amount, created_at, client_id, movement_type_id, balance) VALUES ('12345', 60, now(), 1, 2, 60);

INSERT INTO "users" (name, username, password) VALUES ('Anthony Hurtado', 'anthony', '$2a$10$/fQDSGoJH5Mep8C2PdYKIOg9VyM132oOKXrc11E/tCrqzkxFw.WgK');