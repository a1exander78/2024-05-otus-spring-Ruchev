insert into author(full_name)
values ('Author_Test_1'), ('Author_Test_2'), ('Author_Test_3');

insert into genre(name)
values ('Genre_Test_1'), ('Genre_Test_2'), ('Genre_Test_3');

insert into book(title, author_id, genre_id)
values ('Book_Test_1', 1, 1), ('Book_Test_2', 2, 2), ('Book_Test_3', 3, 3);

insert into comment(description, book_id)
values ('Comment_Test_1', 1), ('Comment_Test_2', 2), ('Comment_Test_3', 3), ('Comment_Test_4', 1);
