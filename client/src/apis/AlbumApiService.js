import { apiClient } from "./ApiClient";

export const retrieveAlbumListApi
    = (queryParams) => apiClient.get('/albums', {
        params: queryParams,
    });

export const retrieveAlbumApi
    = (id) => apiClient.get(`/albums/${id}`);