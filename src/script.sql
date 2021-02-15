create table sys_user (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    surname VARCHAR(50) NOT NULL,
    password INT NOT NULL
);

create table account (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    user_id INT REFERENCES sys_user(id),
    balance NUMERIC NOT NULL
);

create table transaction (
    id SERIAL PRIMARY KEY,
    trans_date TIMESTAMP NOT NULL,
    description VARCHAR(50) NOT NULL,
    acc_from INT REFERENCES account(id),
    acc_to INT REFERENCES account(id),
    trans_money NUMERIC NOT NULL
);

create table category (
    id SERIAL PRIMARY KEY,
    trans_type VARCHAR(50) NOT NULL
);

create table transaction_to_category (
    transaction_id INT REFERENCES transaction(id),
    category_id INT REFERENCES category(id)
);

insert into sys_user (name, surname, password) VALUES ('Artyom', 'Vasiliev', 1234); --1
insert into sys_user (name, surname, password) VALUES ('Vladimir', 'Kondratiev', 4321); --2
insert into sys_user (name, surname, password) VALUES ('Anton', 'Vlasov', 8902); --3
insert into sys_user (name, surname, password) VALUES ('Polina', 'Grigorieva', 8475); --4
insert into sys_user (name, surname, password) VALUES ('Nikita', 'Andreev', 6037); --5
insert into sys_user (name, surname, password) VALUES ('Inna', 'Vdovina', 9341); --6

insert into account (name, user_id, balance) VALUES ('Acc for salary', 1, 69000.23); --1
insert into account (name, user_id, balance) VALUES ('Acc for everyday trans', 1, 89000.50); --2
insert into account (name, user_id, balance) VALUES ('Acc for salary', 2, 37500.00); --3
insert into account (name, user_id, balance) VALUES ('Acc for insurance payments', 2, 69000.70); --4
insert into account (name, user_id, balance) VALUES ('Acc for salary', 3, 63250.00); --5
insert into account (name, user_id, balance) VALUES ('Acc for salary', 4, 20000.80); --6
insert into account (name, user_id, balance) VALUES ('Acc for salary', 5, 15560.90); --7
insert into account (name, user_id, balance) VALUES ('Acc for salary', 5, 180000.50); --8

insert into category (trans_type) VALUES ('Salary'); --1
insert into category (trans_type) VALUES ('Transfer between own accs'); --2
insert into category (trans_type) VALUES ('Purchase'); --3
insert into category (trans_type) VALUES ('transfer to another acc'); --4

insert into transaction (trans_date, description, acc_from, trans_money) VALUES ('2021-02-04 20:00:04'::timestamp, 'Purchase in market', 2, 20000.0); --1
insert into transaction (trans_date, description, acc_from, trans_money) VALUES ('2021-02-13 19:32:14'::timestamp, 'Transfer to friend', 2, 15000.0); --2
insert into transaction (trans_date, description, acc_from, acc_to, trans_money) VALUES ('2021-02-05 10:05:20'::timestamp, 'Transfer to everyday acc', 1, 2, 10500.0); --3
insert into transaction (trans_date, description, acc_to, trans_money) VALUES ('2021-01-28 19:10:00'::timestamp, 'Salary', 5, 6800.0); --4
insert into transaction (trans_date, description, acc_from, trans_money) VALUES ('2021-01-04 10:48:53'::timestamp, 'Payment for parking', 2, 5000.0); --5
insert into transaction (trans_date, description, acc_to, trans_money) VALUES ('2020-12-21 09:36:37'::timestamp, 'Salary', 6, 7000.0); --6
insert into transaction (trans_date, description, acc_from, acc_to, trans_money) VALUES ('2020-12-15 15:00:30'::timestamp, 'Transfer to insurance acc', 3, 4, 12500.0); --7
insert into transaction (trans_date, description, acc_to, trans_money) VALUES ('2020-12-18 20:40:58'::timestamp, 'Salary', 7, 16000.0); --8

insert into transaction_to_category (transaction_id, category_id) VALUES (1, 3);
insert into transaction_to_category (transaction_id, category_id) VALUES (2, 4);
insert into transaction_to_category (transaction_id, category_id) VALUES (3, 2);
insert into transaction_to_category (transaction_id, category_id) VALUES (4, 1);
insert into transaction_to_category (transaction_id, category_id) VALUES (5, 3);
insert into transaction_to_category (transaction_id, category_id) VALUES (5, 4);
insert into transaction_to_category (transaction_id, category_id) VALUES (6, 1);
insert into transaction_to_category (transaction_id, category_id) VALUES (7, 3);
insert into transaction_to_category (transaction_id, category_id) VALUES (7, 4);
insert into transaction_to_category (transaction_id, category_id) VALUES (8, 1);

select
    u.name,
    a.name
from sys_user as u
    left join account as a on u.id = a.user_id
where u.name = 'Artyom';

select
    u.name,
    t.description,
    t.trans_money,
    a.name
from sys_user as u
    left join account as a on u.id = a.user_id
    left join transaction as t on a.id = t.acc_from or a.id = t.acc_to
where u.name = 'Artyom'
    and t.trans_date between '2021-02-13 00:00:00'::timestamp and '2021-02-14 00:00:00'::timestamp;

select
    u.name,
    sum(a.balance)
from sys_user as u
         left join account as a on u.id = a.user_id
group by u.name;
