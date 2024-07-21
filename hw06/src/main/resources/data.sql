insert into author(full_name)
values ('Author1'), ('Author2'), ('Author3');

insert into genre(name)
values ('Genre1'), ('Genre2'), ('Genre3');

insert into book(title, author_id, genre_id)
values ('BookTitle1', 1, 1), ('BookTitle2', 2, 2), ('BookTitle3', 3, 3);

insert into comment(description, book_id)
values ('Comment_1', 1), ('Comment_2', 2), ('Comment_3', 3);
