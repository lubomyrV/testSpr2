
USE demoDB;

-- Create table
CREATE TABLE user(
    id                int NOT NULL AUTO_INCREMENT,
    username          varchar(36) NOT NULL,
    email             varchar(64),
    encryted_password varchar(128) NOT NULL,
    enabled           bool NOT NULL DEFAULT 1,
    PRIMARY KEY (id),
    CONSTRAINT uc_user UNIQUE (username)
);

-- Create table
CREATE TABLE role(
    id      int NOT NULL AUTO_INCREMENT,
    name    varchar(30) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uc_role UNIQUE (name)
);
 
-- Create table
CREATE TABLE user_role(
    id      int NOT NULL AUTO_INCREMENT,
    user_id int NOT NULL,
    role_id int NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uc_user_role UNIQUE (user_id, role_id)
);

ALTER TABLE user_role
ADD FOREIGN KEY (user_id) REFERENCES user(id);

ALTER TABLE user_role
ADD FOREIGN KEY (role_id) REFERENCES role(id);

-- Used by Spring Remember Me API.  
CREATE TABLE persistent_logins (
    series      varchar(64) NOT NULL,
    username    varchar(64) NOT NULL,
    token       varchar(64) NOT NULL,
    last_used   timestamp NOT NULL,
    PRIMARY KEY (series)
);

---
 
INSERT INTO role (name) VALUES ('ROLE_ADMIN');
 
INSERT INTO role (name) VALUES ('ROLE_USER');
 
---

--------------------------------------
INSERT INTO user (username, email, encryted_password)
VALUES ('admin', 'testAdmin@mail.com', '$2a$10$PrI5Gk9L.tSZiW9FXhTS8O8Mz9E97k2FZbFvGFFaSsiTUIl.TCrFu');

INSERT INTO user (username, encryted_password)
VALUES ('user', '$2a$10$PrI5Gk9L.tSZiW9FXhTS8O8Mz9E97k2FZbFvGFFaSsiTUIl.TCrFu');

--add disabled user
INSERT INTO user (username, encryted_password, enabled)
VALUES ('userD1', '$2a$10$PrI5Gk9L.tSZiW9FXhTS8O8Mz9E97k2FZbFvGFFaSsiTUIl.TCrFu', 0);

---

INSERT INTO user_role (user_id, role_id) VALUES (1, 1); 
INSERT INTO user_role (user_id, role_id) VALUES (1, 2);
 
INSERT INTO user_role (user_id, role_id) VALUES (2, 2);

INSERT INTO user_role (user_id, role_id) VALUES (3, 2);

-- Triggers 1
CREATE TRIGGER add_role_for_new_user 
   AFTER INSERT ON user FOR EACH ROW
    INSERT INTO user_role (user_id, role_id) VALUES (NEW.id, (SELECT id FROM role WHERE name = 'ROLE_USER'));
    
-- Triggers 2    
CREATE TRIGGER cleanup_user_role_before_delete_user
   BEFORE DELETE ON user FOR EACH ROW
    DELETE FROM user_role WHERE user_id IN (OLD.id);

--DROP TRIGGER cleanup_user_role_after_delete_user;
--SHOW TRIGGERS FROM test \G
