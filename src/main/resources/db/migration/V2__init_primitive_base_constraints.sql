alter table books

add constraint book_fk foreign key (user_id) references users(id)