import { apiClient } from "./ApiClient";

export const addPhotoApi
    = (id, formData) => apiClient.post(`/albums/${id}/photos`, formData);

export const movePhotoApi
    = (id, photo) => apiClient.put(`/albums/${id}/photos/move`, photo);

export const deletePhotoApi
    = (id, photoIds) => apiClient.delete(`/albums/${id}/photos`, {
        data: {photoIds}
    });

export const downloadPhotoApi
    = (id, queryParams) => apiClient.get(`/albums/${id}/photos/download`, {
        params: queryParams,
    });