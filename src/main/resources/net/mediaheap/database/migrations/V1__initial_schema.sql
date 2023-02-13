CREATE TABLE File
(
    id         INTEGER PRIMARY KEY AUTOINCREMENT,
    path       TEXT NOT NULL,

    sha256Hash TEXT NOT NULL,
    sha512Hash TEXT NOT NULL,
    md5Hash    TEXT NOT NULL,

    fileType   TEXT
);

CREATE TABLE Tag
(
    id        INTEGER PRIMARY KEY AUTOINCREMENT,
    fileId    INTEGER NOT NULL,

    namespace TEXT    NOT NULL,
    key       TEXT    NOT NULL,
    value     TEXT    NOT NULL,

    FOREIGN KEY (fileId) REFERENCES File (id)
);

CREATE INDEX file_path_idx on File (path);
CREATE INDEX tag_file_idx ON Tag (fileId);
CREATE INDEX tag_file_namespace_idx on Tag (fileId, namespace);
CREATE INDEX tag_file_namespace_key_idx ON Tag (fileId, namespace, key);