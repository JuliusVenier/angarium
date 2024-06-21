
-- angarium_user
-- Speichert die Metadaten einer Datei
create table file_meta_data
(
    id                uuid         not null,
    name              varchar(255) not null,
    max_downloads     integer      not null,
    current_downloads integer,
    creation_date     date         not null,
    deletion_date     date         not null,
    sha256            varchar(255),
    encrypted         boolean      not null,
    angarium_user     bigint       not null,
    constraint pk_file_meta_data primary key (id)
);

alter table file_meta_data
    add constraint FK_FILE_META_DATA_ON_ANGARIUM_USER foreign key (angarium_user) references angarium_user (id);


-- angarium_user
-- Speichert die Benutzer
create table angarium_user
(
    id       bigint       not null,
    username varchar(255) not null,
    password varchar(255) not null,
    role     varchar(255) not null,
    constraint pk_angarium_user primary key (id)
);

alter table angarium_user
    add constraint uc_angarium_user_username unique (username);