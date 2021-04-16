INSERT INTO OWNER (owner_id, owner) VALUES (OWNER_SEQ.nextval, 'Kiss János');

INSERT INTO CONTACT (contact_id, owner_id, contact_type, contact) VALUES (CONTACT_SEQ.nextval, OWNER_SEQ.currval, 'SMS', '+36301234567');
INSERT INTO CONTACT (contact_id, owner_id, contact_type, contact) VALUES (CONTACT_SEQ.nextval, OWNER_SEQ.currval, 'EMAIL', 'kiss.janos@example.com');

INSERT INTO CARD (card_number, valid_thru, card_type_id, owner_id) VALUES ('5555555555554444', '04/23', 1, OWNER_SEQ.currval);
INSERT INTO CARD (card_number, valid_thru, card_type_id, owner_id) VALUES ('4111111111111111', '02/22', 9, OWNER_SEQ.currval);