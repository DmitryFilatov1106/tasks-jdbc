INSERT INTO users (name, username, password)
VALUES ('Ivan Ivanov', 'ii@mail.ru', '$2a$10$Xl0yhvzLIaJCDdKBS0Lld.ksK7c2Zytg/ZKFdtIYYQUv8rUfvCR4W'),
       ('Petr Petrov', 'pp@mail.ru', '$2a$10$fFLij9aYgaNCFPTL9WcA/uoCRukxnwf.vOQ8nrEEOskrCNmGsxY7m');

INSERT INTO tasks (title, description, status, expiration_date)
VALUES ('Buy cheese', '-', 'TODO', '2024-01-29 12:00:00'),
       ('Do homework', 'Math, physics, literature', 'IN_PROGRESS', '2024-01-31 00:00:00'),
       ('Clean rooms', '-', 'DONE', '2024-03-10 00:00:00'),
       ('Call mike', 'Ask about meeting', 'TODO', '2024-02-01 00:00:00');

INSERT INTO users_tasks (task_id, user_id)
VALUES (1, 2),
       (2, 2),
       (3, 2),
       (4, 1);

INSERT INTO users_roles (user_id, role)
VALUES (1, 'ROLE_ADMIN'),
       (1, 'ROLE_USER'),
       (2, 'ROLE_USER');