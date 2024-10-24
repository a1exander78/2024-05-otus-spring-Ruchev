insert into author(full_name)
values ('Author_1'), ('Author_2'), ('Author_3');

insert into genre(name)
values ('Genre_1'), ('Genre_2'), ('Genre_3');

insert into book(title, author_id, genre_id)
values ('Book_1', 1, 1), ('Book_2', 2, 2), ('Book_3', 3, 3);

insert into comment(description, book_id)
values ('Comment_1', 1), ('Comment_2', 2), ('Comment_3', 3);
