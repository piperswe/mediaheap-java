CREATE VIRTUAL TABLE FileSearch USING fts5
(
    id,
    path,
    content=File,
    content_rowid=id
);

CREATE VIRTUAL TABLE TagSearch USING fts5
(
    namespace,
    key,
    value,
    content=Tag,
    content_rowid=id
);

CREATE TRIGGER FileSearch_ai AFTER INSERT ON File BEGIN
    INSERT INTO FileSearch(rowid, path) VALUES (new.id, new.path);
END;

CREATE TRIGGER FileSearch_ad AFTER DELETE ON File BEGIN
    INSERT INTO FileSearch(FileSearch, rowid, path) VALUES ('delete', old.id, old.path);
END;

CREATE TRIGGER FileSearch_au AFTER UPDATE ON File BEGIN
    INSERT INTO FileSearch(FileSearch, rowid, path) VALUES ('delete', old.id, old.path);
    INSERT INTO FileSearch(rowid, path) VALUES (new.id, new.path);
END;

CREATE TRIGGER TagSearch_ai AFTER INSERT ON Tag BEGIN
    INSERT INTO TagSearch(rowid, namespace, key, value) VALUES (new.id, new.namespace, new.key, new.value);
END;

CREATE TRIGGER TagSearch_ad AFTER DELETE ON Tag BEGIN
    INSERT INTO TagSearch(TagSearch, rowid, namespace, key, value) VALUES ('delete', old.id, old.namespace, old.key, old.value);
END;

CREATE TRIGGER TagSearch_au AFTER UPDATE ON Tag BEGIN
    INSERT INTO TagSearch(TagSearch, rowid, namespace, key, value) VALUES ('delete', old.id, old.namespace, old.key, old.value);
    INSERT INTO TagSearch(rowid, namespace, key, value) VALUES (new.id, new.namespace, new.key, new.value);
END;