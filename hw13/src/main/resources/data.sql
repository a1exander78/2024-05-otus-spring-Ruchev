insert into author(full_name)
values ('Author_1'), ('Author_2'), ('Author_3'), ('Author_4'), ('Author_5'), ('Author_6'), ('Author_7'), ('Author_8'), ('Author_9');

insert into genre(name)
values ('Genre_1'), ('Genre_2'), ('Genre_3'), ('Genre_4'), ('Genre_5'), ('Genre_6'), ('Genre_7'), ('Genre_8'), ('Genre_9');

insert into book(title, author_id, genre_id)
values ('Book_1', 1, 1), ('Book_2', 2, 2), ('Book_3', 3, 3), ('Book_4', 4, 4), ('Book_5', 5, 5), ('Book_6', 6, 6), ('Book_7', 7, 7), ('Book_8', 8, 8), ('Book_9', 9, 9);

insert into comment(description, book_id)
values ('Comment_1', 1), ('Comment_2', 2), ('Comment_3', 3), ('Comment_4', 4), ('Comment_5', 5), ('Comment_6', 6), ('Comment_7', 7), ('Comment_8', 8), ('Comment_9', 9);