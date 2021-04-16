INSERT INTO OWNER (owner_id, owner) VALUES (1, 'Kiss János');

INSERT INTO CONTACT (contact_id, owner_id, contact_type, contact) VALUES (1, 1, 'SMS', '+36301234567');
INSERT INTO CONTACT (contact_id, owner_id, contact_type, contact) VALUES (2, 1, 'EMAIL', 'kiss.janos@example.com');

INSERT INTO CARD (card_number, valid_thru, card_type_id, owner_id) VALUES ('5555555555554444', '04/23', 1, 1);
INSERT INTO CARD (card_number, valid_thru, card_type_id, owner_id) VALUES ('4111111111111111', '02/22', 9, 1);