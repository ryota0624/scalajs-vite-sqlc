import { QueryArrayConfig, QueryArrayResult } from "pg";
interface Client {
    query: (config: QueryArrayConfig) => Promise<QueryArrayResult>;
}
export declare const getAuthorQuery = "-- name: GetAuthor :one\nSELECT id, name, bio FROM authors\nWHERE id = $1 LIMIT 1";
export interface GetAuthorArgs {
    id: string;
}
export interface GetAuthorRow {
    id: string;
    name: string;
    bio: string | null;
}
export declare function getAuthor(client: Client, args: GetAuthorArgs): Promise<GetAuthorRow | null>;
export declare const listAuthorsQuery = "-- name: ListAuthors :many\nSELECT id, name, bio FROM authors\nORDER BY name";
export interface ListAuthorsRow {
    id: string;
    name: string;
    bio: string | null;
}
export declare function listAuthors(client: Client): Promise<ListAuthorsRow[]>;
export declare const createAuthorQuery = "-- name: CreateAuthor :one\nINSERT INTO authors (\n    name, bio\n) VALUES (\n             $1, $2\n         )\n    RETURNING id, name, bio";
export interface CreateAuthorArgs {
    name: string;
    bio: string | null;
}
export interface CreateAuthorRow {
    id: string;
    name: string;
    bio: string | null;
}
export declare function createAuthor(client: Client, args: CreateAuthorArgs): Promise<CreateAuthorRow | null>;
export declare const updateAuthorQuery = "-- name: UpdateAuthor :exec\nUPDATE authors\nset name = $2,\n    bio = $3\nWHERE id = $1";
export interface UpdateAuthorArgs {
    id: string;
    name: string;
    bio: string | null;
}
export declare function updateAuthor(client: Client, args: UpdateAuthorArgs): Promise<void>;
export declare const deleteAuthorQuery = "-- name: DeleteAuthor :exec\nDELETE FROM authors\nWHERE id = $1";
export interface DeleteAuthorArgs {
    id: string;
}
export declare function deleteAuthor(client: Client, args: DeleteAuthorArgs): Promise<void>;
export {};
